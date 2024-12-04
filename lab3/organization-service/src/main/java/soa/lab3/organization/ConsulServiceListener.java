package soa.lab3.organization;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.entity.StringEntity;

@WebListener
public class ConsulServiceListener implements ServletContextListener {

    private static final String CONSUL_URL = "http://localhost:8500/v1/agent/service";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        String contextPath = context.getContextPath();
        String serviceId = "organization-service-" + contextPath.replace("/", "");

        String serviceDefinition = """
        {
            "Name": "organization-service",
            "ID": "%s",
            "Address": "127.0.0.1",
            "Port": 8080,
            "Tags": ["%s"],
            "Check": {
                "HTTP": "http://127.0.0.1:8080%s/api/health",
                "Interval": "10s"
            }
        }""".formatted(serviceId, contextPath, contextPath);


        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPut put = new HttpPut(CONSUL_URL + "/register");
            put.setEntity(new StringEntity(serviceDefinition));
            put.setHeader("Content-Type", "application/json");
            client.execute(put);
            System.out.println("Service registered with Consul. Context: " + contextPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        String contextPath = context.getContextPath();
        String serviceId = "organization-service-" + contextPath.replace("/", "");

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPut put = new HttpPut(CONSUL_URL + "/deregister/" + serviceId);
            client.execute(put);
            System.out.println("Service deregistered from Consul. Context: " + contextPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

