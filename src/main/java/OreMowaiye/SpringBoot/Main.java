package OreMowaiye.SpringBoot;

import OreMowaiye.SpringBoot.Service.DataLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    private final DataLoaderService dataLoaderService;

    @Autowired
    public Main(DataLoaderService dataLoaderService) {
        this.dataLoaderService = dataLoaderService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}