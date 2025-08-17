package com.inf1nlty.sethome.command;

import com.inf1nlty.sethome.WarpPoint;
import com.inf1nlty.sethome.util.WarpManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

public class SetWarpCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "setwarp";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/setwarp [name]";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer player)) return;
        if (args.length < 1) {
            player.sendChatToPlayer(ChatMessageComponent.createFromText("commands.setwarp.usage")
                    .setColor(EnumChatFormatting.RED));
            return;
        }
        String name = args[0];
        WarpPoint wp = new WarpPoint();
        wp.name = name;
        wp.x = player.posX;
        wp.y = player.posY;
        wp.z = player.posZ;
        wp.dim = player.dimension;
        WarpManager.addWarp(wp);

        String broadcastMsg = String.format("commands.warp.set.broadcast|player=%s|name=%s", player.username, name);
        for (Object obj : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
            EntityPlayer target = (EntityPlayer) obj;
            target.sendChatToPlayer(ChatMessageComponent.createFromText(broadcastMsg)
                    .setColor(EnumChatFormatting.AQUA));
        }
    }
}