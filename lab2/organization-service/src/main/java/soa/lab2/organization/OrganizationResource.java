package soa.lab2.organization;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/organizations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrganizationResource {

    public static final Map<Long, Organization> organizations = new HashMap<>();
    private static final AtomicLong idGenerator = new AtomicLong(1);

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

    @GET
    public Response getFilteredAndSortedOrganizations(
            @QueryParam("creationDate") String creationDate,
            @QueryParam("annualTurnover") Integer annualTurnover,
            @QueryParam("sort") String sort) {
        try {
            Stream<Organization> filteredStream = organizations.values().stream();

            if (creationDate != null) {
                Date filterDate = Date.from(LocalDate.parse(creationDate).atStartOfDay(ZoneId.systemDefault()).toInstant());
                filteredStream = filteredStream.filter(org -> org.getCreationDate().after(filterDate));
            }

            if (annualTurnover != null) {
                filteredStream = filteredStream.filter(org -> org.getAnnualTurnover() < annualTurnover);
            }

            if (sort != null) {
                String[] sortParams = sort.split(",");
                if (sortParams.length == 2) {
                    String field = sortParams[0];
                    boolean ascending = "asc".equalsIgnoreCase(sortParams[1]);
                    Comparator<Organization> comparator = applySortOrder(getComparator(field), ascending);

                    filteredStream = filteredStream.sorted(comparator);
                }
            }

            List<Organization> result = filteredStream.toList();
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid query parameters").build();
        }
    }

    private Comparator<Organization> getComparator(String field) {
        return switch (field) {
            case "name" -> Comparator.comparing(Organization::getName);
            case "creationDate" -> Comparator.comparing(Organization::getCreationDate);
            default -> throw new IllegalArgumentException("Unsupported sort field: " + field);
        };
    }

    @GET
    @Path("/count-by-employees")
    public Response countByEmployeesCount(@QueryParam("count") Long count) {
        if (count == null || count < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Parameter 'count' must be a positive integer.")
                    .build();
        }

        long resultCount = organizations.values().stream()
                .filter(org -> org.getEmployeesCount() != null && org.getEmployeesCount() > count)
                .count();

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

        List<Organization> filteredOrganizations = organizations.values().stream()
                .filter(org -> org.getFullName() != null && org.getFullName().contains(substring))
                .toList();

        return Response.ok(filteredOrganizations).build();
    }

    @GET
    @Path("/group-by-address")
    public Response groupByOfficialAddress() {
        try {
            Map<Address, Long> groupedByAddress = organizations.values().stream()
                    .collect(Collectors.groupingBy(
                            Organization::getOfficialAddress,
                            Collectors.counting()
                    ));

            List<Map<String, Object>> result = groupedByAddress.entrySet().stream()
                    .map(entry -> Map.of(
                            "officialAddress", entry.getKey(),
                            "count", entry.getValue()
                    ))
                    .toList();

            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while grouping by address").build();
        }
    }

    private Comparator<Organization> applySortOrder(Comparator<Organization> comparator, boolean ascending) {
        return ascending ? comparator : comparator.reversed();
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