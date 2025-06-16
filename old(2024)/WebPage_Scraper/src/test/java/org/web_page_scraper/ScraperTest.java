package org.web_page_scraper;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

class ScraperTest {
    WebDriver driver;
    WebDriverWait wait;
    Scraper scraper;

    Scraper.Path path;

    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();
        driver.get("https://research.hanbat.ac.kr/pageview/page/posts");
        wait = new WebDriverWait(
                driver, Duration.of(10, ChronoUnit.SECONDS)
        );

        scraper = new Scraper();
        path = new Scraper.Path(".");
    }

    @AfterEach
    void release() {
        driver.quit();
    }

    @Test
    void save() {
        path = new Scraper.Path("askldfh");
        scraper.save(path, scraper.getPostsAfterDate(wait, LocalDate.parse("2024-04-02")));
    }

    @Test
    void getPostsAfterDate() {
        LocalDate date = LocalDate.parse("2024-04-02");
        scraper.getPostsAfterDate(wait, date).forEach(x ->
                Assertions.assertTrue(x.getDate().compareTo(date) > -1));
    }

    @Test
    void turnNextPage() {
        Assertions.assertDoesNotThrow(() -> {
            Thread.sleep(1000);
            scraper.turnNextPage(wait);
        });
    }

    @Test
    void getPosts() {
        for (int i = 0; i < 10; i++) {
            List<Post> posts = scraper.getPosts(wait);
            posts.forEach(post -> {
                Assertions.assertInstanceOf(Post.class, post);
                Assertions.assertNotNull(post.getTitle());
                Assertions.assertNotNull(post.getHref());
                Assertions.assertNotNull(post.getDate());
            });
            scraper.turnNextPage(wait);
        }
    }
}