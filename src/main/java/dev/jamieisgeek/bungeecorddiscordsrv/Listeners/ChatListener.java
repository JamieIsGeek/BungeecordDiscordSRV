package dev.jamieisgeek.bungeecorddiscordsrv.Listeners;

import dev.jamieisgeek.bungeecorddiscordsrv.Managers.DiscordManager;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChatListener implements Listener {
    @EventHandler
    public void onChat(ChatEvent event) {
        if (event.isCommand()) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }

        String sender = ((net.md_5.bungee.api.connection.ProxiedPlayer) event.getSender()).getName();
        String serverName = ((net.md_5.bungee.api.connection.ProxiedPlayer) event.getSender()).getServer().getInfo().getName();

        DiscordManager.getDiscordManager().sendChannelMessage(event.getMessage(), sender, serverName);
    }
}
