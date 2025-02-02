package org.discord.bot.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpGet {
    private String ip;
    public HttpGet(String ip) {
        this.ip = ip;
    }

    public String geoIP() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://ip-api.com/json/" + ip))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String jsonResult = response.body();
            ObjectMapper mapper = new ObjectMapper();

            JsonNode json = mapper.readTree(jsonResult);
            String country = "País: " + json.get("country").asText();
            String city = "Cidade: " + json.get("city").asText();
            String region = "Região: " + json.get("region").asText();
            String lat = "Latitude: " + json.get("lat").asText();
            String lon = "Longitude: " + json.get("lon").asText();
            String zip = "Zip: " + json.get("zip").asText();
            String org = "Org: " + json.get("org").asText();

            return country + "\n"+city + "\n"+region + "\n"+lat + "\n"+lon + "\n"+zip + "\n"+org;

        } catch (Exception error) {
            error.printStackTrace();
        }

        return "Ocorreu um erro, não consegui fazer uma requisição com o ip solicitado.";
    }
}
