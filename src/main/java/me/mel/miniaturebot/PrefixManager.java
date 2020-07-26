package me.mel.miniaturebot;

import java.util.HashMap;

public class PrefixManager {
    private static final HashMap<String, String> prefixes = new HashMap<String, String>();

    public static String getPrefixForGuild(String guildId) {
        return prefixes.getOrDefault(guildId, Config.get("DEFAULT_PREFIX"));
    }

    public static void setPrefixForGuild(String guildId, String newPrefix) {
        prefixes.put(guildId, newPrefix);
    }
}
