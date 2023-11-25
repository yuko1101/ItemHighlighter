package io.github.yuko1101.itemhighlighter

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.yuko1101.itemhighlighter.util.ConfigFile
import net.fabricmc.api.ClientModInitializer
import net.minecraft.item.Item
import net.minecraft.item.Items
import java.io.File

@Suppress("unused")
object ItemHighlighter : ClientModInitializer {

    private val configFile: ConfigFile = ConfigFile(File("config/item-highlighter.json"), JsonObject().apply { add("needlessItems", JsonArray()) } )
    val needlessItems: ArrayList<Item> = arrayListOf()

    override fun onInitializeClient() {
        loadConfig()
    }

    // TODO: add reload command
    private fun loadConfig() {
        needlessItems.clear()
        needlessItems.addAll(configFile.load().getValue("needlessItems").asJsonArray.map {
            try {
                Items::class.java.getField(it.asString).get(null) as Item
            } catch (e: NoSuchFieldException) {
                Items.AIR
            }
        }.filter { it != Items.AIR })
    }

}