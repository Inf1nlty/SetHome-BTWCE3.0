package com.inf1nlty.sethome;

import net.minecraft.src.NBTTagCompound;

public class BackPoint {
    public double x, y, z;
    public int dim;
    public long time;

    public BackPoint() {}

    public BackPoint(double x, double y, double z, int dim, long time) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dim = dim;
        this.time = time;
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setDouble("X", x);
        tag.setDouble("Y", y);
        tag.setDouble("Z", z);
        tag.setInteger("Dim", dim);
        tag.setLong("Time", time);
        return tag;
    }

    public static BackPoint readFromNBT(NBTTagCompound tag) {
        BackPoint bp = new BackPoint();
        bp.x = tag.getDouble("X");
        bp.y = tag.getDouble("Y");
        bp.z = tag.getDouble("Z");
        bp.dim = tag.getInteger("Dim");
        if (tag.hasKey("Time")) bp.time = tag.getLong("Time");
        else bp.time = 0L;
        return bp;
    }
}