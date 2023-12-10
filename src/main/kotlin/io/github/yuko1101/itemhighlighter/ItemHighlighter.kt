package io.github.yuko1101.itemhighlighter

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mojang.brigadier.StringReader
import io.github.yuko1101.itemhighlighter.command.ItemHighlighterCommand
import io.github.yuko1101.itemhighlighter.util.ConfigFile
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.minecraft.command.argument.ItemStackArgumentType
import net.minecraft.command.argument.ItemStringReader
import net.minecraft.command.argument.ItemStringReader.ItemResult
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryWrapper
import java.io.File

@Suppress("unused")
object ItemHighlighter : ClientModInitializer {

    val configFile: ConfigFile = ConfigFile(File("config/item-highlighter.json"), JsonObject().apply { add("needlessItems", JsonArray()) } )
    val needlessItems: MutableSet<ItemResult> = mutableSetOf()

    override fun onInitializeClient() {
        loadConfig()

        ClientCommandRegistrationCallback.EVENT.register(ItemHighlighterCommand::register)
    }

    fun loadConfig() {
        needlessItems.clear()
        needlessItems.addAll(configFile.load().getValue("needlessItems").asJsonArray.map {
            ItemStringReader.item(Registries.ITEM.readOnlyWrapper, StringReader(it.asString))
        }.filter { it.item.type != Items.AIR })
    }
}