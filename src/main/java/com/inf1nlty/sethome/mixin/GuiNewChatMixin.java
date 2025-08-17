package com.inf1nlty.sethome.mixin;

import net.minecraft.src.GuiNewChat;
import net.minecraft.src.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GuiNewChat.class)
public class GuiNewChatMixin {
    @ModifyVariable(method = "drawChat", at = @At(value = "STORE", ordinal = 0), name = "var17")
    private String localizeHomeMessage(String var17) {
        if (var17 == null) return null;

        if (var17.matches("^\\|[a-zA-Z0-9_]+=.*")) {
            return "";
        }

        String[] myKeys = {
                "commands.home.usage",
                "commands.sharehome.usage",
                "commands.sethome.usage",
                "commands.delhome.usage",
                "commands.listhome.usage",
                "commands.home.set.success",
                "commands.home.tp.dim_mismatch",
                "commands.home.wait",
                "commands.home.tp.success",
                "commands.home.list.empty",
                "commands.home.list.success",
                "commands.home.delete.success",
                "commands.home.notfound",
                "commands.sharehome.share.line1",
                "commands.sharehome.share.line2",

                "commands.warp.usage",
                "commands.sharewarp.usage",
                "commands.setwarp.usage",
                "commands.delwarp.usage",
                "commands.warp.set.broadcast",
                "commands.warp.tp.dim_mismatch",
                "commands.warp.wait",
                "commands.warp.tp.success",
                "commands.warp.list.empty",
                "commands.warp.list.success",
                "commands.warp.delete.broadcast",
                "commands.warp.notfound",
                "commands.sharewarp.share.line1",
                "commands.sharewarp.share.line2",

                "commands.t.usage",
                "commands.t.request",
                "commands.t.request_sent",
                "commands.t.accepted",
                "commands.t.accepted_by",
                "commands.t.denied",
                "commands.t.denied_by",
                "commands.t.norequest",
                "commands.t.offline",
                "commands.t.dim_mismatch",
                "commands.t.wait",
                "commands.t.tp_success",
                "commands.t.tp_successed",
                "commands.t.self"
        };

        for (String keyPrefix : myKeys) {
            int idx = var17.indexOf(keyPrefix);
            if (idx != -1) {
                int pipeIdx = var17.indexOf("|", idx);
                String key = pipeIdx > -1 ? var17.substring(idx, pipeIdx) : var17.substring(idx);
                String localized = I18n.getString(key);

                if (pipeIdx > -1) {
                    String params = var17.substring(pipeIdx + 1);
                    String[] paramPairs = params.split("\\|");
                    for (String pair : paramPairs) {
                        String[] kv = pair.split("=", 2);
                        if (kv.length == 2) {
                            String paramKey = kv[0];
                            String paramValue = kv[1];
                            if (paramKey.equals("dim")) {
                                String dimKey = switch (paramValue) {
                                    case "0" -> "dimension.overworld";
                                    case "-1" -> "dimension.nether";
                                    case "1" -> "dimension.end";
                                    default -> "dimension.unknown";
                                };
                                paramValue = I18n.getString(dimKey);
                            }
                            localized = localized.replace("{" + paramKey + "}", paramValue);
                        }
                    }
                    localized = localized.replaceAll("\\{[a-zA-Z0-9_]+}", "");
                }

                localized = replaceDimInList(localized);

                String prefix = var17.substring(0, idx);
                return prefix + localized;
            }
        }
        return replaceDimInList(var17);
    }

    @Unique
    private String replaceDimInList(String input) {
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\{dim=(-?\\d+)}");
        java.util.regex.Matcher m = p.matcher(input);
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            String dimValue = m.group(1);
            String dimKey = switch (dimValue) {
                case "0" -> "dimension.overworld";
                case "-1" -> "dimension.nether";
                case "1" -> "dimension.end";
                default -> "dimension.unknown";
            };
            m.appendReplacement(sb, I18n.getString(dimKey));
        }
        m.appendTail(sb);
        return sb.toString();
    }
}