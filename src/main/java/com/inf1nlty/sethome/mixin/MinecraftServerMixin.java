package com.inf1nlty.sethome.mixin;

import com.inf1nlty.sethome.command.BackCommand;
import com.inf1nlty.sethome.command.HomeCommand;
import com.inf1nlty.sethome.command.TPACommand;
import com.inf1nlty.sethome.command.WarpCommand;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "tick", at = @At("RETURN"))
    private void tick(CallbackInfo ci) {
        HomeCommand.onServerTick();
        WarpCommand.onServerTick();
        TPACommand.onServerTick();
        BackCommand.onServerTick();
    }
}