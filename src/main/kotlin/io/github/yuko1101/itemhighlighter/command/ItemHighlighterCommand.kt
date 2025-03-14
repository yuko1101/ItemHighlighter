package io.github.yuko1101.itemhighlighter.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.github.yuko1101.itemhighlighter.ItemHighlighter
import io.github.yuko1101.itemhighlighter.util.ComponentUtil.asString
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.command.argument.ItemPredicateArgumentType
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
                    Command.SINGLE_SUCCESS
                }
            )
            .then(literal("add")
                .then(argument("item", ItemPredicateArgumentType.itemPredicate(registryAccess))
                    .executes {
                        val item = StringArgumentType.getString(it, "item")
                        ItemHighlighter.addItem(item)
                        Command.SINGLE_SUCCESS
                    }
                )
            )
            .then(literal("remove")
                .then(argument("item", ItemPredicateArgumentType.itemPredicate(registryAccess))
                    .executes {
                        val item = StringArgumentType.getString(it, "item")
                        ItemHighlighter.removeItem(item)
                        Command.SINGLE_SUCCESS
                    }
                )
            )
            .then(literal("mainhand")
                .then(literal("add")
                    .executes {
                        val itemStack = MinecraftClient.getInstance().player?.mainHandStack
                        val itemString = itemStack?.asString() ?: return@executes Command.SINGLE_SUCCESS
                        ItemHighlighter.addItem(itemString)
                        Command.SINGLE_SUCCESS
                    }
                )
                .then(literal("remove")
                    .executes {
                        val itemStack = MinecraftClient.getInstance().player?.mainHandStack
                        val itemString = itemStack?.asString() ?: return@executes Command.SINGLE_SUCCESS
                        val hasRemoved = ItemHighlighter.removeItem(itemString)
                        if (!hasRemoved) {
                            val matchedConditions = ItemHighlighter.getMatchedConditions(itemStack)
                            if (matchedConditions.isNotEmpty()) {
                                it.source.sendFeedback(Text.literal("Matched ${matchedConditions.size} conditions:"))
                                for (condition in matchedConditions) {
                                    it.source.sendFeedback(
                                        Text.literal(" - ")
                                            .append(Text.literal(condition.toString()).setStyle(Style.EMPTY.withClickEvent(ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/itemhighlighter remove $condition"))))
                                    )
                                }
                            } else {
                                it.source.sendFeedback(Text.literal("No matched conditions."))
                            }
                        }
                        Command.SINGLE_SUCCESS
                    }
                )
            )
    }
}