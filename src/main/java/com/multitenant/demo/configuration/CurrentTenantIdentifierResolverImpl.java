package com.multitenant.demo.configuration;

import com.multitenant.demo.ApplicationThread;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

    private static final String DEFAULT_TENANT = "default";

    @Override
    public Object resolveCurrentTenantIdentifier() {
        String tenantId = ApplicationThread.getCurrentTenant();
        return tenantId != null ? tenantId : DEFAULT_TENANT;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
