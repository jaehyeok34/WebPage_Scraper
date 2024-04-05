package org.web_page_scraper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class MainTest {
    Scraper scraper;

    @BeforeEach
    void setup() {
        scraper = new Scraper();
    }

    @Test
    void start() {
        scraper.start(LocalDate.parse("2024-04-02"), new Scraper.Path("."));
    }
}
