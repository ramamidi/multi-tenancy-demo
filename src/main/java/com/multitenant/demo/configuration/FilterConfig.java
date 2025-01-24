package com.multitenant.demo.configuration;

import com.multitenant.demo.filters.ApplicationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<ApplicationFilter> tenantFilter() {
        FilterRegistrationBean<ApplicationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ApplicationFilter());
        registrationBean.addUrlPatterns("/*"); // Adjust URL patterns as necessary
        return registrationBean;
    }
}
