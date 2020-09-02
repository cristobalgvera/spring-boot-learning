package cl.crisgvera.movieinfoservice.resource;

import cl.crisgvera.movieinfoservice.model.Movie;
import cl.crisgvera.movieinfoservice.model.MovieSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/movies")
public class MovieResource {

    @Value(value = "${api.key}")
    private String apiKey;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @GetMapping("/{id}")
    public Movie getMovieInfo(@PathVariable("id") String movieId) {
        MovieSummary movieSummary = webClientBuilder.build()
                .get()
//                https://api.themoviedb.org/3/movie/2?api_key=5f3acab7680e1e56beeb058a3b702dd5
                .uri("https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey)
                .retrieve()
                .bodyToMono(MovieSummary.class)
                .block();
        return new Movie(movieId, movieSummary.getTitle(), movieSummary.getOverview());
    }
}
