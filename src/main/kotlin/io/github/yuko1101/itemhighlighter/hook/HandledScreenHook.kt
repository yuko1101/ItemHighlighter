package io.github.yuko1101.itemhighlighter.hook

import io.github.yuko1101.itemhighlighter.ItemHighlighter
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.screen.slot.Slot
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object HandledScreenHook {
    @JvmStatic
    fun drawSlot(drawContext: DrawContext, slot: Slot) {
        if (!slot.hasStack()) return
        val itemStack = slot.stack
        if (!ItemHighlighter.needlessItems.contains(itemStack.item)) return

        val scaleFactor = 0.9f
        val text = Text.literal("❌").formatted(Formatting.RED)

        drawContext.matrices.push()
        drawContext.matrices.translate(slot.x.toFloat(), slot.y.toFloat(), 260f)
        drawContext.matrices.scale(scaleFactor, scaleFactor, 0f)

        drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, text, 0, 0, -1)

        drawContext.matrices.pop()
    }
}