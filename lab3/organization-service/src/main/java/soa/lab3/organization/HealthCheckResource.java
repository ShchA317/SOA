package soa.lab3.organization;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/health")
@Produces(MediaType.TEXT_PLAIN)
public class HealthCheckResource {
    @GET
    public String healthCheck() {
        return "OK";
    }
}

