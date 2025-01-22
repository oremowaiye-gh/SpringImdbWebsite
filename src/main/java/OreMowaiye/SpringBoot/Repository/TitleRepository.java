package OreMowaiye.SpringBoot.Repository;

import OreMowaiye.SpringBoot.Entity.FilmTitles;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TitleRepository extends CrudRepository<FilmTitles, Long> {

    List<FilmTitles> findByTitleContainingIgnoreCase(String title);
    List<FilmTitles> findByRegion(String region);
    List<FilmTitles> findByLanguage(String language);

}
