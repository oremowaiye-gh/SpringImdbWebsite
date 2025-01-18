import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class DataCleaner{

    private static final String PROGRESS_FILE_PATH = "src/main/resources/progress.txt";

    public static void main(String[] args) {
        String inputFilePath = "src/main/resources/title.akas (1).tsv";
        String outputFilePath = "src/main/resources/title.clean.tsv";

        try {
            cleanLargeTSVData(inputFilePath, outputFilePath);
        } catch (IOException e) {
            System.err.println("Error cleaning the TSV data: " + e.getMessage());
        }
    }

    public static void cleanLargeTSVData(String inputFilePath, String outputFilePath) throws IOException {
        long lastProcessedLine = readLastProcessedLine();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFilePath), StandardCharsets.UTF_8));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath, true))) {

            CSVParser csvParser = new CSVParser(reader, CSVFormat.TDF.withFirstRecordAsHeader()
                    .withIgnoreEmptyLines()
                    .withAllowMissingColumnNames()
                    .withRecordSeparator("\n"));

            if (lastProcessedLine == 0) {
                writeHeaders(writer, csvParser.getHeaderNames());
            }

            List<CSVRecord> records = csvParser.getRecords();
            List<List<String>> cleanedRecords = (new CleanRecordsTask(records, csvParser.getHeaderNames(), lastProcessedLine)).compute();

            for (List<String> cleanedRow : cleanedRecords) {
                writer.write(String.join("\t", cleanedRow) + "\n");
            }

            System.out.println("Data cleaning completed successfully.");
        } catch (IOException e) {
            System.err.println("Error reading or writing to the file: " + e.getMessage());
            throw e;
        }
    }

    private static void writeHeaders(BufferedWriter writer, List<String> headers) throws IOException {
        writer.write(String.join("\t", headers) + "\n");
    }

    private static long readLastProcessedLine() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PROGRESS_FILE_PATH))) {
            String line = reader.readLine();
            return line != null ? Long.parseLong(line) : 0;
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading progress file: " + e.getMessage());
            return 0;
        }
    }

    private static class CleanRecordsTask extends RecursiveTask<List<List<String>>> {
        private static final int THRESHOLD = 1000;
        private final List<CSVRecord> records;
        private final List<String> headers;
        private final long lastProcessedLine;

        public CleanRecordsTask(List<CSVRecord> records, List<String> headers, long lastProcessedLine) {
            this.records = records;
            this.headers = headers;
            this.lastProcessedLine = lastProcessedLine;
        }

        @Override
        protected List<List<String>> compute() {
            if (records.size() <= THRESHOLD) {
                return cleanRecords(records, headers, lastProcessedLine);
            } else {
                int mid = records.size() / 2;
                CleanRecordsTask leftTask = new CleanRecordsTask(records.subList(0, mid), headers, lastProcessedLine);
                CleanRecordsTask rightTask = new CleanRecordsTask(records.subList(mid, records.size()), headers, lastProcessedLine);

                invokeAll(leftTask, rightTask);

                List<List<String>> leftResult = leftTask.join();
                List<List<String>> rightResult = rightTask.join();

                List<List<String>> result = new ArrayList<>(leftResult);
                result.addAll(rightResult);
                return result;
            }
        }

        private List<List<String>> cleanRecords(List<CSVRecord> records, List<String> headers, long lastProcessedLine) {
            List<List<String>> cleanedRecords = new ArrayList<>();
            long currentLine = 0;

            for (CSVRecord record : records) {
                currentLine++;
                if (currentLine <= lastProcessedLine) {
                    continue;
                }

                if (isMalformedRecord(record, headers)) {
                    System.out.println("Skipping malformed row: " + record);
                    continue;
                }

                List<String> cleanedRow = cleanRecord(record, headers);
                if (cleanedRow != null) {
                    cleanedRecords.add(cleanedRow);
                }
            }

            return cleanedRecords;
        }

        private boolean isMalformedRecord(CSVRecord record, List<String> headers) {
            return record.size() != headers.size();
        }

        private List<String> cleanRecord(CSVRecord record, List<String> headers) {
            List<String> cleanedRow = new ArrayList<>();

            for (String header : headers) {
                try {
                    String value = record.get(header);
                    value = cleanString(value);

                    if (value == null || value.isEmpty()) {
                        value = "N/A";
                    }

                    cleanedRow.add(value);
                } catch (Exception e) {
                    System.err.println("Error cleaning column '" + header + "' with value: " + record.get(header));
                    cleanedRow.add("Error");
                }
            }

            return cleanedRow;
        }

        private String cleanString(String input) {
            if (input != null) {
                input = input.replaceAll("^\"|\"$", "");
                input = input.replaceAll("\"", "");
                input = input.replaceAll("\\\\N", "N/A");
                input = input.replaceAll("[^\\x20-\\x7E]", "");
                input = input.trim();
            }
            return input;
        }
    }
}