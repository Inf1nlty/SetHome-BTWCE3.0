package com.inf1nlty.sethome.command;

import com.inf1nlty.sethome.HomePoint;
import com.inf1nlty.sethome.util.ChatUtil;
import com.inf1nlty.sethome.util.HomeManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

public class ShareHomeCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "sharehome";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/sharehome [homeName]";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public java.util.List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer player)) return java.util.Collections.emptyList();
        if (args.length == 1) {
            java.util.List<HomePoint> homes = HomeManager.getHomes(player);
            return homes.stream().map(hp -> hp.name).toList();
        }
        return java.util.Collections.emptyList();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayerMP player)) return;

        String name = args.length < 1 ? "default" : args[0];
        HomePoint hp = HomeManager.getHome(player, name);
        if (hp == null) {
            player.sendChatToPlayer(ChatUtil.trans("commands.home.notfound", EnumChatFormatting.RED, name));
            return;
        }

        for (Object obj : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
            EntityPlayerMP target = (EntityPlayerMP) obj;
            target.sendChatToPlayer(ChatUtil.trans("commands.sharehome.share.line1", EnumChatFormatting.AQUA, player.username, name));
            target.sendChatToPlayer(ChatUtil.trans("commands.sharehome.share.line2", EnumChatFormatting.AQUA, String.format("%.3f", hp.x), String.format("%.3f", hp.y), String.format("%.3f", hp.z), Integer.toString(hp.dim)));
        }
    }
}