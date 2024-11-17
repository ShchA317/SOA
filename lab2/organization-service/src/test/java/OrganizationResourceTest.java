import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import jakarta.ws.rs.core.Response;
import soa.lab2.organization.Organization;
import soa.lab2.organization.OrganizationResource;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SuppressWarnings("unchecked")
class OrganizationResourceTest {

    private OrganizationResource resource;

    @BeforeEach
    void setUp() {
        resource = new OrganizationResource();
        populateOrganizations();
    }

    private void populateOrganizations() {
        Organization org1 = new Organization();
        org1.setId(1L);
        org1.setName("Alpha");
        org1.setCreationDate(Date.from(LocalDate.of(2020, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        org1.setAnnualTurnover(500);
        OrganizationResource.organizations.put(org1.getId(), org1);

        Organization org2 = new Organization();
        org2.setId(2L);
        org2.setName("Beta");
        org2.setCreationDate(Date.from(LocalDate.of(2021, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        org2.setAnnualTurnover(300);
        OrganizationResource.organizations.put(org2.getId(), org2);

        Organization org3 = new Organization();
        org3.setId(3L);
        org3.setName("Gamma");
        org3.setCreationDate(Date.from(LocalDate.of(2022, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        org3.setAnnualTurnover(800);
        OrganizationResource.organizations.put(org3.getId(), org3);
    }

    @Test
    void testGetAllOrganizationsWithoutFilters() {
        Response response = resource.getFilteredAndSortedOrganizations(null, null, null);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<Organization> organizations = (List<Organization>) response.getEntity();
        assertEquals(3, organizations.size());
    }

    @Test
    void testFilterByCreationDate() {
        Response response = resource.getFilteredAndSortedOrganizations("2020-12-31", null, null);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<Organization> organizations = (List<Organization>) response.getEntity();
        assertEquals(2, organizations.size());
        assertTrue(organizations.stream().allMatch(org -> org.getCreationDate().after(Date.from(
                LocalDate.of(2020, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()))));
    }

    @Test
    void testFilterByAnnualTurnover() {
        Response response = resource.getFilteredAndSortedOrganizations(null, 600, null);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<Organization> organizations = (List<Organization>) response.getEntity();
        assertEquals(2, organizations.size());
        assertTrue(organizations.stream().allMatch(org -> org.getAnnualTurnover() < 600));
    }

    @Test
    void testSortByNameAscending() {
        Response response = resource.getFilteredAndSortedOrganizations(null, null, "name,asc");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<Organization> organizations = (List<Organization>) response.getEntity();
        assertEquals("Alpha", organizations.get(0).getName());
        assertEquals("Beta", organizations.get(1).getName());
        assertEquals("Gamma", organizations.get(2).getName());
    }

    @Test
    void testSortByCreationDateDescending() {
        Response response = resource.getFilteredAndSortedOrganizations(null, null, "creationDate,desc");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<Organization> organizations = (List<Organization>) response.getEntity();
        assertEquals("Gamma", organizations.get(0).getName());
        assertEquals("Beta", organizations.get(1).getName());
        assertEquals("Alpha", organizations.get(2).getName());
    }

    @Test
    void testFilterAndSort() {
        Response response = resource.getFilteredAndSortedOrganizations("2020-12-31", 600, "name,asc");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<Organization> organizations = (List<Organization>) response.getEntity();
        assertEquals(1, organizations.size());
        assertEquals("Beta", organizations.get(0).getName());
    }

    @Test
    void testInvalidSortParameter() {
        Response response = resource.getFilteredAndSortedOrganizations(null, null, "invalidField,asc");
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Invalid query parameters", response.getEntity());
    }
}

