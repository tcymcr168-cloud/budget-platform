package com.budgetplatform.security.context;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "budget-platform.auth")
public class AuthProperties {

    private AuthMode mode = AuthMode.DEV_HEADER;
    private boolean allowHeaderRoles = true;
    private String reverseProxyUserHeader = "X-Forwarded-User";

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
}
