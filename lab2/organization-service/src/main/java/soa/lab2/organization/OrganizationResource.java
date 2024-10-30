package soa.lab2.organization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.*;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

@Path("/organizations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrganizationResource {

    private static Map<Long, Organization> organizations = new HashMap<>();
    private static AtomicLong idGenerator = new AtomicLong(1);

    @GET
    public Response getAllOrganizations() {
        return Response.ok(organizations.values()).build();
    }

    @GET
    @Path("/{id}")
    public Response getOrganization(@PathParam("id") Long id) {
        Organization org = organizations.get(id);
        if (org == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(org).build();
    }

    @POST
    public Response createOrganization(@Valid Organization org) {
        org.setId(idGenerator.getAndIncrement());
        org.setCreationDate(LocalDate.now());

        organizations.put(org.getId(), org);
        return Response.status(Response.Status.CREATED).entity(org).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateOrganization(@PathParam("id") Long id, @Valid Organization updatedOrg) {
        Organization existingOrg = organizations.get(id);
        if (existingOrg == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        updatedOrg.setId(id);
        updatedOrg.setCreationDate(existingOrg.getCreationDate());
        organizations.put(id, updatedOrg);
        return Response.ok(updatedOrg).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteOrganization(@PathParam("id") Long id) {
        Organization removedOrg = organizations.remove(id);
        if (removedOrg == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    @Provider
    public static class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
        @Override
        public Response toResponse(ConstraintViolationException exception) {
            List<String> errors = new ArrayList<>();
            for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
                errors.add(violation.getPropertyPath() + ": " + violation.getMessage());
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
        }
    }
}