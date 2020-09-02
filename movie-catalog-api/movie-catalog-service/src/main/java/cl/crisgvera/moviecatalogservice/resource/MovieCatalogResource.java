package cl.crisgvera.moviecatalogservice.resource;

import cl.crisgvera.moviecatalogservice.model.*;
import cl.crisgvera.moviecatalogservice.model.rest.UserCatalogItem;
import cl.crisgvera.moviecatalogservice.model.rest.UserRating;
import cl.crisgvera.moviecatalogservice.service.CatalogItemService;
import cl.crisgvera.moviecatalogservice.service.UserRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    // Create services to handle API calls let circuit breaker do separated breaks
    @Autowired
    private CatalogItemService catalogItemService;

    @Autowired
    private UserRatingService userRatingService;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @GetMapping("/{id}")
    public UserCatalogItem getCatalog(@PathVariable("id") String userId) {
        //Each API (service) call has his own fallback method
        UserRating ratings = userRatingService.getUserRating(userId);

        List<CatalogItem> catalogItems = ratings.getUserRatings().stream()
                .map(rating -> catalogItemService.getCatalogItem(rating))
                .collect(Collectors.toList());

        return new UserCatalogItem(catalogItems);
    }

}
