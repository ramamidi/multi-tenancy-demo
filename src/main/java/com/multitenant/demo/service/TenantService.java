package com.multitenant.demo.service;

import com.multitenant.demo.configuration.DataSourceBasedMultiTenantConnectionProviderImpl;
import com.multitenant.demo.database.entities.TenantDataSource;
import com.multitenant.demo.database.repositories.TenantDatasourceRepository;
import com.multitenant.demo.dto.TenantInfo;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TenantService {
    @Autowired
    private TenantDatasourceRepository repository;

    @Autowired
    private DataSourceBasedMultiTenantConnectionProviderImpl connectionProvider;

    @Autowired
    ModelMapper modelMapper;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.base-url}") // Set this to "jdbc:postgresql://localhost:5432/"
    private String baseUrl;

    @PostConstruct
    public void registerExistingTenants() {
        List<TenantInfo> allTenants = loadAllTenantsFromDB();
        for (TenantInfo tenantInfo: allTenants) {
            connectionProvider.addDataSource(tenantInfo.getTenantIdentifier(),
                    tenantInfo.getDatasourceUrl(), tenantInfo.getUsername(), tenantInfo.getPassword());
        }
    }

    private List<TenantInfo> loadAllTenantsFromDB() {
        List<TenantDataSource> tenants = repository.findByActive(true);
        return tenants.stream().map(tenant -> modelMapper.map(tenant, TenantInfo.class))
                .collect(Collectors.toList());
    }

    public boolean createTenant(String tenantId) {
        try {
            String datasourceUrl = baseUrl + tenantId;
            repository.findByTenantIdentifier(tenantId);
            addTenantDataSourceRecord(tenantId, datasourceUrl, username, password);
            connectionProvider.addDataSource(tenantId, datasourceUrl, username, password);
            createTenantDatabase(tenantId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeTenant(String tenantId) {
        return removeTenant(tenantId, false);
    }

    public boolean removeTenant(String tenantId, boolean removeDatabase) {
        try {
            connectionProvider.removeDataSource(tenantId);
            removeTenantDatasource(tenantId);
            if (removeDatabase) {
                removeTenantDatabase(tenantId);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void createTenantDatabase(String tenantId) throws SQLException {
        String createDatabaseSql = "CREATE DATABASE \"" + tenantId + "\"";
        Connection connection = null;
        try {
            connection = connectionProvider.getAnyConnection();
            connection.createStatement().execute(createDatabaseSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean initializeTenantDatabase(String tenantId) throws SQLException {
        Connection connection = null;
        try {
            InputStream resource = new ClassPathResource("createTables.sql").getInputStream();
            String createTablesSqlScript = StreamUtils.copyToString(resource, StandardCharsets.UTF_8);

            connection = connectionProvider.getConnection(tenantId);
            if (connection == null) {
                throw  new RuntimeException("Unable to get connection for tenantIdentifier: " + tenantId);
            }

            connection.createStatement().execute(createTablesSqlScript);
            return true;
        } catch (IOException ex) {
          throw new RuntimeException("Unable to read createTables.sql");
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
            return false;
        } catch (Exception exception) {
            throw new RuntimeException("Unable to create tables for tenantIdentifier: " + tenantId);
        }
    }

    private void removeTenantDatabase(String tenantId) throws SQLException {
        Connection connection = null;
        String dropDatabaseSql = "DROP DATABASE \"" + tenantId + "\"";
        try {
            connection = connectionProvider.getAnyConnection();
            connection.createStatement().execute(dropDatabaseSql);
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }
    }

    private void addTenantDataSourceRecord(String tenantId, String datasourceUrl, String username, String password) {
        TenantDataSource tenantDataSource = new TenantDataSource(tenantId, datasourceUrl, username, password, true);
        repository.save(tenantDataSource);
    }

    private  void removeTenantDatasource(String tenantId) {
        TenantDataSource tenantDataSource = repository.findByTenantIdentifier(tenantId);
        tenantDataSource.setActive(false);
        repository.save(tenantDataSource);
    }
}
