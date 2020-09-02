package cl.crisgvera.moviecatalogservice.service;

import cl.crisgvera.moviecatalogservice.model.CatalogItem;
import cl.crisgvera.moviecatalogservice.model.Movie;
import cl.crisgvera.moviecatalogservice.model.Rating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CatalogItemService {
    @Autowired
    private WebClient.Builder webClientBuilder;

    // Tells Spring Boot this method should have a circuit breaker and a fallback method to handle a break
    @HystrixCommand(
            fallbackMethod = "getFallbackCatalogItem",
            // Bulkhead pattern implementation
            threadPoolKey = "catalogItemServicePool",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "20"),
                    @HystrixProperty(name = "maxQueueSize", value = "10")
            }
    )
    public CatalogItem getCatalogItem(Rating rating) {
        Movie movie = webClientBuilder.build()
                .get() // Request type. Can be post, put, delete, and so on
                .uri("http://movie-info-service/movies/" + rating.getMovieId()) // URI where get the consume
                .retrieve() // Kind a fetch
                .bodyToMono(Movie.class) // It's a promise for when consume is available to populate the class
                .block(); // .block() "stop" method execution and resume it when Movie class instance are populate

        // Put them all together
        return new CatalogItem(movie.getName(), movie.getOverview(), rating.getRating());
    }

    public CatalogItem getFallbackCatalogItem(Rating rating) {
        return new CatalogItem("Movie name not found", "", rating.getRating());
    }
}
