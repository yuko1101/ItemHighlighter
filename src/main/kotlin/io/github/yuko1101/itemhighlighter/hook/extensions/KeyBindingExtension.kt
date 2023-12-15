package io.github.yuko1101.itemhighlighter.hook.extensions

import net.minecraft.client.util.InputUtil

interface KeyBindingExtension {

    fun `itemhighlighter$getBoundKey`(): InputUtil.Key
}