package soa.lab4.organization;

import javax.xml.ws.Endpoint;

public class OrganizationServicePublisher {
    public static void main(String[] args) {
        String serviceURL = "http://localhost:8080/ws/organization";
        System.out.println("Publishing service at: " + serviceURL);
        Endpoint.publish(serviceURL, new OrganizationServiceBean());
    }
}
