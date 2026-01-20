package com.inf1nlty.sethome.command;

import com.inf1nlty.sethome.util.BackManager;
import com.inf1nlty.sethome.util.ChatUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

import java.util.*;

public class TPACommand extends CommandBase {

    private static final Map<String, String> pendingRequests = new HashMap<>();

    private static final List<PendingTeleport> pendingTeleports = new ArrayList<>();

    private static class PendingTeleport {
        public final EntityPlayerMP from;
        public final EntityPlayerMP to;
        public int ticksLeft;

        public PendingTeleport(EntityPlayerMP from, EntityPlayerMP to, int ticksLeft) {
            this.from = from;
            this.to = to;
            this.ticksLeft = ticksLeft;
        }
    }

    @Override
    public String getCommandName() { return "t"; }

    @Override
    public int getRequiredPermissionLevel() { return 0; }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/t <player> | /t yes | /t no";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) { return true; }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayerMP player)) return Collections.emptyList();
        if (args.length == 1) {
            String sub = args[0].toLowerCase();
            List<String> names = new ArrayList<>();
            for (Object obj : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
                EntityPlayerMP target = (EntityPlayerMP) obj;
                if (!target.username.equalsIgnoreCase(player.username) &&
                        target.username.toLowerCase().startsWith(sub)) {
                    names.add(target.username);
                }
            }
            if ("yes".startsWith(sub)) names.add("yes");
            if ("no".startsWith(sub)) names.add("no");
            return names;
        }
        return Collections.emptyList();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayerMP player)) return;
        if (args.length < 1) {
            player.sendChatToPlayer(ChatUtil.trans("commands.t.usage", EnumChatFormatting.YELLOW));
            return;
        }

        String arg = args[0].toLowerCase();

        if (arg.equals("yes")) {
            String requesterName = pendingRequests.remove(player.username);
            if (requesterName == null) {
                player.sendChatToPlayer(ChatUtil.trans("commands.t.norequest", EnumChatFormatting.RED));
                return;
            }
            EntityPlayerMP requester = getOnlinePlayer(requesterName);
            if (requester == null) {
                player.sendChatToPlayer(ChatUtil.trans("commands.t.offline", EnumChatFormatting.RED, requesterName));
                return;
            }
            if (player.dimension != requester.dimension) {
                player.sendChatToPlayer(ChatUtil.trans("commands.t.dim_mismatch", EnumChatFormatting.RED, requesterName));
                requester.sendChatToPlayer(ChatUtil.trans("commands.t.dim_mismatch", EnumChatFormatting.RED, player.username));
                return;
            }
            player.sendChatToPlayer(ChatUtil.trans("commands.t.accepted", EnumChatFormatting.GREEN, requester.username));
            requester.sendChatToPlayer(ChatUtil.trans("commands.t.accepted_by", EnumChatFormatting.GREEN, player.username));

            BackManager.setBack(player, player.posX, player.posY, player.posZ, player.dimension);

            pendingTeleports.add(new PendingTeleport(requester, player, 100));
            requester.sendChatToPlayer(ChatUtil.trans("commands.t.wait", EnumChatFormatting.YELLOW, player.username));
            return;
        }

        if (arg.equals("no")) {
            String requesterName = pendingRequests.remove(player.username);
            if (requesterName == null) {
                player.sendChatToPlayer(ChatUtil.trans("commands.t.norequest", EnumChatFormatting.RED));
                return;
            }
            EntityPlayerMP requester = getOnlinePlayer(requesterName);
            if (requester != null) {
                requester.sendChatToPlayer(ChatUtil.trans("commands.t.denied_by", EnumChatFormatting.RED, player.username));
            }
            player.sendChatToPlayer(ChatUtil.trans("commands.t.denied", EnumChatFormatting.RED, requesterName));
            return;
        }

        if (arg.equalsIgnoreCase(player.username)) {
            player.sendChatToPlayer(ChatUtil.trans("commands.t.self", EnumChatFormatting.RED));
            return;
        }
        EntityPlayerMP target = getOnlinePlayer(arg);
        if (target == null) {
            player.sendChatToPlayer(ChatUtil.trans("commands.t.offline", EnumChatFormatting.RED, arg));
            return;
        }
        if (player.dimension != target.dimension) {
            player.sendChatToPlayer(ChatUtil.trans("commands.t.dim_mismatch", EnumChatFormatting.RED, target.username));
            return;
        }
        pendingRequests.put(target.username, player.username);
        player.sendChatToPlayer(ChatUtil.trans("commands.t.request_sent", EnumChatFormatting.AQUA, target.username));
        target.sendChatToPlayer(ChatUtil.trans("commands.t.request", EnumChatFormatting.AQUA, player.username));
    }

    public static void onServerTick() {
        Iterator<PendingTeleport> it = pendingTeleports.iterator();
        while (it.hasNext()) {
            PendingTeleport pt = it.next();
            pt.ticksLeft--;
            if (pt.ticksLeft <= 0) {
                pt.from.setPositionAndUpdate(pt.to.posX, pt.to.posY, pt.to.posZ);
                pt.from.sendChatToPlayer(ChatUtil.trans("commands.t.tp_success", EnumChatFormatting.GREEN, pt.to.username));
                pt.to.sendChatToPlayer(ChatUtil.trans("commands.t.tp_successed", EnumChatFormatting.GREEN, pt.from.username));
                it.remove();
            }
        }
    }

    private EntityPlayerMP getOnlinePlayer(String name) {
        for (Object obj : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
            EntityPlayerMP player = (EntityPlayerMP) obj;
            if (player.username.equalsIgnoreCase(name)) return player;
        }
        return null;
    }
}