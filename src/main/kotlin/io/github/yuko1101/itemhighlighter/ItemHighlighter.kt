package io.github.yuko1101.itemhighlighter

import net.fabricmc.api.ClientModInitializer
import net.minecraft.item.Items

@Suppress("unused")
object ItemHighlighter : ClientModInitializer {

    // TODO: add more items
    val needlessItems = arrayListOf(
        Items.WOODEN_SHOVEL, Items.WOODEN_PICKAXE, Items.WOODEN_AXE, Items.WOODEN_HOE, Items.WOODEN_SWORD,
        Items.STONE_SHOVEL, Items.STONE_PICKAXE, Items.STONE_AXE, Items.STONE_HOE, Items.STONE_SWORD,
    )

    override fun onInitializeClient() {

    }
}