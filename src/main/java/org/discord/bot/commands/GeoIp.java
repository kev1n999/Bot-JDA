package org.discord.bot.commands;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.discord.bot.LoadConfig;
import org.jetbrains.annotations.NotNull;

public class GeoIp extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        LoadConfig config = new LoadConfig();
        String prefix = config.getPrefix();

        String[] args = event.getMessage().getContentRaw().split(" ");

        if (args[0].equalsIgnoreCase(prefix + "geoip")) {
            String ip = args[1];
            HttpGet request = new HttpGet(ip);
            String result = request.geoIP();

            MessageEmbed embed = new EmbedBuilder()
                    .setTitle("🌐  Informações do ip")
                    .setDescription(result)
                    .setFooter("Por " + event.getAuthor().getName())
                    .setColor(0x000000)
                    .build();

            event.getChannel().sendMessageEmbeds(embed).queue();
        }
    }
}
