package io.github.yuko1101.itemhighlighter.mixin;

import io.github.yuko1101.itemhighlighter.hook.HandledScreenHook;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.gui.screen.ingame.HandledScreen.class)
public class HandledScreenMixin {
    @Inject(method = "drawSlot", at = @At("TAIL"))
    public void drawSlot(DrawContext context, Slot slot, CallbackInfo ci) {
        HandledScreenHook.drawSlot(context, slot);
    }
}
