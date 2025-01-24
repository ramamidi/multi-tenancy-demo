package com.multitenant.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TenantInfo {
    private String tenantIdentifier;
    private String datasourceUrl;
    private String username;
    private String password;
}
