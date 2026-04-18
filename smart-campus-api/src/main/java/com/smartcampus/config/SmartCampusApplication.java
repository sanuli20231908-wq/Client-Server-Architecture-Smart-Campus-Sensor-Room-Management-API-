package com.smartcampus.config;

import com.smartcampus.resource.DiscoveryResource;
import com.smartcampus.resource.RoomResource;
import com.smartcampus.resource.SensorResource;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api/v1")
public class SmartCampusApplication extends ResourceConfig {
    public SmartCampusApplication() {
        register(DiscoveryResource.class);
        register(RoomResource.class);
        register(SensorResource.class);
        packages("org.glassfish.jersey.jackson");
    }
}