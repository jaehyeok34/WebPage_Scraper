package org.web_page_scraper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Scraper {
    public static class Path {
        private static final String FILE_NAME = "post_info.txt";
        private String path;

        public Path(@Nonnull String dir) {
            try {
                if (dir.substring(dir.length() - 1).compareTo("/") != 0) {
                    dir += "/";
                }
                this.path = dir + Path.FILE_NAME;
            } catch (NullPointerException e) {
                this.path = "./" + Path.FILE_NAME;
            }
        }

        public String get() {
            return this.path;
        }
    }

    protected void start(LocalDate date, Path savePath) {
        WebDriverManager.chromedriver().setup();
        final WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://research.hanbat.ac.kr/pageview/page/posts");
            WebDriverWait wait = new WebDriverWait(
                    driver,
                    Duration.of(3, ChronoUnit.SECONDS)
            );

            this.save(savePath, this.getPostsAfterDate(wait, date));
        } catch (NullPointerException e) {
            Errors.printRunTimeErrorMessage();
        } finally {
            driver.quit();
        }
    }

    protected void save(Path path, List<Post> posts) {
        while (true) {
            try (final BufferedWriter writer = new BufferedWriter(
                    new FileWriter(path.get(), true)
            )) {
                for (Post post : posts) {
                    writer.write(post.getTitle() + "\n");
                    writer.write(post.getHref() + "\n");
                    writer.write("\n".repeat(3));
                }

                return;
            } catch (IOException e) {
                Errors.printNotFoundDirectoryMessage();
                path = new Path(".");
            }
        }
    }

    protected List<Post> getPostsAfterDate(WebDriverWait wait, LocalDate date) {
        ArrayList<Post> posts = new ArrayList<>();
        final int MAXIMUM = 10;
        for (int i = 0; i < MAXIMUM; i++) {
            boolean isBreak = false;
            for (Post post : this.getPosts(wait)) {
                if (post.getDate().isBefore(date)) {
                    isBreak = true;
                    break;
                }

                posts.add(post);
            }

            if (isBreak) {
                break;
            }

            this.turnNextPage(wait);
        }

        return posts;
    }

    protected void turnNextPage(WebDriverWait wait) throws TimeoutException, StaleElementReferenceException {
        // click() -> StaleElementReferenceException
        // element가 DOM에 대해 참조를 잃을 경우(속도 문제)
        wait.until(
                ExpectedConditions
                        .visibilityOfElementLocated(By.id("boardListTable_next"))
        ).click();
    }

    protected List<Post> getPosts(WebDriverWait wait) throws TimeoutException {
        ArrayList<Post> posts = new ArrayList<>();
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".odd, .even")))
                .forEach(element -> {
                    try {
                        String title = element.findElement(By.className("tdWrap")).getText();
                        String href = Optional.ofNullable(element.findElement(By.tagName("a")).getAttribute("href")).orElse("");
                        String date = element.findElement(By.className("sorting_1")).getText();
                        posts.add(new Post(title, href, date));
                    } catch (NoSuchElementException e) {
                        Errors.printNotFoundTagMessage();
                    }
                });

        return posts;
    }
}
