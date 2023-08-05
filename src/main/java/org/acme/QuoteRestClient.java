package org.acme;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/jokes")
@RegisterRestClient(configKey="quotes-service")
@Produces(MediaType.APPLICATION_JSON)
public interface QuoteRestClient {

    @GET
    @Path(("/categories"))
    Uni<List<String>> getCategories();

    @GET
    @Path("/random")
    String getQuote();
}
