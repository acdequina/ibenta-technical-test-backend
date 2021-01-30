package au.com.ibenta.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthenticationService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String authenticationHealthUrl = "http://authentication-service.staging.ibenta.com/actuator/health";

    public ResponseEntity<AuthenticationHealth> getHealth() {
        return restTemplate.getForEntity(authenticationHealthUrl, AuthenticationHealth.class);
    }
}
