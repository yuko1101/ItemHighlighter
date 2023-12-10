package io.github.yuko1101.itemhighlighter.util

import net.minecraft.command.argument.ItemStringReader.ItemResult
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound

object NBTUtil {

    fun NbtCompound?.satisfy(nbt: NbtCompound?): Boolean {
        if (nbt == null) return true
        if (this == null) return false

        nbt.keys.forEach { key ->
            if (!this.contains(key)) return@satisfy false
            val thisTag = this.get(key)
            val thatTag = nbt.get(key)
            if (thisTag is NbtCompound && thatTag is NbtCompound) {
                if (!thisTag.satisfy(thatTag)) return@satisfy false
            } else {
                if ((thisTag == null) != (thatTag == null)) return@satisfy false
                if (thisTag != null) {
                    if (thisTag.nbtType != thatTag!!.nbtType) return@satisfy false
                    if (thisTag.asString() != thatTag.asString()) return@satisfy false
                }
            }
        }
        return true
    }

    fun ItemStack.asString(): String {
        return this.item.registryKey() + (if (this.hasNbt()) this.nbt.toString() else "")
    }

    fun ItemResult.asString(): String {
        return this.item.value().registryKey() + (if (this.nbt != null) this.nbt.toString() else "")
    }

    fun Item.registryKey(): String {
        return this.registryEntry.registryKey().value.toString()
    }
}