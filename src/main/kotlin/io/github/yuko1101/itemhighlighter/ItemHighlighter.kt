package io.github.yuko1101.itemhighlighter

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mojang.brigadier.StringReader
import io.github.yuko1101.itemhighlighter.command.ItemHighlighterCommand
import io.github.yuko1101.itemhighlighter.util.ConfigFile
import io.github.yuko1101.itemhighlighter.util.NBTUtil.asString
import io.github.yuko1101.itemhighlighter.util.NBTUtil.satisfy
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.option.KeyBinding
import net.minecraft.command.argument.ItemStringReader
import net.minecraft.command.argument.ItemStringReader.ItemResult
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.screen.ScreenHandler
import org.lwjgl.glfw.GLFW
import java.io.File

@Suppress("unused")
object ItemHighlighter : ClientModInitializer {

    val addItemKeyBinding: KeyBinding = KeyBinding("key.itemhighlighter.add", GLFW.GLFW_KEY_SEMICOLON, "key.categories.itemhighlighter")

    val configFile: ConfigFile = ConfigFile(File("config/item-highlighter.json"), JsonObject().apply { add("valuableItems", JsonArray()) } )
    val valuableItems: MutableSet<ItemResult> = mutableSetOf()

    val mc by lazy {
        MinecraftClient.getInstance()
    }

    override fun onInitializeClient() {
        loadConfig()
        println("ItemHighlighter config loaded at ${configFile.file.absolutePath}")

        ClientCommandRegistrationCallback.EVENT.register(ItemHighlighterCommand::register)

        KeyBindingHelper.registerKeyBinding(addItemKeyBinding)
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick {
            if (addItemKeyBinding.wasPressed()) {
                addCurrentItem()
            }
        })
    }

    fun loadConfig() {
        valuableItems.clear()
        valuableItems.addAll(configFile.load().getValue("valuableItems").asJsonArray.map {
            ItemStringReader.item(Registries.ITEM.readOnlyWrapper, StringReader(it.asString))
        }.filter { it.item.type != Items.AIR })
    }

    fun getMatchedConditions(item: ItemStack): Set<ItemResult> {
        return valuableItems.filter { item.item == it.item.value() && item.nbt.satisfy(it.nbt) }.toSet()
    }

    fun addCurrentItem() {
        val currentScreen = mc.currentScreen
        val itemStack: ItemStack? = if (currentScreen != null) {
            if (currentScreen is HandledScreen<*>) {
                // TODO: make it needless to click the item, just get the item under the cursor if possible
                currentScreen.screenHandler.cursorStack
            } else {
                null
            }
        } else {
            mc.player?.mainHandStack
        }
        val str = itemStack?.asString() ?: return
        println(str)
        ItemHighlighterCommand.addItem(str)
    }
}