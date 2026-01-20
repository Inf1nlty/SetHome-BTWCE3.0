package com.inf1nlty.sethome.command;

import com.inf1nlty.sethome.HomePoint;
import com.inf1nlty.sethome.util.BackManager;
import com.inf1nlty.sethome.util.ChatUtil;
import com.inf1nlty.sethome.util.HomeManager;
import net.minecraft.src.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HomeCommand extends CommandBase {

    private static final Map<String, PendingTeleport> pendingTeleports = new HashMap<>();

    private static class PendingTeleport {
        public final EntityPlayer player;
        public final HomePoint home;
        public int ticksLeft;
        public final String name;
        public PendingTeleport(EntityPlayer player, HomePoint home, String name, int ticksLeft) {
            this.player = player;
            this.home = home;
            this.name = name;
            this.ticksLeft = ticksLeft;
        }
    }

    @Override
    public String getCommandName() {
        return "home";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/home [name]";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer player)) return;
        String name = args.length >= 1 ? args[0] : "default";
        HomePoint hp = HomeManager.getHome(player, name);
        if (hp != null) {
            if (hp.dim != player.dimension) {
                player.sendChatToPlayer(ChatUtil.trans("commands.home.tp.dim_mismatch", EnumChatFormatting.RED, name));
                return;
            }
            BackManager.setBack(player, player.posX, player.posY, player.posZ, player.dimension);

            pendingTeleports.put(player.username, new PendingTeleport(player, hp, name, 100));
            player.sendChatToPlayer(ChatUtil.trans("commands.home.wait", EnumChatFormatting.YELLOW, name));
        } else {
            player.sendChatToPlayer(ChatUtil.trans("commands.home.notfound", EnumChatFormatting.RED, name));
        }
    }

    public static void onServerTick() {
        Iterator<Map.Entry<String, PendingTeleport>> it = pendingTeleports.entrySet().iterator();
        while (it.hasNext()) {
            PendingTeleport pt = it.next().getValue();
            pt.ticksLeft--;
            if (pt.ticksLeft <= 0) {
                pt.player.setPositionAndUpdate(pt.home.x, pt.home.y, pt.home.z);
                pt.player.sendChatToPlayer(ChatUtil.trans("commands.home.tp.success", EnumChatFormatting.GREEN, pt.name));
                it.remove();
            }
        }
    }
}