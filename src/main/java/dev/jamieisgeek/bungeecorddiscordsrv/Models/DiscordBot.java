package dev.jamieisgeek.bungeecorddiscordsrv.Models;

import dev.jamieisgeek.bungeecorddiscordsrv.BungeecordDiscordSRV;
import dev.jamieisgeek.bungeecorddiscordsrv.Managers.DiscordManager;
import dev.jamieisgeek.bungeecorddiscordsrv.Managers.ProxyManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;

public final class DiscordBot implements EventListener {
    private final String token;
    private final DiscordManager discordManager;
    private static DiscordBot instance;
    private final JDA BOT;
    private final String categoryID;
    private final ProxyServer proxy;
    private ArrayList<TextChannel> channels = new ArrayList<>();

    public DiscordBot(String token, String categoryID, ProxyServer proxy) throws LoginException, InterruptedException {
        instance = this;
        this.token = token;
        this.categoryID = categoryID;
        this.proxy = proxy;

        BOT = JDABuilder.createLight(token)
                .enableIntents(
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_MEMBERS
                )
                .disableIntents(
                        GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                        GatewayIntent.GUILD_BANS,
                        GatewayIntent.DIRECT_MESSAGES,
                        GatewayIntent.DIRECT_MESSAGE_TYPING
                )
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setBulkDeleteSplittingEnabled(false)
                .addEventListeners(this)
                .build().awaitReady();

        this.setupChannels();
        this.discordManager = new DiscordManager();
    }
    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if(event instanceof ReadyEvent) {
            proxy.getLogger().info("Discord Bot is ready!");
        } else if (event instanceof GuildMessageReceivedEvent) {
            GuildMessageReceivedEvent guildMessageReceivedEvent = (GuildMessageReceivedEvent) event;
            if(guildMessageReceivedEvent.getChannel().getParent().getId().equals(categoryID)) {
                ProxyManager.sendChannelMessage(guildMessageReceivedEvent.getMessage().getContentRaw(), guildMessageReceivedEvent.getAuthor().getName(), guildMessageReceivedEvent.getChannel().getName());
            }
        }
    }

    private void setupChannels() {
        proxy.getLogger().info(proxy.getServers().toString());
        proxy.getServers().forEach((server, serverInfo) -> {
            String serverName = serverInfo.getName();
            if (!BOT.getCategoryById(categoryID).getTextChannels().contains(BOT.getTextChannelsByName(serverName.toLowerCase(), true))) {
                BOT.getCategoryById(categoryID).createTextChannel(serverName).queue();
                BOT.getTextChannels().stream().filter(textChannel -> textChannel.getName().equals(serverName)).forEach(channels::add);
                proxy.getLogger().info("Created channel for " + serverName);
                return;
            }

            BOT.getTextChannels().stream().filter(textChannel -> textChannel.getName().equals(serverName)).forEach(channel -> {
                if(!channels.contains(channel)) {
                    channels.add(channel);
                }
            });
        });
    }

    public void shutdown() {
        if(this.BOT == null) return;
        BOT.shutdown();
    }

    public static DiscordBot getInstance() {
        return instance;
    }

    public static JDA getBot() {
        return instance.BOT;
    }

    public static ArrayList<TextChannel> getChannels() {
        return instance.channels;
    }
}
