package com.g02.handyShare;

import org.springframework.stereotype.Component;


import lombok.NoArgsConstructor;


@NoArgsConstructor
@Component
public class Constants {

    public final String SECRET = "AFSDVS";
    public final String FRONT_END_HOST="http://172.17.0.99:3000";
    public final String SERVER_URL = "http://172.17.0.99:8080";

    public String getSecret() {
        return SECRET;
    }

    public String getFrontEndHost() {
        return FRONT_END_HOST;
    }

    public String getServerUrl() {
        return SERVER_URL;
    }

    public String getSERVER_URL() {
        return "http://172.17.0.99:8080";
    }
}
