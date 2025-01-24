package com.multitenant.demo.configuration;

import com.multitenant.demo.ApplicationThread;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MultiTenantRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return ApplicationThread.getCurrentTenant();
    }
}
