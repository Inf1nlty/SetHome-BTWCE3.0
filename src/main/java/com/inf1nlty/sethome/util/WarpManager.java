package com.inf1nlty.sethome.util;

import com.inf1nlty.sethome.WarpPoint;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;

import java.util.*;

public class WarpManager {
    private static final List<WarpPoint> warpList = new ArrayList<>();
    private static final String NBT_KEY = "WarpPoints";

    public static void writeWarpsToNBT(NBTTagCompound nbt) {
        NBTTagList tagList = new NBTTagList();
        for (WarpPoint wp : warpList) {
            tagList.appendTag(wp.writeToNBT());
        }
        nbt.setTag(NBT_KEY, tagList);
    }

    public static void readWarpsFromNBT(NBTTagCompound nbt) {
        warpList.clear();
        if (!nbt.hasKey(NBT_KEY)) return;
        NBTTagList tagList = nbt.getTagList(NBT_KEY);
        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
            warpList.add(WarpPoint.readFromNBT(tag));
        }
    }

    public static List<WarpPoint> getWarps() {
        return new ArrayList<>(warpList);
    }

    public static void addWarp(WarpPoint wp) {
        warpList.removeIf(w -> w.name.equalsIgnoreCase(wp.name));
        warpList.add(wp);
    }

    public static boolean delWarp(String name) {
        return warpList.removeIf(w -> w.name.equalsIgnoreCase(name));
    }

    public static WarpPoint getWarp(String name) {
        return warpList.stream().filter(w -> w.name.equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}