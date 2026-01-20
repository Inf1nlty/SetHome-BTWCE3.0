package com.inf1nlty.sethome.mixin;

import com.inf1nlty.sethome.BackPoint;
import com.inf1nlty.sethome.util.BackManager;
import com.inf1nlty.sethome.util.WarpManager;
import net.minecraft.src.DamageSource;
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

        EntityPlayerMP player = (EntityPlayerMP) (Object) this;
        BackPoint bp = BackManager.getBack(player);
        if (bp != null) {
            tag.setTag("BackPoint", bp.writeToNBT());
        }
    }

    @Inject(method = "readEntityFromNBT", at = @At("TAIL"))
    public void readWarpPoints(NBTTagCompound tag, CallbackInfo ci) {
        WarpManager.readWarpsFromNBT(tag);

        EntityPlayerMP player = (EntityPlayerMP) (Object) this;
        if (tag.hasKey("BackPoint")) {
            NBTTagCompound backTag = tag.getCompoundTag("BackPoint");
            BackPoint bp = BackPoint.readFromNBT(backTag);
            BackManager.setBack(player, bp);
        }
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    public void onDeathInject(DamageSource source, CallbackInfo ci) {
        EntityPlayerMP player = (EntityPlayerMP) (Object) this;
        BackManager.setBack(player, player.posX, player.posY, player.posZ, player.dimension);
    }
}