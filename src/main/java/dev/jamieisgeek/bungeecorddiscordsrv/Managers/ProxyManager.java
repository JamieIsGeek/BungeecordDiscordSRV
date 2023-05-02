package dev.jamieisgeek.bungeecorddiscordsrv.Managers;

import dev.jamieisgeek.bungeecorddiscordsrv.Models.MessageBuilder;
import net.md_5.bungee.api.ProxyServer;

public class ProxyManager {
    private static ProxyServer proxy;
    public ProxyManager(ProxyServer proxy) {
        ProxyManager.proxy = proxy;
    }
    public static void sendChannelMessage(String message, String username, String serverName) {
        if(proxy.getServers().containsKey(serverName)) {
            proxy.getServers().get(serverName).getPlayers().forEach(player -> player.sendMessage(new MessageBuilder(username, message).DiscordToMC()));
        }
    }
}
