package com.inf1nlty.sethome;

import net.minecraft.src.NBTTagCompound;

public class WarpPoint {
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

    public static WarpPoint readFromNBT(NBTTagCompound tag) {
        WarpPoint wp = new WarpPoint();
        wp.name = tag.getString("Name");
        wp.x = tag.getDouble("X");
        wp.y = tag.getDouble("Y");
        wp.z = tag.getDouble("Z");
        wp.dim = tag.getInteger("Dim");
        return wp;
    }
}