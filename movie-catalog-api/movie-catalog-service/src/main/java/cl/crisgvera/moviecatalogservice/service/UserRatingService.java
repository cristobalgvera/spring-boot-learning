package cl.crisgvera.moviecatalogservice.service;

import cl.crisgvera.moviecatalogservice.model.Rating;
import cl.crisgvera.moviecatalogservice.model.rest.UserRating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;

@Service
public class UserRatingService {
    @Autowired
    private WebClient.Builder webClientBuilder;

    // Tells Spring Boot this method should have a circuit breaker and a fallback method to handle a break
    @HystrixCommand(
            fallbackMethod = "getFallbackUserRating",
            // Is possible to set a bunch of useful properties
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")
            },
            // Bulkhead patter implementation
            threadPoolKey = "userRatingServicePool",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "20"),
                    @HystrixProperty(name = "maxQueueSize", value = "10")
            }
    )
    public UserRating getUserRating(String userId) {
        return webClientBuilder.build()
                .get()
                .uri("http://ratings-data-service/ratingsdata/users/" + userId) // 'ratings-data-service' is the name of service on Eureka, check it on application.properties
                .retrieve()
                .bodyToMono(UserRating.class)
                .block();
    }

    public UserRating getFallbackUserRating(String userId) {
        UserRating userRating = new UserRating();
        userRating.setUserRatings(Arrays.asList(
                new Rating("0", 0)
        ));
        return userRating;
    }
}
