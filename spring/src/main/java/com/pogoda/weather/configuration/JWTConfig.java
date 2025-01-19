package com.pogoda.weather.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWTConfig {

    @Value("${jwt.token.key}")
    private static String jwtSecretKey;

    @Value("${jwt.token.expiration}")
    private static long jwtExpiration;

    public static String getJwtSecretKey() {
        return jwtSecretKey;
    }

    public static long getJwtExpiration() {
        return jwtExpiration;
    }
}