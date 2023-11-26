package io.github.yuko1101.itemhighlighter

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.yuko1101.itemhighlighter.command.ItemHighlighterCommand
import io.github.yuko1101.itemhighlighter.util.ConfigFile
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import java.io.File

@Suppress("unused")
object ItemHighlighter : ClientModInitializer {

    val configFile: ConfigFile = ConfigFile(File("config/item-highlighter.json"), JsonObject().apply { add("needlessItems", JsonArray()) } )
    val needlessItems: MutableSet<Item> = mutableSetOf()

    override fun onInitializeClient() {
        loadConfig()

        ClientCommandRegistrationCallback.EVENT.register(ItemHighlighterCommand::register)
    }

    fun loadConfig() {
        val items = Registries.ITEM.entrySet
        needlessItems.clear()
        needlessItems.addAll(configFile.load().getValue("needlessItems").asJsonArray.map {
            items.find { item -> item.key.value.toString() == it.asString }?.value ?: Items.AIR
        }.filter { it != Items.AIR })
    }
}