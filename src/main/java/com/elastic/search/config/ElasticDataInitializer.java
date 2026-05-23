package com.elastic.search.config;

import com.elastic.search.model.Movie;
import com.elastic.search.repository.MovieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ElasticDataInitializer implements CommandLineRunner {

    private final MovieRepository movieRepository;

    public ElasticDataInitializer(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Clear out previous PoC data
        movieRepository.deleteAll();

        // 2. Create and Save documents
        Movie movie1 = new Movie("1", "Interstellar", List.of("Sci-Fi"), 2014, "Explorers travel through a wormhole.");
        Movie movie2 = new Movie("2", "The Dark Knight", List.of("Action"), 2008, "Batman fights the Joker in Gotham.");
        
        movieRepository.save(movie1);
        movieRepository.save(movie2);
        System.out.println(">>> Movies successfully indexed via Spring Data!");

        // 3. Test Full-Text Search Method
        System.out.println("\n>>> Searching for keyword: 'wormhole'...");
        List<Movie> results = movieRepository.findByDescriptionContaining("wormhole");
        
        for (Movie m : results) {
            System.out.println("Found Match: " + m.getTitle() + " - " + m.getDescription());
        }
    }
}