package dev.jamieisgeek.bungeecorddiscordsrv;

import dev.jamieisgeek.bungeecorddiscordsrv.Commands.ReloadCommand;
import dev.jamieisgeek.bungeecorddiscordsrv.Listeners.ChatListener;
import dev.jamieisgeek.bungeecorddiscordsrv.Listeners.JoinLeaveListener;
import dev.jamieisgeek.bungeecorddiscordsrv.Managers.DiscordManager;
import dev.jamieisgeek.bungeecorddiscordsrv.Managers.ProxyManager;
import dev.jamieisgeek.bungeecorddiscordsrv.Models.DiscordBot;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class BungeecordDiscordSRV extends Plugin {
    private Configuration configuration = null;
    private static BungeecordDiscordSRV instance;

    @Override
    public void onEnable() {
        instance = this;
        String[] items;
        try {
            items = this.getConfigItems();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        new ProxyManager(this.getProxy());

        try {
            new DiscordBot(items[0], configuration, items[1], getProxy());
        } catch (LoginException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.getProxy().getPluginManager().registerListener(this, new ChatListener());
        this.getProxy().getPluginManager().registerListener(this, new JoinLeaveListener());
        this.getProxy().getPluginManager().registerCommand(this, new ReloadCommand());

        getLogger().info("BungeecordDiscordSRV has been enabled!");
    }

    @Override
    public void onDisable() {
        DiscordManager.getDiscordManager().sendStatusMessage(false);
        DiscordBot.getInstance().shutdown();
        getLogger().info("BungeecordDiscordSRV has been disabled!");
    }

    public void setupConfig() throws IOException {
        if (!getDataFolder().exists()) {
            getLogger().info("Created config folder: " + getDataFolder().mkdir());
        }

        File configFile = new File(getDataFolder(), "config.yml");

        // Copy default config if it doesn't exist
        if (!configFile.exists()) {
            FileOutputStream outputStream = new FileOutputStream(configFile); // Throws IOException
            InputStream in = getResourceAsStream("config.yml"); // This file must exist in the jar resources folder
            in.transferTo(outputStream); // Throws IOException
        }

        if (!getDataFolder().exists()) {
            getLogger().info("Created config folder: " + getDataFolder().mkdir());
        }

        configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
    }

    private String[] getConfigItems() throws IOException {
        this.setupConfig();
        String[] configItems = new String[2];

        configItems[0] = configuration.getString("token");  // Discord Bot Token
        configItems[1] = configuration.getString("category"); // Discord Category ID
        return configItems;
    }

    public static BungeecordDiscordSRV getPlugin() {
        return instance;
    }
}
