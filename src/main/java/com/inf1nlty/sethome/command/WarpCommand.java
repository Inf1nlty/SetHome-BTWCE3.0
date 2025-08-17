package com.inf1nlty.sethome.command;

import com.inf1nlty.sethome.WarpPoint;
import com.inf1nlty.sethome.util.WarpManager;
import net.minecraft.src.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WarpCommand extends CommandBase {

    private static final Map<String, PendingTeleport> pendingTeleports = new HashMap<>();

    private static class PendingTeleport {
        public final EntityPlayer player;
        public final WarpPoint warp;
        public int ticksLeft;
        public final String name;
        public PendingTeleport(EntityPlayer player, WarpPoint warp, String name, int ticksLeft) {
            this.player = player;
            this.warp = warp;
            this.name = name;
            this.ticksLeft = ticksLeft;
        }
    }

    @Override
    public String getCommandName() {
        return "warp";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/warp [name]";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer player)) return;
        if (args.length < 1) {
            player.sendChatToPlayer(ChatMessageComponent.createFromText("commands.warp.usage")
                    .setColor(EnumChatFormatting.RED));
            return;
        }
        String name = args[0];
        WarpPoint wp = WarpManager.getWarp(name);
        if (wp != null) {
            if (wp.dim != player.dimension) {
                player.sendChatToPlayer(ChatMessageComponent.createFromText("commands.warp.tp.dim_mismatch|name=" + name)
                        .setColor(EnumChatFormatting.RED));
                return;
            }
            pendingTeleports.put(player.username, new PendingTeleport(player, wp, name, 100));
            player.sendChatToPlayer(ChatMessageComponent.createFromText("commands.warp.wait|name=" + name)
                    .setColor(EnumChatFormatting.YELLOW));
        } else {
            player.sendChatToPlayer(ChatMessageComponent.createFromText("commands.warp.notfound|name=" + name)
                    .setColor(EnumChatFormatting.RED));
        }
    }

    public static void onServerTick() {
        Iterator<Map.Entry<String, PendingTeleport>> it = pendingTeleports.entrySet().iterator();
        while (it.hasNext()) {
            PendingTeleport pt = it.next().getValue();
            pt.ticksLeft--;
            if (pt.ticksLeft <= 0) {
                pt.player.setPositionAndUpdate(pt.warp.x, pt.warp.y, pt.warp.z);
                pt.player.sendChatToPlayer(ChatMessageComponent.createFromText("commands.warp.tp.success|name=" + pt.name)
                        .setColor(EnumChatFormatting.GREEN));
                it.remove();
            }
        }
    }
}