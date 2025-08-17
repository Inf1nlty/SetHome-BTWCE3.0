package com.inf1nlty.sethome;

import net.minecraft.src.NBTTagCompound;

public class HomePoint {
    public String name;
    public double x, y, z;
    public int dim;

    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Name", name);
        tag.setDouble("X", x);
        tag.setDouble("Y", y);
        tag.setDouble("Z", z);
        tag.setInteger("Dim", dim);
        return tag;
    }

    public static HomePoint readFromNBT(NBTTagCompound tag) {
        HomePoint hp = new HomePoint();
        hp.name = tag.getString("Name");
        hp.x = tag.getDouble("X");
        hp.y = tag.getDouble("Y");
        hp.z = tag.getDouble("Z");
        hp.dim = tag.getInteger("Dim");
        return hp;
    }
}