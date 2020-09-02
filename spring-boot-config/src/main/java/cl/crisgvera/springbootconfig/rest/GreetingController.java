package cl.crisgvera.springbootconfig.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @Value("${my.greetings}")
    private String greetingMessage;

    @Value("${app.description}")
    private String appDescription;

    @GetMapping("/greeting")
    public String greeting() {
        return greetingMessage + ", " + appDescription.toLowerCase();
    }
}
