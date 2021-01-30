package au.com.ibenta.test.service;

public class AuthenticationHealth {

    private String status;

    public AuthenticationHealth() {
    }

    public AuthenticationHealth(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
