package OreMowaiye.SpringBoot;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Main {

    @Autowired
    private TitleRepository titleRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Transactional
    public void readAndSaveData() {
        int recordLimit = 100; //6000000
        int processedCount = 0;

        try (Reader reader = new FileReader("src/main/resources/title.clean.tsv");
             CSVParser csvParser = new CSVParser(reader, CSVFormat.MONGODB_TSV
                     .withFirstRecordAsHeader()
                     .withQuote(null)
                     .withEscape('\\'))) {

            List<FilmTitles> filmTitlesList = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                if (processedCount >= recordLimit) {
                    break;
                }

                if (record.size() < 8) {
                    System.out.println("Skipping row with wrong number of columns: " + record);
                    continue;
                }
                String titleId = record.get("titleId");
                String title = record.get("title");
                if (title == null || title.contains("\"") || title.length() <= 1 || title.equals("N/A")) {
                    System.out.println("Skipping row with empty title, too short title: " + record);
                    continue;
                }

                if (titleId == null || titleId.isEmpty()) {
                    System.out.println("Skipping row with missing titleId: " + record);
                    continue;
                }

                if (title.length() > 255) {
                    title = title.substring(0, 255);
                }

                String orderingStr = record.get("ordering");
                int ordering = "N/A".equalsIgnoreCase(orderingStr) ? 0 : Integer.parseInt(orderingStr);

                String region = record.get("region");
                String language = record.get("language");
                String types = record.get("types");
                String attributes = record.get("attributes");
                boolean isOriginalTitle = "1".equals(record.get("isOriginalTitle"));

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

                filmTitlesList.add(filmTitle);
                processedCount++;
            }

            batchInsert(filmTitlesList);
            System.out.println("Saved " + filmTitlesList.size() + " rows.");
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing ordering field: " + e.getMessage());
        }
    }

    @Transactional
    public void batchInsert(List<FilmTitles> filmTitlesList) {
        String sql = "INSERT INTO film_titles (title_id, ordering, title, region, language, types, attributes, is_original_title) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, filmTitlesList, 1000, (ps, filmTitle) -> {
            ps.setString(1, filmTitle.getTitleId());
            ps.setInt(2, filmTitle.getOrdering());
            ps.setString(3, filmTitle.getTitle());
            ps.setString(4, filmTitle.getRegion());
            ps.setString(5, filmTitle.getLanguage());
            ps.setString(6, filmTitle.getTypes());
            ps.setString(7, filmTitle.getAttributes());
            ps.setBoolean(8, filmTitle.isOriginalTitle());
        });
    }
}