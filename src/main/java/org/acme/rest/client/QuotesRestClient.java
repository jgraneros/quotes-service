package org.acme.rest.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.common.annotation.NonBlocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
//@Blocking
@Slf4j
public class QuotesRestClient {

    private final String CHUCK_NORRIS_API = "https://api.chucknorris.io/jokes/random";
    private final String CHUCK_NORRIS_CATEGORIES_API = "https://api.chucknorris.io/jokes/categories";

    private ExecutorService executorService = Executors.newFixedThreadPool(5);
    private HttpClient httpClient = HttpClient.newBuilder()
            .executor(executorService)
            .version(HttpClient.Version.HTTP_2)
            .build();


    public String getQuote() throws IOException, InterruptedException {
            Thread.sleep(2100);
            Thread currentThread = Thread.currentThread();
            log.info("El nombre del hilo actual es: " + currentThread.getName());
            log.info("El ID del hilo actual es: " + currentThread.getId());
            return this.sendRequest(httpClient);
    }


    private String sendRequest(HttpClient httpClient) throws IOException, InterruptedException {

        HttpResponse<String> response;
        ObjectMapper objectMapper = new ObjectMapper();

        HttpRequest request =HttpRequest.newBuilder()
                .uri(URI.create(CHUCK_NORRIS_API))
                .GET()
                .build();

        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode jsonNode = objectMapper.readTree(response.body());

        return jsonNode.get("value").asText();
    }

}
