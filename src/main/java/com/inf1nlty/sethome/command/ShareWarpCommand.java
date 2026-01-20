package com.inf1nlty.sethome.command;

import com.inf1nlty.sethome.WarpPoint;
import com.inf1nlty.sethome.util.ChatUtil;
import com.inf1nlty.sethome.util.WarpManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ShareWarpCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "sharewarp";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/sharewarp [warpName]";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer)) return Collections.emptyList();
        if (args.length == 1) {
            List<WarpPoint> warps = WarpManager.getWarps();
            return warps.stream().map(wp -> wp.name).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayerMP player)) return;

        if (args.length < 1) {
            player.sendChatToPlayer(ChatUtil.trans("commands.sharewarp.usage", EnumChatFormatting.RED));
            return;
        }

        String name = args[0];
        WarpPoint wp = WarpManager.getWarp(name);
        if (wp == null) {
            player.sendChatToPlayer(ChatUtil.trans("commands.warp.notfound", EnumChatFormatting.RED, name));
            return;
        }

        for (Object obj : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
            EntityPlayerMP target = (EntityPlayerMP) obj;
            target.sendChatToPlayer(ChatUtil.trans("commands.sharewarp.share.line1", EnumChatFormatting.AQUA, player.username, name));
            target.sendChatToPlayer(ChatUtil.trans("commands.sharewarp.share.line2", EnumChatFormatting.AQUA, String.format("%.3f", wp.x), String.format("%.3f", wp.y), String.format("%.3f", wp.z), Integer.toString(wp.dim)));
        }
    }
}