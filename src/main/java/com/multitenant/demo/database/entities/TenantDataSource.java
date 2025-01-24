package com.multitenant.demo.database.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "tenant_datasources")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TenantDataSource {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tenant_datasource_seq")
    @SequenceGenerator(name = "tenant_datasource_seq", sequenceName = "tenant_datasources_tenant_datasource_id_seq", allocationSize = 1)
    @Column(name = "tenant_datasource_id")
    private Long tenantDatasourceId;

    @Column(name = "tenant_identifier", unique = true)
    private String tenantIdentifier;

    @Column(name = "datasource_url")
    private String datasourceUrl;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "active")
    private Boolean active;

    public TenantDataSource(String tenantIdentifier, String datasourceUrl, String username, String password, Boolean active) {
        this.tenantIdentifier = tenantIdentifier;
        this.datasourceUrl = datasourceUrl;
        this.username = username;
        this.password = password;
        this.active = active;
    }
}
