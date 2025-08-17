package com.inf1nlty.sethome.util;

import com.inf1nlty.sethome.HomePoint;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;

import java.util.*;

public class HomeManager {
    private static final Map<String, List<HomePoint>> homeMap = new HashMap<>();
    private static final String NBT_KEY = "HomePoints";

    public static void writeHomesToNBT(NBTTagCompound nbt, List<HomePoint> homes) {
        NBTTagList tagList = new NBTTagList();
        for (HomePoint hp : homes) {
            tagList.appendTag(hp.writeToNBT());
        }
        nbt.setTag(NBT_KEY, tagList);
    }

    public static List<HomePoint> readHomesFromNBT(NBTTagCompound nbt) {
        List<HomePoint> homes = new ArrayList<>();
        if (!nbt.hasKey(NBT_KEY)) return homes;
        NBTTagList tagList = nbt.getTagList(NBT_KEY);
        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
            homes.add(HomePoint.readFromNBT(tag));
        }
        return homes;
    }

    public static List<HomePoint> getHomes(EntityPlayer player) {
        String name = player.username;
        return homeMap.getOrDefault(name, new ArrayList<>());
    }
    public static void setHomes(EntityPlayer player, List<HomePoint> homes) {
        homeMap.put(player.username, homes);
    }
    public static void setHome(EntityPlayer player, String homeName, double x, double y, double z, int dim) {
        List<HomePoint> list = getHomes(player);
        list.removeIf(hp -> hp.name.equalsIgnoreCase(homeName));
        HomePoint hp = new HomePoint();
        hp.name = homeName;
        hp.x = x;
        hp.y = y;
        hp.z = z;
        hp.dim = dim;
        list.add(hp);
        setHomes(player, list);
    }
    public static boolean delHome(EntityPlayer player, String homeName) {
        List<HomePoint> list = getHomes(player);
        boolean removed = list.removeIf(hp -> hp.name.equalsIgnoreCase(homeName));
        setHomes(player, list);
        return removed;
    }
    public static HomePoint getHome(EntityPlayer player, String homeName) {
        return getHomes(player).stream().filter(hp -> hp.name.equalsIgnoreCase(homeName)).findFirst().orElse(null);
    }
}