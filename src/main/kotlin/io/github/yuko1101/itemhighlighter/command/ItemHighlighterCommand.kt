package io.github.yuko1101.itemhighlighter.command

import com.google.gson.JsonPrimitive
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.github.yuko1101.itemhighlighter.ItemHighlighter
import io.github.yuko1101.itemhighlighter.util.NBTUtil.asString
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.command.argument.ItemStackArgumentType
import net.minecraft.text.ClickEvent
import net.minecraft.text.Style
import net.minecraft.text.Text

object ItemHighlighterCommand : ICommand {
    override fun getCommand(registryAccess: CommandRegistryAccess): LiteralArgumentBuilder<FabricClientCommandSource> {
        return literal("itemhighlighter")
            .then(literal("reload")
                .executes {
                    ItemHighlighter.loadConfig()
                    1
                }
            )
            .then(literal("add")
                .then(argument("item", ItemStackArgumentType.itemStack(registryAccess))
                    .executes {
                        val item = ItemStackArgumentType.getItemStackArgument(it, "item").asString()
                        addItem(item)
                        1
                    }
                )
            )
            .then(literal("remove")
                .then(argument("item", ItemStackArgumentType.itemStack(registryAccess))
                    .executes {
                        val item = ItemStackArgumentType.getItemStackArgument(it, "item").asString()
                        removeItem(item)
                        1
                    }
                )
            )
            .then(literal("mainhand")
                .then(literal("add")
                    .executes {
                        val itemStack = MinecraftClient.getInstance().player?.mainHandStack
                        if (itemStack == null || itemStack.item == null) return@executes 1
                        addItem(itemStack.asString())
                        1
                    }
                )
                .then(literal("remove")
                    .executes {
                        val itemStack = MinecraftClient.getInstance().player?.mainHandStack
                        if (itemStack == null || itemStack.item == null) return@executes 1
                        val hasRemoved = removeItem(itemStack.asString())
                        if (!hasRemoved) {
                            val matchedConditions = ItemHighlighter.getMatchedConditions(itemStack)
                            if (matchedConditions.isNotEmpty()) {
                                it.source.sendFeedback(Text.literal("Matched ${matchedConditions.size} conditions:"))
                                for (condition in matchedConditions) {
                                    it.source.sendFeedback(
                                        Text.literal(" - ")
                                            .append(Text.literal(condition.asString()).setStyle(Style.EMPTY.withClickEvent(ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/itemhighlighter remove ${condition.asString()}"))))
                                    )
                                }
                            } else {
                                it.source.sendFeedback(Text.literal("No matched conditions."))
                            }
                        }
                        1
                    }
                )
            )
    }

    private fun addItem(item: String) {
        val newList = ItemHighlighter.configFile.getValue("valuableItems").asJsonArray.apply { add(item) }
        ItemHighlighter.configFile.set("valuableItems", newList).save()
        ItemHighlighter.loadConfig()
    }

    private fun removeItem(item: String): Boolean {
        val newList = ItemHighlighter.configFile.getValue("valuableItems").asJsonArray
        val hasRemoved = newList.remove(JsonPrimitive(item))
        ItemHighlighter.configFile.set("valuableItems", newList).save()
        ItemHighlighter.loadConfig()
        return hasRemoved
    }
}