package com.inf1nlty.sethome.util;

import net.minecraft.src.ChatMessageComponent;
import net.minecraft.src.EnumChatFormatting;

public class ChatUtil {

    public static ChatMessageComponent trans(String key, EnumChatFormatting color, Object... args) {
        ChatMessageComponent msg = new ChatMessageComponent();
        msg.addFormatted(key, args);
        if (color != null) msg.setColor(color);
        return msg;
    }
}