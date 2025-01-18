package OreMowaiye.SpringBoot;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
    public void readAndSaveData() {


        try (Reader reader = new FileReader("src/main/resources/title.akas (1).tsv")) {
            Iterable<CSVRecord> records = CSVFormat.TDF
                    .withFirstRecordAsHeader()
                    .parse(reader);

            int savedRows = 0;

            for (CSVRecord record : records) {

                String titleId = record.get("titleId");
                String title = record.get("title");


                if ((title == null || title.isEmpty()) && (titleId == null || titleId.isEmpty())) {
                    System.out.println("Skipping row with missing title and titleId: " + record);
                    continue;
                }
                int ordering = Integer.parseInt(record.get("ordering"));

                String region = record.get("region");
                String language = record.get("language");
                String types = record.get("types");
                String attributes = record.get("attributes");
                boolean isOriginalTitle = "1".equals(record.get("isOriginalTitle"));  // 1 means true, 0 means false




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
                savedRows++;
                System.out.println("Saved FilmTitle: " + title);

                if (savedRows >= 100) {
                    break;
                }
            }

            System.out.println("Saved " + savedRows + " rows.");

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }
}
