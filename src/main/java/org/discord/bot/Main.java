package org.discord.bot;

import com.fasterxml.jackson.databind.JsonNode;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import okhttp3.internal.http2.Http2Connection;
import org.discord.bot.commands.*;
import org.jetbrains.annotations.NotNull;

public class Main extends ListenerAdapter {
    public static JDA jda;
    static String token;
    static {
        LoadConfig config = new LoadConfig();
        token = config.getToken();
    }

    public static void main(String[] args) {
        jda = JDABuilder.create(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS).build();

        // All commands
        jda.addEventListener(new UserAvatar());
        jda.addEventListener(new CreateTicket());
        jda.addEventListener(new onReady());
        jda.addEventListener(new Administrator());
        jda.addEventListener(new GeoIp());
    }
}
