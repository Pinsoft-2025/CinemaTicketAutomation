package com.example.CinemaTicketAutomation.service.Impl;

import com.example.CinemaTicketAutomation.entity.Movie;
import com.example.CinemaTicketAutomation.entity.enums.Country;
import com.example.CinemaTicketAutomation.repository.MovieRepository;
import com.example.CinemaTicketAutomation.service.MovieService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Transactional
    @Override
    public Movie addMovie(Movie movie, MultipartFile posterFile) {

        //set poster as base64
        if (posterFile != null && !posterFile.isEmpty()) {
            movie.setPoster(encodeImageToBase64(posterFile));
        }
        return movieRepository.save(movie);
    }

    @Transactional
    @Override
    public Movie updateMovie(Movie movie, MultipartFile posterFile) {
        Movie oldMovie = movieRepository.findById(movie.getId()).orElseThrow(() -> new EntityNotFoundException("Movie not found"));

        //set new poster as base64
        if (posterFile != null && !posterFile.isEmpty()) {
            oldMovie.setPoster(encodeImageToBase64(posterFile));
        }

        oldMovie.setTitle(movie.getTitle());
        oldMovie.setDescription(movie.getDescription());
        oldMovie.setTrailerURI(movie.getTrailerURI());
        oldMovie.setDurationMin(movie.getDurationMin());
        oldMovie.setGenres(movie.getGenres());
        oldMovie.setDirector(movie.getDirector());
        oldMovie.setCountry(movie.getCountry());
        oldMovie.setReleaseDate(movie.getReleaseDate());
        oldMovie.setWarnings(movie.getWarnings());

        return movieRepository.save(oldMovie);
    }

    @Transactional
    @Override
    public void deleteMovie(long movieId) {
        if (movieRepository.existsById(movieId)) {
            movieRepository.deleteById(movieId);
        } else {
            throw new EntityNotFoundException("Movie not found");
        }
    }

    @Override
    public Movie getMovie(long movieId) {
        return movieRepository.findById(movieId).orElseThrow(() -> new EntityNotFoundException("Movie not found"));
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public List<Movie> getAllMoviesByGenre(String genre) {
        return movieRepository.findByGenresContaining(genre);
    }

    @Override
    public List<Movie> getAllMoviesByDirector(String director) {
        return movieRepository.findByDirector(director);
    }

    @Override
    public List<Movie> getAllMoviesByCountry(Country country) {
        return movieRepository.findByCountry(country);
    }

    //keyword should either be in the title or description
    @Override
    public List<Movie> searchMovie(String keyword) {
        return movieRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
    }



    //IMAGE METHOD
    //transforms image bytes to string
    private String encodeImageToBase64(MultipartFile imageFile) {
        try {
            return Base64.getEncoder().encodeToString(imageFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error encoding image to Base64", e);
        }
    }




    /*
    ************************* trying to combine all filters *********************
     @Override
    public List<Movie> searchMovies(String genre, String director, Country country, String keyword) {
        // Use Spring Data JPA Specifications to build a dynamic query
        return movieRepository.findAll((Specification<Movie>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (genre != null && !genre.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("genre"), genre));
            }

            if (director != null && !director.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("director"), director));
            }

            if (country != null) {
                predicates.add(criteriaBuilder.equal(root.get("country"), country));
            }

            if (keyword != null && !keyword.isEmpty()) {
                String likePattern = "%" + keyword + "%";
                Predicate titlePredicate = criteriaBuilder.like(root.get("title"), likePattern);
                Predicate descriptionPredicate = criteriaBuilder.like(root.get("description"), likePattern);
                predicates.add(criteriaBuilder.or(titlePredicate, descriptionPredicate));
            }

            // If no criteria specified, return all movies
            if (predicates.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            // Combine all predicates with AND operator
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
     */
}
