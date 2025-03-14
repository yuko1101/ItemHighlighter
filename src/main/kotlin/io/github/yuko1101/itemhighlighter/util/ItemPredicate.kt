package io.github.yuko1101.itemhighlighter.util

import com.mojang.brigadier.StringReader
import io.github.yuko1101.itemhighlighter.ItemHighlighter
import net.minecraft.item.ItemStack
import java.util.function.Predicate

class ItemPredicate(private val raw: String) : Predicate<ItemStack> {
    private val delegate: Predicate<ItemStack> by lazy {
        ItemHighlighter.itemPredicateReader.parse(StringReader(raw))
    }

    override fun test(itemStack: ItemStack): Boolean {
        return delegate.test(itemStack)
    }

    override fun toString(): String {
        return raw
    }
}