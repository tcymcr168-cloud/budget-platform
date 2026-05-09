package com.budgetplatform.security.context;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "budget-platform.auth")
public class AuthProperties {

    private AuthMode mode = AuthMode.DEV_HEADER;
    private boolean allowHeaderRoles = false;
    private String reverseProxyUserHeader = "X-Forwarded-User";
    private List<String> bootstrapAdminUsers = new ArrayList<>();

    public AuthMode getMode() {
        return mode;
    }

    public void setMode(AuthMode mode) {
        this.mode = mode;
    }

    public boolean isAllowHeaderRoles() {
        return allowHeaderRoles;
    }

    public void setAllowHeaderRoles(boolean allowHeaderRoles) {
        this.allowHeaderRoles = allowHeaderRoles;
    }

    public String getReverseProxyUserHeader() {
        return reverseProxyUserHeader;
    }

    public void setReverseProxyUserHeader(String reverseProxyUserHeader) {
        this.reverseProxyUserHeader = reverseProxyUserHeader;
    }

    public List<String> getBootstrapAdminUsers() {
        return bootstrapAdminUsers;
    }

    public void setBootstrapAdminUsers(List<String> bootstrapAdminUsers) {
        this.bootstrapAdminUsers = bootstrapAdminUsers == null ? new ArrayList<>() : bootstrapAdminUsers;
    }
}
