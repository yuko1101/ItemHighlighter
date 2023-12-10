package io.github.yuko1101.itemhighlighter.command

import com.google.gson.JsonPrimitive
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.github.yuko1101.itemhighlighter.ItemHighlighter
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.command.argument.ItemStackArgumentType

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
                        val newList = ItemHighlighter.configFile.getValue("needlessItems").asJsonArray.apply { add(item) }
                        ItemHighlighter.configFile.set("needlessItems", newList).save()
                        ItemHighlighter.loadConfig()
                        1
                    }
                )
            )
            .then(literal("remove")
                .then(argument("item", ItemStackArgumentType.itemStack(registryAccess))
                    .executes {
                        val item = ItemStackArgumentType.getItemStackArgument(it, "item").asString()
                        val newList = ItemHighlighter.configFile.getValue("needlessItems").asJsonArray.apply { remove(JsonPrimitive(item)) }
                        ItemHighlighter.configFile.set("needlessItems", newList).save()
                        ItemHighlighter.loadConfig()
                        1
                    }
                )
            )
    }
}