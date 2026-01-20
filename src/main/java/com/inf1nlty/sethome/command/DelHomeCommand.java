package com.inf1nlty.sethome.command;

import com.inf1nlty.sethome.HomePoint;
import com.inf1nlty.sethome.util.ChatUtil;
import com.inf1nlty.sethome.util.HomeManager;
import net.minecraft.src.*;

public class DelHomeCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "delhome";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/delhome [name]";
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
        if (!(sender instanceof EntityPlayer player)) return;
        String name = args.length >= 1 ? args[0] : "default";
        boolean ok = HomeManager.delHome(player, name);
        if (ok) {
            ChatMessageComponent msg = ChatUtil.trans("commands.home.delete.success", EnumChatFormatting.GREEN, name);
            player.sendChatToPlayer(msg);
        } else {
            ChatMessageComponent msg = ChatUtil.trans("commands.home.notfound", EnumChatFormatting.RED, name);
            player.sendChatToPlayer(msg);
        }
    }
}