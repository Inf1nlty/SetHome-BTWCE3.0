package com.inf1nlty.sethome.command;

import com.inf1nlty.sethome.WarpPoint;
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
            player.sendChatToPlayer(
                    ChatMessageComponent.createFromText("commands.sharewarp.usage")
                            .setColor(EnumChatFormatting.RED)
            );
            return;
        }

        String name = args[0];
        WarpPoint wp = WarpManager.getWarp(name);
        if (wp == null) {
            player.sendChatToPlayer(
                    ChatMessageComponent.createFromText("commands.warp.notfound|name=" + name)
                            .setColor(EnumChatFormatting.RED)
            );
            return;
        }

        String line1 = String.format(
                "commands.sharewarp.share.line1|player=%s|name=%s", player.username, name
        );
        String line2 = String.format(
                "commands.sharewarp.share.line2|x=%.3f|y=%.3f|z=%.3f|dim=%d", wp.x, wp.y, wp.z, wp.dim
        );

        for (Object obj : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
            EntityPlayerMP target = (EntityPlayerMP) obj;
            target.sendChatToPlayer(
                    ChatMessageComponent.createFromText(line1)
                            .setColor(EnumChatFormatting.AQUA)
            );
            target.sendChatToPlayer(
                    ChatMessageComponent.createFromText(line2)
                            .setColor(EnumChatFormatting.AQUA)
            );
        }
    }
}