package com.inf1nlty.sethome.command;

import com.inf1nlty.sethome.HomePoint;
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
            player.sendChatToPlayer(
                    ChatMessageComponent.createFromText("commands.home.notfound|name=" + name)
                            .setColor(EnumChatFormatting.RED)
            );
            return;
        }

        String line1 = String.format(
                "commands.sharehome.share.line1|player=%s|name=%s", player.username, name
        );
        String line2 = String.format(
                "commands.sharehome.share.line2|x=%.3f|y=%.3f|z=%.3f|dim=%d", hp.x, hp.y, hp.z, hp.dim
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