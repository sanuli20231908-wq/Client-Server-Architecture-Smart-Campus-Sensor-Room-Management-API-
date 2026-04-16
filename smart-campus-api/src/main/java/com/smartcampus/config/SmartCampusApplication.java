package com.smartcampus.config;

import com.smartcampus.resource.DiscoveryResource;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api/v1")
public class SmartCampusApplication extends ResourceConfig {
    public SmartCampusApplication() {
        register(DiscoveryResource.class);
        packages("org.glassfish.jersey.jackson");
    }
}