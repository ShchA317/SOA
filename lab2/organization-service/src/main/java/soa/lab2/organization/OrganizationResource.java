package soa.lab2.organization;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.*;

@Path("/organizations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrganizationResource {

    private static final List<Organization> organizations = new ArrayList<>();

    @GET
    public Response getOrganizations(@QueryParam("creationDate") String creationDate,
                                     @QueryParam("annualTurnover") Integer annualTurnover,
                                     @QueryParam("sort") String sort) {
        List<Organization> resultList = new ArrayList<>();
        if(annualTurnover != null) {
             resultList = organizations.stream()
                    .filter(org -> org.getAnnualTurnover() > annualTurnover).toList();
        }
        return Response.ok(resultList).build();
    }

    @POST
    public Response createOrganization(Organization organization) {
        organizations.add(organization);
        return Response.status(Response.Status.CREATED).entity(organization).build();
    }

    @GET
    @Path("/{id}")
    public Response getOrganizationById(@PathParam("id") long id) {
        return organizations.stream()
                .filter(org -> org.getId() == id)
                .findFirst()
                .map(org -> Response.ok(org).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @PUT
    @Path("/{id}")
    public Response updateOrganizationById(@PathParam("id") long id, Organization updatedOrganization) {
        for (int i = 0; i < organizations.size(); i++) {
            if (organizations.get(i).getId() == id) {
                organizations.set(i, updatedOrganization);
                return Response.ok(updatedOrganization).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteOrganizationById(@PathParam("id") long id) {
        if (organizations.removeIf(org -> org.getId() == id)) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/group-by-address")
    public Response groupByOfficialAddress() {
        Map<String, Long> grouped = new HashMap<>();
        for (Organization org : organizations) {
            String address = org.getOfficialAddress();
            grouped.put(address, grouped.getOrDefault(address, 0L) + 1);
        }
        return Response.ok(grouped).build();
    }

    @GET
    @Path("/count-by-employees")
    public Response countByEmployeesCount(@QueryParam("count") long count) {
        long organizationCount = organizations.stream()
                .filter(org -> org.getEmployeesCount() > count)
                .count();
        return Response.ok(Map.of("count", organizationCount)).build();
    }

    @GET
    @Path("/search-by-fullname")
    public Response searchByFullName(@QueryParam("substring") String substring) {
        List<Organization> result = new ArrayList<>();
        for (Organization org : organizations) {
            if (org.getFullName().contains(substring)) {
                result.add(org);
            }
        }
        return Response.ok(result).build();
    }
}
