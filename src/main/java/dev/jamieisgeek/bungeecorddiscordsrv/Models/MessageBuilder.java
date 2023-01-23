package dev.jamieisgeek.bungeecorddiscordsrv.Models;

public class MessageBuilder {
    private static String message;
    private static String username;
    public MessageBuilder(String username, String message) {
        MessageBuilder.username = username;
        MessageBuilder.message = message;
    }

    public static String MCToDiscord() {
        return "**" + username + "**: " + message;
    }

    public static String DiscordToMC() {
        return message;
    }
}
