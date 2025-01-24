package com.multitenant.demo.configuration;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DataSourceBasedMultiTenantConnectionProviderImpl implements MultiTenantConnectionProvider {
    private final Map<String, DataSource> dataSources = new HashMap<>();

    public DataSource getAnyDataSource() {
        return dataSources.values().iterator().next();
    }

    public DataSource selectDataSource(String tenantIdentifier) {
        return dataSources.get(tenantIdentifier);
    }


    public  void addDataSource(String tenantId, DataSource dataSource) {
        dataSources.put(tenantId, dataSource);
    }

    public void addDataSource(String tenantId, String _dataSourceUrl, String _username, String _password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(_dataSourceUrl);
        dataSource.setUsername(_username);
        dataSource.setPassword(_password);
        dataSources.put(tenantId, dataSource);
    }

    public void removeDataSource(String tenantId) {
        dataSources.remove(tenantId);
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        if (!dataSources.values().isEmpty())
            return dataSources.get("default").getConnection();
        return null;
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(Object o) throws SQLException {
        String tenantIdentifier = (String) o;
        DataSource dataSource = dataSources.get(tenantIdentifier);
        if (dataSource == null) {
            throw new SQLException("Unkonwn tenant identifier: " + tenantIdentifier);
        }
        return dataSource.getConnection();
    }

    @Override
    public void releaseConnection(Object o, Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class<?> unwrapType) {
        return MultiTenantConnectionProvider.class.isAssignableFrom(unwrapType);
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return (T) this;
    }
}
