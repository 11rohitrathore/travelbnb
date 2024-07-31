package com.travelbnb.payload;

public class JWTTokenDto {

    private String type;

    private String token;

    public String getType() {
        return type;
    }

    public String getToken() {
        return token;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
