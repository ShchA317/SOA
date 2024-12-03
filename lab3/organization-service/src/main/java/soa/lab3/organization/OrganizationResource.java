package soa.lab3.organization;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import soa.lab3.organization.model.Address;
import soa.lab3.organization.model.Organization;
import soa.lab3.organization.ejb.OrganizationService;

import java.util.*;

@Path("/organizations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrganizationResource {

    @EJB
    private OrganizationService organizationService;

    @GET
    @Path("/{id}")
    public Response getOrganization(@PathParam("id") Long id) {
        Organization org = organizationService.getOrganization(id);
        if (org == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(org).build();
    }

    @POST
    public Response createOrganization(Organization org) {
        Organization createdOrg = organizationService.createOrganization(org);
        return Response.status(Response.Status.CREATED).entity(createdOrg).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateOrganization(@PathParam("id") Long id, Organization updatedOrg) {
        Organization existingOrg = organizationService.getOrganization(id);
        if (existingOrg == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Organization updated = organizationService.updateOrganization(id, updatedOrg);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteOrganization(@PathParam("id") Long id) {
        boolean deleted = organizationService.deleteOrganization(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    @GET
    public Response getFilteredAndSortedOrganizations(
            @QueryParam("creationDate") String creationDate,
            @QueryParam("annualTurnover") Integer annualTurnover,
            @QueryParam("sort") String sort) {
        try {
            List<Organization> result = organizationService.getFilteredOrganizations(creationDate, annualTurnover, sort);
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid query parameters").build();
        }
    }

    @GET
    @Path("/count-by-employees")
    public Response countByEmployeesCount(@QueryParam("count") Long count) {
        if (count == null || count < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Parameter 'count' must be a positive integer.")
                    .build();
        }

        long resultCount = organizationService.countByEmployeesCount(count);
        Map<String, Long> response = Map.of("count", resultCount);
        return Response.ok(response).build();
    }

    @GET
    @Path("/search-by-fullname")
    public Response searchByFullName(@QueryParam("substring") String substring) {
        if (substring == null || substring.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Parameter 'substring' must not be empty.")
                    .build();
        }

        List<Organization> filteredOrganizations = organizationService.searchByFullName(substring);
        return Response.ok(filteredOrganizations).build();
    }

    @GET
    @Path("/group-by-address")
    public Response groupByOfficialAddress() {
        try {
            Map<Address, Long> groupedByAddress = organizationService.groupByOfficialAddress();
            return Response.ok(groupedByAddress).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while grouping by address").build();
        }
    }
}