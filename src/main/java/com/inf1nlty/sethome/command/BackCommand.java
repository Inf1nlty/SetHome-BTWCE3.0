package com.inf1nlty.sethome.command;

import com.inf1nlty.sethome.BackPoint;
import com.inf1nlty.sethome.util.BackManager;
import com.inf1nlty.sethome.util.ChatUtil;
import net.minecraft.src.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BackCommand extends CommandBase {
    private static final List<PendingTeleport> pendingTeleports = new ArrayList<>();

    private static class PendingTeleport {
        public final EntityPlayer player;
        public final BackPoint backPoint;
        public int ticksLeft;

        public PendingTeleport(EntityPlayer player, BackPoint backPoint, int ticksLeft) {
            this.player = player;
            this.backPoint = backPoint;
            this.ticksLeft = ticksLeft;
        }
    }

    @Override
    public String getCommandName() {
        return "back";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/back";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return sender instanceof EntityPlayer;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer player)) return;

        BackPoint bp = BackManager.getBack(player);
        if (bp == null) {
            player.sendChatToPlayer(ChatUtil.trans("commands.back.noentry", EnumChatFormatting.RED));
            return;
        }

        if (bp.dim != player.dimension) {
            player.sendChatToPlayer(ChatUtil.trans("commands.back.dim_mismatch", EnumChatFormatting.RED));
            return;
        }

        pendingTeleports.add(new PendingTeleport(player, bp, 60));

        player.sendChatToPlayer(ChatUtil.trans("commands.back.wait", EnumChatFormatting.YELLOW));
    }

    public static void onServerTick() {
        Iterator<PendingTeleport> it = pendingTeleports.iterator();

        while (it.hasNext()) {
            PendingTeleport pt = it.next();
            pt.ticksLeft--;

            if (pt.ticksLeft <= 0) {

                if (pt.player != null) {
                    if (pt.player.dimension == pt.backPoint.dim) {
                        pt.player.setPositionAndUpdate(pt.backPoint.x, pt.backPoint.y, pt.backPoint.z);
                        pt.player.sendChatToPlayer(ChatUtil.trans("commands.back.success", EnumChatFormatting.GREEN));
                    } else {
                        pt.player.sendChatToPlayer(ChatUtil.trans("commands.back.dim_mismatch", EnumChatFormatting.RED));
                    }
                }
                it.remove();
            }
        }
    }
}