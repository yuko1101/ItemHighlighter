package io.github.yuko1101.itemhighlighter

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import io.github.yuko1101.itemhighlighter.command.ItemHighlighterCommand
import io.github.yuko1101.itemhighlighter.util.ConfigFile
import io.github.yuko1101.itemhighlighter.util.ItemPredicate
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.command.argument.ItemPredicateArgumentType
import net.minecraft.item.ItemStack
import net.minecraft.registry.BuiltinRegistries
import net.minecraft.registry.RegistryWrapper
import net.minecraft.server.command.CommandManager
import org.lwjgl.glfw.GLFW
import java.io.File

@Suppress("unused")
object ItemHighlighter : ClientModInitializer {

    val addItemKeyBinding: KeyBinding = KeyBinding("key.itemhighlighter.add", GLFW.GLFW_KEY_SEMICOLON, "key.categories.itemhighlighter")

    val configFile: ConfigFile = ConfigFile(File("config/item-highlighter.json"), JsonObject().apply { add("valuableItems", JsonArray()) } )
    val valuableItems: MutableSet<ItemPredicate> = mutableSetOf()

    val mc: MinecraftClient by lazy {
        MinecraftClient.getInstance()
    }

    val registries: RegistryWrapper.WrapperLookup by lazy {
        BuiltinRegistries.createWrapperLookup()
    }
    val itemPredicateReader: ItemPredicateArgumentType by lazy {
        ItemPredicateArgumentType.itemPredicate(CommandManager.createRegistryAccess(registries))
    }

    override fun onInitializeClient() {
        loadConfig()
        println("ItemHighlighter config loaded at ${configFile.file.absolutePath}")

        ClientCommandRegistrationCallback.EVENT.register(ItemHighlighterCommand::register)

        KeyBindingHelper.registerKeyBinding(addItemKeyBinding)
    }

    fun loadConfig() {
        valuableItems.clear()
        valuableItems.addAll(configFile.load().getValue("valuableItems").asJsonArray.map {
            ItemPredicate(it.asString)
        })
    }

    fun getMatchedConditions(item: ItemStack): Set<ItemPredicate> {
        return valuableItems.filter { it.test(item) }.toSet()
    }

    fun addItem(item: String) {
        val newList = configFile.getValue("valuableItems").asJsonArray.apply { add(item) }
        configFile.set("valuableItems", newList).save()
        loadConfig()
    }

    fun removeItem(item: String): Boolean {
        val newList = configFile.getValue("valuableItems").asJsonArray
        val hasRemoved = newList.remove(JsonPrimitive(item))
        configFile.set("valuableItems", newList).save()
        loadConfig()
        return hasRemoved
    }
}