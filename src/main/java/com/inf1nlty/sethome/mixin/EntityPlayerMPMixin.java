package com.inf1nlty.sethome.mixin;

import com.inf1nlty.sethome.util.WarpManager;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerMP.class)
public class EntityPlayerMPMixin {

    @Inject(method = "writeEntityToNBT", at = @At("TAIL"))
    public void writeWarpPoints(NBTTagCompound tag, CallbackInfo ci) {
        WarpManager.writeWarpsToNBT(tag);
    }

    @Inject(method = "readEntityFromNBT", at = @At("TAIL"))
    public void readWarpPoints(NBTTagCompound tag, CallbackInfo ci) {
        WarpManager.readWarpsFromNBT(tag);
    }
}