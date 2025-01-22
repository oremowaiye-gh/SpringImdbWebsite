package OreMowaiye.SpringBoot.Service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final DataLoaderService dataLoaderService;

    public DataLoader(DataLoaderService dataLoaderService) {
        this.dataLoaderService = dataLoaderService;
    }

    @Override
    public void run(String... args) throws Exception {
        dataLoaderService.readAndSaveData();
    }
}