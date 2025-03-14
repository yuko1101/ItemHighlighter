package io.github.yuko1101.itemhighlighter.mixin;

import io.github.yuko1101.itemhighlighter.hook.HandledScreenHook;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.client.gui.screen.ingame.HandledScreen.class)
public class HandledScreenMixin {
    @Shadow @Nullable protected Slot focusedSlot;

    @Inject(method = "drawSlot", at = @At("TAIL"))
    public void drawSlot(DrawContext context, Slot slot, CallbackInfo ci) {
        HandledScreenHook.drawSlot(context, slot);
    }

    @Inject(method = "keyPressed", at = @At("HEAD"))
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        HandledScreenHook.keyPressed(keyCode, scanCode, modifiers, focusedSlot);
    }
}
