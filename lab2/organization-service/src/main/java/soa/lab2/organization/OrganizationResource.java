package soa.lab2.organization;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.*;

@Path("/organizations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrganizationResource {

    // In-memory list для демонстрации
    private static List<Organization> organizations = new ArrayList<>();

    // Получение списка всех организаций
    @GET
    public Response getOrganizations(@QueryParam("creationDate") String creationDate,
                                     @QueryParam("annualTurnover") Integer annualTurnover,
                                     @QueryParam("sort") String sort) {
        // Здесь можно добавить логику фильтрации и сортировки на основе параметров
        String hello = "hello";
        return Response.ok(organizations).build();
    }

    // Добавление новой организации
    @POST
    public Response createOrganization(Organization organization) {
        // Вставляем логику для добавления новой организации
        organizations.add(organization);
        return Response.status(Response.Status.CREATED).entity(organization).build();
    }

    // Получение организации по ID
    @GET
    @Path("/{id}")
    public Response getOrganizationById(@PathParam("id") long id) {
        // Поиск организации по ID
        return organizations.stream()
                .filter(org -> org.getId() == id)
                .findFirst()
                .map(org -> Response.ok(org).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    // Обновление организации по ID
    @PUT
    @Path("/{id}")
    public Response updateOrganizationById(@PathParam("id") long id, Organization updatedOrganization) {
        // Логика для обновления данных организации
        for (int i = 0; i < organizations.size(); i++) {
            if (organizations.get(i).getId() == id) {
                organizations.set(i, updatedOrganization);
                return Response.ok(updatedOrganization).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // Удаление организации по ID
    @DELETE
    @Path("/{id}")
    public Response deleteOrganizationById(@PathParam("id") long id) {
        // Логика для удаления организации
        if (organizations.removeIf(org -> org.getId() == id)) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // Группировка по адресу и возврат количества элементов
    @GET
    @Path("/group-by-address")
    public Response groupByOfficialAddress() {
        // Логика для группировки организаций по адресу и возврата количества
        Map<String, Long> grouped = new HashMap<>();
        for (Organization org : organizations) {
            String address = org.getOfficialAddress();
            grouped.put(address, grouped.getOrDefault(address, 0L) + 1);
        }
        return Response.ok(grouped).build();
    }

    // Количество организаций по количеству сотрудников
    @GET
    @Path("/count-by-employees")
    public Response countByEmployeesCount(@QueryParam("count") long count) {
        long organizationCount = organizations.stream()
                .filter(org -> org.getEmployeesCount() > count)
                .count();
        return Response.ok(Map.of("count", organizationCount)).build();
    }

    // Поиск организаций по подстроке в названии
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
