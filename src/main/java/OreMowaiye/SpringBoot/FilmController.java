package OreMowaiye.SpringBoot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FilmController {

    private final FilmSearch filmSearch;

    @Autowired
    public FilmController(FilmSearch filmSearch) {
        this.filmSearch = filmSearch;
    }

    @GetMapping("/") // http://localhost:8080/
    public String home() {
        return "index";
    }


    @GetMapping("/search") //http://localhost:8080/search?title=MissJerry
    public String searchFilms(@RequestParam String title, Model model) {

        if (title == null || title.trim().isEmpty()) {
            model.addAttribute("error", "Title cannot be empty");
            return "index";
        }

        List<FilmTitles> results = filmSearch.searchByTitle(title)
                .stream()
                .filter(film -> film.getTitle() != null && !film.getTitle().isEmpty())
                .collect(Collectors.toList());
        model.addAttribute("results", results);
        return "search";
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
