package org.web_page_scraper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        run();
    }

    private static void run() throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        Scraper scraper = new Scraper();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                try {
                    System.out.print("input start date(" +
                            LocalDate.now().format(formatter) + ") > "
                    );
                    LocalDate date = LocalDate.parse(reader.readLine(), formatter);
                    System.out.print("input save path > ");
                    Scraper.Path path = new Scraper.Path(Objects.requireNonNull(reader.readLine()));

                    scraper.start(date, path);
                    return;
                } catch (DateTimeParseException e) {
                    Errors.printInputErrorMessage();
                }
            }
        }
    }
}