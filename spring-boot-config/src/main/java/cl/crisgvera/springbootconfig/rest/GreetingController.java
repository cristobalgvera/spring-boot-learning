package cl.crisgvera.springbootconfig.rest;

import cl.crisgvera.springbootconfig.configuration.DatabaseConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class GreetingController {

    // Setting value from application.properties
    @Value("${my.greetings}")
    private String greetingMessage;

    // Using a referenced reference
    @Value("${app.description}")
    private String appDescription;

    // Setting default value to a variable
    @Value("${app.nameThatDoesNotExist:This is a default value}")
    private String defaultMessage;

    // Setting static value to a variable
    @Value("Some static stuff, nothing relevant")
    private String staticMessage;

    @Value("${my.list.values}")
    private List<String> propertiesList;

    @Value("#{${db.connection}}") // This is called SpEL expression
    private Map<String, String> databaseValues;

    @Autowired
    private DatabaseConfiguration databaseConfiguration;

    @GetMapping("/greeting")
    public String greeting() {
        String separator = " || ";
        // To show application.properties defined variables
        StringBuilder stringBuilder = new StringBuilder(greetingMessage + ", " + appDescription.toLowerCase() + separator);

        // To show default value variable
        stringBuilder.append("Default message: " + defaultMessage + separator);

        // To show static message
        stringBuilder.append("Static message: " + staticMessage + separator);

        // To show list defined in application.properties
        stringBuilder.append("This will be the list writed in properties: ");
        propertiesList.forEach(value -> stringBuilder.append("- " + value + ". "));
        stringBuilder.append("Number of elements: " + propertiesList.size() + separator);

        //To show map defined on application.properties
        stringBuilder.append("Database values: ");
        databaseValues.forEach((k,v) -> stringBuilder.append("- " + k + ":" + v + " "));

        // Get attributes from DatabaseConfiguration
        stringBuilder.append(separator + "DatabaseConfiguration: ");
        stringBuilder.append(databaseConfiguration.getConnection() + " - Host: " + databaseConfiguration.getHost() + separator);

        return stringBuilder.toString();
    }
}
