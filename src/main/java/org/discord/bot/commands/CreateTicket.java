package org.discord.bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.discord.bot.LoadConfig;
import org.jetbrains.annotations.NotNull;

public class CreateTicket extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        LoadConfig config = new LoadConfig();
        String prefix = config.getPrefix();

        String[] args = event.getMessage().getContentRaw().split(" ");
        Button openTicket = Button.primary("open-ticket-button", "📩  Abrir ticket");

        if (args[0].equalsIgnoreCase(prefix + "abrir")) {
            MessageEmbed embed = new EmbedBuilder()
                    .setTitle("📩  Abra um ticket")
                    .setDescription("Para abrir um ticket, clique no botão abaixo e aguarde para ser atendido.")
                    .setFooter("Clique aqui para abrir um ticket")
                    .setColor(0x000000)
                    .build();

                    event.getChannel().sendMessageEmbeds(embed).setActionRow(openTicket).queue();
        }
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        super.onModalInteraction(event);
        if (event.getModalId().equalsIgnoreCase("question-modal")) {
            String question = event.getValue("question-input").getAsString();
            LoadConfig conf = new LoadConfig();
            String TicketCategoryID = conf.TicketCategoryID();

            Category category = event.getGuild().getCategoryById(TicketCategoryID);
            category.createTextChannel("📂 ticket-" + event.getInteraction().getUser().getName()).queue(channel -> {
                channel.upsertPermissionOverride(event.getGuild().getPublicRole())
                        .setDenied(Permission.VIEW_CHANNEL).queue();

                channel.upsertPermissionOverride(event.getInteraction().getMember())
                        .setAllowed(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND).queue();

                MessageEmbed embed = new EmbedBuilder()
                        .setTitle("📩  Novo ticket")
                        .setThumbnail(event.getInteraction().getUser().getAvatarUrl())
                        .addField("❓  Dúvida", question, true)
                        .addField("👤  Usuário", event.getInteraction().getUser().getName(), false)
                        .setFooter("Aguarde alguém lhe-atender")
                        .build();

                Button ticket_delete = Button.primary("delete-ticket", "🗑  Deletar");
                Button ticket_close = Button.danger("close-ticket", "❌  Fechar");

                channel.sendMessageEmbeds(embed).setActionRow(ticket_close, ticket_delete).queue();

            });
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        super.onButtonInteraction(event);
        TextInput question = TextInput.create("question-input", "Digite sua dúvida", TextInputStyle.SHORT)
                .setRequired(true)
                .build();

        Modal modalQuestion = Modal.create("question-modal", "Qual sua dúvida?")
                .addActionRow(question)
                .build();

        if (event.getComponentId().equalsIgnoreCase("open-ticket-button")) {
            event.replyModal(modalQuestion).queue();
        } else if (event.getComponentId().equalsIgnoreCase("delete-ticket")) {
            if (!event.getInteraction().getMember().hasPermission(Permission.ADMINISTRATOR)) {
                event.getChannel().sendMessage("Você não tem permissão!");
                return;
            }

            event.getChannel().delete().queue();

        } else if (event.getComponentId().equalsIgnoreCase("close-ticket")) {
            event.getChannel().asTextChannel().upsertPermissionOverride(event.getInteraction().getMember())
                    .setDenied(Permission.VIEW_CHANNEL).queue();

            event.getChannel().sendMessage(event.getInteraction().getUser().getAsMention() + "Fechou este ticket!").queue();
        }
    }
}
