package com.inf1nlty.sethome.command;

import com.inf1nlty.sethome.util.WarpManager;
import com.inf1nlty.sethome.WarpPoint;
import net.minecraft.src.*;

import java.util.List;
import java.util.stream.Collectors;

public class ListWarpCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "listwarp";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/listwarp";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer player)) return;
        List<WarpPoint> list = WarpManager.getWarps();
        if (list.isEmpty()) {
            player.sendChatToPlayer(ChatMessageComponent.createFromText("commands.warp.list.empty")
                    .setColor(EnumChatFormatting.RED));
        } else {
            String names = list.stream()
                    .map(wp -> String.format("%s (%.1f, %.1f, %.1f, {dim=%d})", wp.name, wp.x, wp.y, wp.z, wp.dim))
                    .collect(Collectors.joining(", "));
            player.sendChatToPlayer(ChatMessageComponent.createFromText("commands.warp.list.success|list=" + names)
                    .setColor(EnumChatFormatting.GREEN));
        }
    }
}