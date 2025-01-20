package OreMowaiye.SpringBoot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilmSearch{

    private final TitleRepository titleRepository;

    @Autowired
    public FilmSearch(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }


    public List<FilmTitles> searchByTitle(String title) {
        return titleRepository.findByTitleContainingIgnoreCase(title);

    }

    public List<FilmTitles> searchByRegion(String region) {
        return titleRepository.findByRegion(region);

    }

    public List<FilmTitles> searchByLanguage(String language) {
        return titleRepository.findByLanguage(language);


    }

}
