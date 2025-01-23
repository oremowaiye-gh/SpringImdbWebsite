package OreMowaiye.SpringBoot.Controller;

import OreMowaiye.SpringBoot.Service.FilmSearch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

public class FilmControllerTest {

    @Mock
    private FilmSearch filmSearch;

    @Mock
    private Model model;

    @Mock
    private SessionStatus sessionStatus;

    @InjectMocks
    private FilmController filmController;
    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(filmController).build();
    }

    @Test
    void DatabaseConnectionTest() {
        filmController.searchFilms("MissJerry", null, model, sessionStatus);
        verify(filmSearch).searchByTitle("MissJerry");
    }

    @Test
    void HomePageTest() {
        String viewName = filmController.home();
        assertEquals("index", viewName);
    }

    @Test
    void NoTitleOrRegionProvided() {
        String viewName = filmController.searchFilms(null, null, model, sessionStatus);

        assertEquals("search", viewName);
        verify(model).addAttribute("error", "Title must be provided");
    }

    @Test
    void SearchByTitle() {
        String title = "MissJerry";
        String region = null;

        String viewName = filmController.searchFilms(title, region, model, sessionStatus);

        assertEquals("search", viewName);
        verify(model).addAttribute("title", title);
    }

}



