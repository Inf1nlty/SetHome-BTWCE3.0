package com.inf1nlty.sethome.command;

import com.inf1nlty.sethome.util.ChatUtil;
import com.inf1nlty.sethome.util.WarpManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

public class DelWarpCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "delwarp";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/delwarp [name]";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer player)) return;
        if (args.length < 1) {
            player.sendChatToPlayer(ChatUtil.trans("commands.delwarp.usage", EnumChatFormatting.RED));
            return;
        }
        String name = args[0];
        boolean ok = WarpManager.delWarp(name);

        if (ok) {
            for (Object obj : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
                EntityPlayer target = (EntityPlayer) obj;
                target.sendChatToPlayer(ChatUtil.trans("commands.warp.delete.broadcast", EnumChatFormatting.AQUA, player.username, name));
            }
        } else {
            player.sendChatToPlayer(ChatUtil.trans("commands.warp.notfound", EnumChatFormatting.RED, name));
        }
    }
}