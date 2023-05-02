package dev.jamieisgeek.bungeecorddiscordsrv.Commands;

import dev.jamieisgeek.bungeecorddiscordsrv.BungeecordDiscordSRV;
import dev.jamieisgeek.bungeecorddiscordsrv.Managers.DiscordManager;
import dev.jamieisgeek.bungeecorddiscordsrv.Models.DiscordBot;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.io.IOException;

public class ReloadCommand extends Command {
    public ReloadCommand() {
        super("bsrvreload", "bungeecorddiscordsrv.reload");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        try {
            BungeecordDiscordSRV.getPlugin().setupConfig();
            DiscordBot.getInstance().setupChannels();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sender.sendMessage(new TextComponent(ChatColor.GREEN + "Reloaded config!"));
    }
}
