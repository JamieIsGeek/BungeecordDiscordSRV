package dev.jamieisgeek.bungeecorddiscordsrv.Commands;

import dev.jamieisgeek.bungeecorddiscordsrv.BungeecordDiscordSRV;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.io.IOException;

public class ReloadCommand extends Command {
    public ReloadCommand() {
        super("bsrvreload", "bungeecorddiscordsrv.reload");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender.hasPermission("bungeecorddiscordsrv.reload")) {

            try {
                BungeecordDiscordSRV.getPlugin().setupConfig();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            sender.sendMessage(ChatColor.GREEN + "Reloaded config!");
        } else {
            sender.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
        }
    }
}
