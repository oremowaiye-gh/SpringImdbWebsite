package OreMowaiye.SpringBoot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final Main main;


    public DataLoader(Main main) {
        this.main = main;

    }

    @Override
    public void run(String... args) throws Exception {
        main.readAndSaveData();
    }
}
