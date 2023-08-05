package org.acme;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@ApplicationScoped
public class QuoteService {

    @Inject
    @RestClient
    QuoteRestClient restClient;

    public Uni<List<String>> getQuoteCategories() {

        return restClient.getCategories();

    }


}
