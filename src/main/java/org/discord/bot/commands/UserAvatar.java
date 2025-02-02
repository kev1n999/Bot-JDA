package org.discord.bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.discord.bot.LoadConfig;
import org.jetbrains.annotations.NotNull;

public class UserAvatar extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        LoadConfig config = new LoadConfig();
        String prefix = config.getPrefix();

        String[] args = event.getMessage().getContentRaw().split(" ");
        User user;
        if (event.getAuthor().isBot()) return;

        if (args[0].equalsIgnoreCase(prefix+"avatar") || args[0].equalsIgnoreCase(prefix+"av")) {
            if (event.getMessage().getMentions().getUsers().isEmpty()) {
                user = event.getAuthor();
            } else {
                user = event.getMessage().getMentions().getUsers().getFirst();
            }

            String avatarURL = user.getAvatarUrl() + "?size=1024";
            Button buttonAvatarURL = Button.link(avatarURL, "🔗  Abrir no navegador");

            MessageEmbed embed = new EmbedBuilder()
                    .setTitle("🖼  Avatar de " + user.getName())
                    .setImage(avatarURL)
                    .setColor(0x000000)
                    .build();

            event.getChannel().sendMessageEmbeds(embed).setActionRow(buttonAvatarURL).queue();
        }
    }
}
