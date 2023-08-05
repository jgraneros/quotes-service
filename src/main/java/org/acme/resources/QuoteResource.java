package org.acme.resources;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.acme.QuoteService;
import org.acme.rest.client.QuotesRestClient;
import org.acme.tools.Utils;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Path("/quote")
public class QuoteResource {

    @Inject
    QuoteService quoteService;

    @Inject
    QuotesRestClient client;

    @GET
    public Uni<Response> hello() throws IOException, InterruptedException {
        //Thread currentThread = Thread.currentThread();
        //log.info("El nombre del hilo actual es: " + currentThread.getName());
        //log.info("El ID del hilo actual es: " + currentThread.getId());
        //return quoteService.getQuoteCategories().map(retrieved -> Response.ok(retrieved).build());
        Utils.getCurrentThread();
        return Uni.createFrom()
                .completionStage(CompletableFuture.supplyAsync(() -> {
                    try {
                        return client.getQuote();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }, Infrastructure.getDefaultExecutor()))
                .map(retrieved -> Response.ok(retrieved).build())
                .onFailure().recoverWithNull();
    }
}
