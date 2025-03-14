package io.github.yuko1101.itemhighlighter.util

import io.github.yuko1101.itemhighlighter.ItemHighlighter
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound

object ComponentUtil {
    fun ItemStack.asString(): String? {
        if (this.isEmpty) return null
        val nbt = this.toNbt(ItemHighlighter.registries) as NbtCompound
        val componentsNbt = nbt.getCompound("components")
        val componentsString = if (!componentsNbt.isEmpty) componentsNbt.keys.joinToString(",", "[", "]") { key ->
            val value = componentsNbt.get(key).toString()
            "$key=$value"
        } else ""
        return "${this.item.registryKey()}$componentsString"
    }

    @Suppress("DEPRECATION")
    fun Item.registryKey(): String {
        return this.registryEntry.registryKey().value.toString()
    }
}