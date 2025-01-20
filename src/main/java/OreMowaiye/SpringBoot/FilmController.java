package OreMowaiye.SpringBoot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import java.util.stream.Collectors;


@Controller
public class FilmController {

    private final FilmSearch filmSearch;//    private final Map<String, String> countryToRegionMap;

    @Autowired
    public FilmController(FilmSearch filmSearch) {
        this.filmSearch = filmSearch;
    }

    @GetMapping("/") // http://localhost:8080/
    public String home() {
        return "index";
    }


    @GetMapping("/search") // http://localhost:8080/search?title=MissJerry
    public String searchFilms(@RequestParam(required = false) String title, @RequestParam(required = false) String region, Model model) {
        if ((title == null || title.trim().isEmpty()) && (region == null || region.trim().isEmpty())) {
            model.addAttribute("error", "Title or Region must be provided");
            return "index";
        }

        List<FilmTitles> results = null;

        if (title != null && !title.trim().isEmpty()) {
            results = filmSearch.searchByTitle(title)
                    .stream()
                    .filter(film -> film.getTitle() != null && !film.getTitle().isEmpty())
                    .collect(Collectors.toList());
        }

        if (region != null && !region.trim().isEmpty()) {
            if (results == null) {
                results = filmSearch.searchByRegion(region);
            } else {
                String finalRegion = region;
                results = results.stream()
                        .filter(film -> film.getRegion().equalsIgnoreCase(finalRegion))
                        .collect(Collectors.toList());
            }
        }

        model.addAttribute("results", results);
        model.addAttribute("title", title);
        model.addAttribute("region", region);
        return "search";
    }}