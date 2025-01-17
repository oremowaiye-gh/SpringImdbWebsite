package OreMowaiye.SpringBoot;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;


@SpringBootApplication
public class Main {

    @Autowired
    private TitleRepository titleRepository;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

    }

    public void readAndSaveData() {


        try (Reader reader = new FileReader("src/main/resources/title.akas (1).tsv")) {
            Iterable<CSVRecord> records = CSVFormat.TDF
                    .withFirstRecordAsHeader()
                    .parse(reader);

            int processedRows = 0;

            for (CSVRecord record : records) {
                String titleId = record.get("titleId");
                int ordering = Integer.parseInt(record.get("ordering"));
                String title = record.get("title");
                String region = record.get("region");
                String language = record.get("language");
                String types = record.get("types");
                String attributes = record.get("attributes");
                boolean isOriginalTitle = "1".equals(record.get("isOriginalTitle"));  // 1 means true, 0 means false


                if (!titleRepository.existsById(titleId)) {
                    FilmTitles filmTitle = new FilmTitles(
                            titleId,
                            ordering,
                            title,
                            region,
                            language,
                            types,
                            attributes,
                            isOriginalTitle
                    );

                    titleRepository.save(filmTitle);
                    processedRows++;
                } else {
                    System.out.println("Skipping duplicate entry for id: " + titleId);
                }

                if (processedRows >= 100) {
                    break;
                }
            }

            System.out.println("Processed " + processedRows + " rows.");

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }
}
