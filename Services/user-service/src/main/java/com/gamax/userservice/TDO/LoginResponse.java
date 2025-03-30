package com.gamax.userservice.TDO;

public class LoginResponse {
    private String token;
    private long expiresIn;

    public String getToken() {
        return token;
    }

    public LoginResponse setToken(String token) {  // ✅ Setter en mode Fluent API
        this.token = token;
        return this;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public LoginResponse setExpiresIn(long expiresIn) {  // ✅ Setter en mode Fluent API
        this.expiresIn = expiresIn;
        return this;
    }
}
