package com.multitenant.demo.database.repositories;

import com.multitenant.demo.database.entities.TenantDataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TenantDatasourceRepository extends JpaRepository<TenantDataSource, Long> {
    public List<TenantDataSource> findByActive(Boolean active);

    public TenantDataSource findByTenantIdentifier(String tenantIdentifier);
}
