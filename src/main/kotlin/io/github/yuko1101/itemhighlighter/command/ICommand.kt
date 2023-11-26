package io.github.yuko1101.itemhighlighter.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess

interface ICommand {
    fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
        dispatcher.register(getCommand(registryAccess))
    }

    fun getCommand(registryAccess: CommandRegistryAccess): LiteralArgumentBuilder<FabricClientCommandSource>
}