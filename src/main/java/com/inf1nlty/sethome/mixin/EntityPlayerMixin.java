package com.inf1nlty.sethome.mixin;

import com.inf1nlty.sethome.HomePoint;
import com.inf1nlty.sethome.util.HomeManager;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EntityPlayer.class)
public class EntityPlayerMixin {

    @Inject(method = "writeEntityToNBT", at = @At("TAIL"))
    public void writeHomePoints(NBTTagCompound tag, CallbackInfo ci) {
        EntityPlayer player = (EntityPlayer) (Object) this;
        List<HomePoint> homes = HomeManager.getHomes(player);
        HomeManager.writeHomesToNBT(tag, homes);
    }

    @Inject(method = "readEntityFromNBT", at = @At("TAIL"))
    public void readHomePoints(NBTTagCompound tag, CallbackInfo ci) {
        EntityPlayer player = (EntityPlayer) (Object) this;
        List<HomePoint> homes = HomeManager.readHomesFromNBT(tag);
        HomeManager.setHomes(player, homes);
    }
}