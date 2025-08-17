package com.inf1nlty.sethome.command;

import com.inf1nlty.sethome.util.HomeManager;
import com.inf1nlty.sethome.HomePoint;
import net.minecraft.src.*;

import java.util.List;
import java.util.stream.Collectors;

public class ListHomeCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "listhome";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/listhome";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer player)) return;
        List<HomePoint> list = HomeManager.getHomes(player);
        if (list.isEmpty()) {
            player.sendChatToPlayer(ChatMessageComponent.createFromText("commands.home.list.empty")
                    .setColor(EnumChatFormatting.RED));
        } else {
            String names = list.stream()
                    .map(hp -> String.format("%s (%.1f, %.1f, %.1f, {dim=%d})", hp.name, hp.x, hp.y, hp.z, hp.dim))
                    .collect(Collectors.joining(", "));
            player.sendChatToPlayer(ChatMessageComponent.createFromText("commands.home.list.success|list=" + names)
                    .setColor(EnumChatFormatting.GREEN));
        }
    }
}