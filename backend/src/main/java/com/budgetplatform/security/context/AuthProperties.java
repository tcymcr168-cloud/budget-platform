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
    private Jwt jwt = new Jwt();

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

    public Jwt getJwt() {
        return jwt;
    }

    public void setJwt(Jwt jwt) {
        this.jwt = jwt == null ? new Jwt() : jwt;
    }

    public static class Jwt {

        private String issuer;
        private String audience;
        private String jwksUri;
        private String usernameClaim = "sub";
        private long clockSkewSeconds = 60;
        private int maxTokenLength = 8192;
        private List<String> allowedAlgorithms = new ArrayList<>(List.of("RS256"));

        public String getIssuer() {
            return issuer;
        }

        public void setIssuer(String issuer) {
            this.issuer = issuer;
        }

        public String getAudience() {
            return audience;
        }

        public void setAudience(String audience) {
            this.audience = audience;
        }

        public String getJwksUri() {
            return jwksUri;
        }

        public void setJwksUri(String jwksUri) {
            this.jwksUri = jwksUri;
        }

        public String getUsernameClaim() {
            return usernameClaim;
        }

        public void setUsernameClaim(String usernameClaim) {
            this.usernameClaim = usernameClaim;
        }

        public long getClockSkewSeconds() {
            return clockSkewSeconds;
        }

        public void setClockSkewSeconds(long clockSkewSeconds) {
            this.clockSkewSeconds = clockSkewSeconds;
        }

        public int getMaxTokenLength() {
            return maxTokenLength;
        }

        public void setMaxTokenLength(int maxTokenLength) {
            this.maxTokenLength = maxTokenLength;
        }

        public List<String> getAllowedAlgorithms() {
            return allowedAlgorithms;
        }

        public void setAllowedAlgorithms(List<String> allowedAlgorithms) {
            this.allowedAlgorithms = allowedAlgorithms == null ? new ArrayList<>() : allowedAlgorithms;
        }
    }
}
