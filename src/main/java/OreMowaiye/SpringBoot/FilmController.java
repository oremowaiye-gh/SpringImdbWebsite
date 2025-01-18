package OreMowaiye.SpringBoot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FilmController {

    private final FilmSearch filmSearch;

    @Autowired
    public FilmController(FilmSearch filmSearch) {
        this.filmSearch = filmSearch;
    }


    @GetMapping("/search") //http://localhost:8080/search?title=MissJerry
    public List<FilmTitles> searchFilms(@RequestParam String title) {
        return filmSearch.searchByTitle(title);
    }

    @GetMapping("/region") //http://localhost:8080/region?region=US
    public List<FilmTitles> searchFilmsByRegion(@RequestParam String region) {
        return filmSearch.searchByRegion(region);
    }


    @GetMapping("/language")// http://localhost:8080/language?language=ja  (japanese)
    public List<FilmTitles> searchFilmsByLanguage(@RequestParam String language) {
        return filmSearch.searchByLanguage(language);
    }
}
