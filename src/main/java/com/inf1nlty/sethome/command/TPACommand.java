package com.inf1nlty.sethome.command;

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
            player.sendChatToPlayer(ChatMessageComponent.createFromText("commands.t.usage").setColor(EnumChatFormatting.YELLOW));
            return;
        }

        String arg = args[0].toLowerCase();

        if (arg.equals("yes")) {
            String requesterName = pendingRequests.remove(player.username);
            if (requesterName == null) {
                player.sendChatToPlayer(ChatMessageComponent.createFromText("commands.t.norequest").setColor(EnumChatFormatting.RED));
                return;
            }
            EntityPlayerMP requester = getOnlinePlayer(requesterName);
            if (requester == null) {
                player.sendChatToPlayer(ChatMessageComponent.createFromText("commands.t.offline|name=" + requesterName).setColor(EnumChatFormatting.RED));
                return;
            }
            if (player.dimension != requester.dimension) {
                player.sendChatToPlayer(ChatMessageComponent.createFromText("commands.t.dim_mismatch|name=" + requesterName).setColor(EnumChatFormatting.RED));
                requester.sendChatToPlayer(ChatMessageComponent.createFromText("commands.t.dim_mismatch|name=" + player.username).setColor(EnumChatFormatting.RED));
                return;
            }
            player.sendChatToPlayer(ChatMessageComponent.createFromText("commands.t.accepted|name=" + requester.username).setColor(EnumChatFormatting.GREEN));
            requester.sendChatToPlayer(ChatMessageComponent.createFromText("commands.t.accepted_by|name=" + player.username).setColor(EnumChatFormatting.GREEN));

            pendingTeleports.add(new PendingTeleport(requester, player, 100));
            requester.sendChatToPlayer(ChatMessageComponent.createFromText("commands.t.wait|name=" + player.username).setColor(EnumChatFormatting.YELLOW));
            return;
        }

        if (arg.equals("no")) {
            String requesterName = pendingRequests.remove(player.username);
            if (requesterName == null) {
                player.sendChatToPlayer(ChatMessageComponent.createFromText("commands.t.norequest").setColor(EnumChatFormatting.RED));
                return;
            }
            EntityPlayerMP requester = getOnlinePlayer(requesterName);
            if (requester != null) {
                requester.sendChatToPlayer(ChatMessageComponent.createFromText("commands.t.denied_by|name=" + player.username).setColor(EnumChatFormatting.RED));
            }
            player.sendChatToPlayer(ChatMessageComponent.createFromText("commands.t.denied|name=" + requesterName).setColor(EnumChatFormatting.RED));
            return;
        }

        if (arg.equalsIgnoreCase(player.username)) {
            player.sendChatToPlayer(ChatMessageComponent.createFromText("commands.t.self").setColor(EnumChatFormatting.RED));
            return;
        }
        EntityPlayerMP target = getOnlinePlayer(arg);
        if (target == null) {
            player.sendChatToPlayer(ChatMessageComponent.createFromText("commands.t.offline|name=" + arg).setColor(EnumChatFormatting.RED));
            return;
        }
        if (player.dimension != target.dimension) {
            player.sendChatToPlayer(ChatMessageComponent.createFromText("commands.t.dim_mismatch|name=" + target.username).setColor(EnumChatFormatting.RED));
            return;
        }
        pendingRequests.put(target.username, player.username);
        player.sendChatToPlayer(ChatMessageComponent.createFromText("commands.t.request_sent|name=" + target.username).setColor(EnumChatFormatting.AQUA));
        target.sendChatToPlayer(ChatMessageComponent.createFromText("commands.t.request|name=" + player.username).setColor(EnumChatFormatting.AQUA));
    }

    public static void onServerTick() {
        Iterator<PendingTeleport> it = pendingTeleports.iterator();
        while (it.hasNext()) {
            PendingTeleport pt = it.next();
            pt.ticksLeft--;
            if (pt.ticksLeft <= 0) {
                pt.from.setPositionAndUpdate(pt.to.posX, pt.to.posY, pt.to.posZ);
                pt.from.sendChatToPlayer(ChatMessageComponent.createFromText("commands.t.tp_success|name=" + pt.to.username).setColor(EnumChatFormatting.GREEN));
                pt.to.sendChatToPlayer(ChatMessageComponent.createFromText("commands.t.tp_successed|name=" + pt.from.username).setColor(EnumChatFormatting.GREEN));
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