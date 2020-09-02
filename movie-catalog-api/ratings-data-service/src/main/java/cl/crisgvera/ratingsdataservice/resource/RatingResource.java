package cl.crisgvera.ratingsdataservice.resource;

import cl.crisgvera.ratingsdataservice.model.Rating;
import cl.crisgvera.ratingsdataservice.model.UserRating;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/ratingsdata")
public class RatingResource {
    @GetMapping("/{id}")
    public Rating getRating(@PathVariable("id") String movieId) {
        return new Rating(movieId, 4);
    }

    /*
        Instead of returning List<?> you must create an object to set that List<?>
        on one or more of his parameters.

        The below method return an UserRating object that have a single attribute
        called userRatings that represent a List<Rating>. This will helps to deployment
        and updating without affect API users.

        RESUME: DON'T USE LISTs AS RETURN OBJECT NEVER! ALWAYS USE MODEL OBJECTS
     */
    @GetMapping("/users/{userId}")
    public UserRating getUserRating(@PathVariable("userId") String userId) {
        List<Rating> ratings = Arrays.asList(
                new Rating("120", 4),
                new Rating("35", 3)
        );
        return new UserRating(ratings);
    }
}
