package com.inf1nlty.sethome.command;

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
            player.sendChatToPlayer(ChatMessageComponent.createFromText("commands.delwarp.usage")
                    .setColor(EnumChatFormatting.RED));
            return;
        }
        String name = args[0];
        boolean ok = WarpManager.delWarp(name);

        if (ok) {
            String broadcastMsg = String.format("commands.warp.delete.broadcast|player=%s|name=%s", player.username, name);
            for (Object obj : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
                EntityPlayer target = (EntityPlayer) obj;
                target.sendChatToPlayer(ChatMessageComponent.createFromText(broadcastMsg)
                        .setColor(EnumChatFormatting.AQUA));
            }
        } else {
            player.sendChatToPlayer(ChatMessageComponent.createFromText("commands.warp.notfound|name=" + name)
                    .setColor(EnumChatFormatting.RED));
        }
    }
}