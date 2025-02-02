package org.discord.bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.discord.bot.LoadConfig;
import org.discord.bot.Main;
import org.jetbrains.annotations.NotNull;

public class onReady extends ListenerAdapter {
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        super.onReady(event);
        System.out.printf("Bot online em %d servidores!", event.getGuildTotalCount());

        Main.jda.getPresence().setActivity(Activity.listening("Java é foda"));
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        LoadConfig config = new LoadConfig();
        String prefix = config.getPrefix();

        if (event.getMessage().getContentRaw().split(" ")[0].equalsIgnoreCase(prefix + "Ping")) {
            event.getChannel().sendMessage(Long.toString(Main.jda.getGatewayPing())).queue();
        }
    }
}
