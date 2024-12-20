package com.example.expenseapi.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CurrencyRatesFetcher {

    public static double getCurrencyRates(String currencyCode) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String url = String.format("https://api.nbp.pl/api/exchangerates/rates/A/%s?format=json", currencyCode);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());
            return root.get("rates").get(0).get("mid").asDouble();
        } catch (Exception e) {
            return -1;
        }
    }
}
