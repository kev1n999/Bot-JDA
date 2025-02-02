package org.discord.bot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.discord.bot.LoadConfig;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Administrator extends ListenerAdapter {
    private final Map<String, UserSnowflake> pendingBans = new HashMap<>();

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getComponentId().equalsIgnoreCase("confirm-ban")) {
            String userId = event.getUser().getId();

            if (!pendingBans.containsKey(userId)) {
                event.reply("Nenhum banimento pendente encontrado para você.").setEphemeral(true).queue();
                return;
            }

            UserSnowflake userBan = pendingBans.remove(userId);
            event.getGuild().ban(userBan, 0, TimeUnit.SECONDS).queue();
            event.reply("Membro banido com sucesso!").setEphemeral(true).queue();
        }
        else if (event.getComponentId().equalsIgnoreCase("cancel-ban")) {
            String userId = event.getUser().getId();
            pendingBans.remove(userId);
            event.reply("Banimento cancelado.").setEphemeral(true).queue();
        }

    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        LoadConfig config = new LoadConfig();
        String prefix = config.getPrefix();

        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args[0].equalsIgnoreCase(prefix + "ban")) {
            if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                event.getChannel().sendMessage("Você não possui a permissão necessária para usar este comando.").queue();
                return;
            }

            if (event.getMessage().getMentions().getUsers().isEmpty()) {
                event.getChannel().sendMessage("Você precisa mencionar um membro para banir!").queue();
                return;
            }

            UserSnowflake userToBan = event.getMessage().getMentions().getUsers().get(0);
            String authorId = event.getAuthor().getId();
            pendingBans.put(authorId, userToBan);

            Button confirm = Button.primary("confirm-ban", "✔ Confirmar");
            Button cancel = Button.danger("cancel-ban", "❌ Cancelar");

            event.getChannel()
                    .sendMessage(event.getAuthor().getAsMention() + "\n\nTem certeza que deseja banir este membro?\n-# 🚨 Escolha a opção desejada nos botões abaixo:")
                    .setActionRow(confirm, cancel)
                    .queue();
        }

        if (args[0].equalsIgnoreCase(prefix + "clear")) {
            if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                event.getChannel().sendMessage("Você não possui a permissão necessária para usar este comando.").queue();
                return;
            }

            int limit = Integer.parseInt(args[1]);
            event.getChannel().purgeMessages(event.getChannel().getHistory().retrievePast(limit).complete());
            event.getChannel().sendMessage("#- ✔  | Foram apagadas" + limit + "mensagens");
        }
    }
}
