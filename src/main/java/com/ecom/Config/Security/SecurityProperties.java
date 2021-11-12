package com.ecom.Config.Security;

public class SecurityProperties {
    public static final String SECRET = "SECRET_KEY";
    public static final long EXPIRATION_TIME = 7_200_000; // 1 hour
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/auth/signup";
    public static final String SWAGGER_URL1 = "/swagger-ui/**";
    public static final String SWAGGER_URL2 = "/webjars/**";
    public static final String SWAGGER_URL3 = "/v2/**";
    public static final String SWAGGER_URL4 = "/swagger-resources/**";
}
