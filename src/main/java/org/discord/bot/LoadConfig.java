package org.discord.bot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public class LoadConfig {
    private JsonNode config;

    public LoadConfig() {
        try {

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.json");
            ObjectMapper mapper = new ObjectMapper();

            config = mapper.readTree(inputStream);
        } catch (Exception error) {
            error.printStackTrace();
        }

    }

    public String getToken() {
        return config.get("token").asText();
    }

    public String getPrefix() {
        return config.get("prefix").asText();
    }

    public String TicketCategoryID() {
        return config.get("category_ticket_id").asText();
    }
}
