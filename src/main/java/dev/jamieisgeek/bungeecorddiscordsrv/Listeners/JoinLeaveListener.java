package dev.jamieisgeek.bungeecorddiscordsrv.Listeners;

import dev.jamieisgeek.bungeecorddiscordsrv.Managers.DiscordManager;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinLeaveListener implements Listener {
    @EventHandler
    public void onJoin(ServerSwitchEvent event) {
        String username = event.getPlayer().getName();
        String serverName = event.getPlayer().getServer().getInfo().getName();
        DiscordManager.getDiscordManager().sendConnectionEmbed(username, serverName, true);
    }

    @EventHandler
    public void onLeave(PlayerDisconnectEvent event) {
        String username = event.getPlayer().getName();
        String serverName = event.getPlayer().getServer().getInfo().getName();
        DiscordManager.getDiscordManager().sendConnectionEmbed(username, serverName, false);
    }
}
