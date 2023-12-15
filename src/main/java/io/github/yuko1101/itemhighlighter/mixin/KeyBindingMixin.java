package io.github.yuko1101.itemhighlighter.mixin;

import io.github.yuko1101.itemhighlighter.hook.extensions.KeyBindingExtension;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(KeyBinding.class)
public class KeyBindingMixin implements KeyBindingExtension {
    @Shadow
    private InputUtil.Key boundKey;


    @Unique
    @Override
    public InputUtil.@NotNull Key itemhighlighter$getBoundKey() {
        return this.boundKey;
    }
}
