package com.multitenant.demo.filters;

import com.multitenant.demo.ApplicationThread;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class ApplicationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String universityId = httpServletRequest.getHeader("X-TenantId"); // Or extract from subdomain, etc.
        ApplicationThread.setCurrentTenant(universityId);
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            ApplicationThread.clear();
        }
    }
}
