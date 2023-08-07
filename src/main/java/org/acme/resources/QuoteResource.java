package org.acme.resources;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.acme.QuoteService;
import org.acme.model.Quote;
import org.acme.rest.client.QuotesRestClient;
import org.acme.tools.Utils;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Path("/quote")
public class QuoteResource {

    public static final Executor DEFAULT_EXECUTOR=Infrastructure.getDefaultExecutor();
    public static final Executor DEFAULT_WORKER_POOL=Infrastructure.getDefaultWorkerPool();
    @Inject
    QuoteService quoteService;

    @Inject
    QuotesRestClient client;

    @Path("/uni")
    @GET
    public Uni<String> hello() throws InterruptedException {
        log.info("ejecutando el metodo hello");
        Utils.getCurrentThread();
        return Uni.createFrom().item("hola");
    }



    @GET
    public Uni<Response> getQuoteAndValidate() throws IOException, InterruptedException {
        //Thread currentThread = Thread.currentThread();
        //log.info("El nombre del hilo actual es: " + currentThread.getName());
        //log.info("El ID del hilo actual es: " + currentThread.getId());
        //return quoteService.getQuoteCategories().map(retrieved -> Response.ok(retrieved).build());
        Utils.getCurrentThread();
        var retorno = Uni.createFrom()
                .completionStage(CompletableFuture
                        .supplyAsync(this::getQuoteFromClientHandler, DEFAULT_EXECUTOR))
                .invoke(retrieved -> log.info("retrieved1: " + retrieved))
                .onItem().transformToUni(retrieved -> Uni.createFrom().completionStage(CompletableFuture
                        .supplyAsync(this::getQuoteFromClientHandler, DEFAULT_EXECUTOR)))
                .invoke(retrieved -> log.info("retrieved2: " + retrieved))
                .flatMap(retrieved -> Uni.createFrom().completionStage(CompletableFuture
                        .supplyAsync(() -> {
                            Boolean isString;
                            try {
                                isString = this.blockingValidation(retrieved);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            log.info("logger invocado desde el supply async");
                            return isString;
                        }, DEFAULT_EXECUTOR)))
                .map(retrieved -> Response.ok(retrieved).build())
                .onFailure().recoverWithNull();

        Utils.getCurrentThread();

        return retorno;

    }

    @Path("/all")
    @GET
    @Transactional
    public Uni<Response> getAll() {
        return Uni.createFrom().completionStage(CompletableFuture.supplyAsync(() -> Quote.listAll(), DEFAULT_EXECUTOR))
                .map(retrieved -> Response.ok(retrieved).build());
    }

    /**
     * Blocking
     * @return
     */
    private String getQuoteFromClientHandler() {
        try {
            return client.getQuote();
        } catch (IOException | InterruptedException e) {
            throw  new RuntimeException(e);
        }
    }


    private String mapAndPersist(String frase) {

        Quote quote = Quote.builder()
                .quote(frase)
                .category(null)
                .build();

        quote.persist();

        return frase;
    }

    /**
     * Blocking
     * @param quote
     * @return
     * @throws InterruptedException
     */
    private Boolean blockingValidation(Object quote) throws InterruptedException {

        Thread.sleep(30000);

        if (quote instanceof String) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }
}
