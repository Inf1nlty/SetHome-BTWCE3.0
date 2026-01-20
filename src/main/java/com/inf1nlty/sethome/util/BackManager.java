package com.inf1nlty.sethome.util;

import com.inf1nlty.sethome.BackPoint;
import net.minecraft.src.EntityPlayer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class BackManager {
    private static final Map<String, BackPoint> BACK_MAP = new ConcurrentHashMap<>();

    // Internal API: other code (commands, mixins) should call this to record pre-teleport / death points.
    public static void setBack(EntityPlayer player, double x, double y, double z, int dim) {
        if (player == null) return;
        BACK_MAP.put(player.username, new BackPoint(x, y, z, dim, System.currentTimeMillis()));
    }

    public static void setBack(EntityPlayer player, BackPoint bp) {
        if (player == null || bp == null) return;
        BACK_MAP.put(player.username, bp);
    }

    public static void setBackToCurrent(EntityPlayer player) {
        if (player == null) return;
        setBack(player, player.posX, player.posY, player.posZ, player.dimension);
    }

    public static BackPoint getBack(EntityPlayer player) {
        if (player == null) return null;
        return BACK_MAP.get(player.username);
    }

    // Not exposed via commands: removal only via code if needed.
    public static BackPoint removeBack(EntityPlayer player) {
        if (player == null) return null;
        return BACK_MAP.remove(player.username);
    }

    public static boolean hasBack(EntityPlayer player) {
        return getBack(player) != null;
    }
}