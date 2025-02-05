package web_scraper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JFileChooser;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.NonNull;

public class App {

    private static final String URL = "https://research.hanbat.ac.kr/ko/projects";

    public static void main(String[] args) {
        // 시작 일자 설정
        LocalDate date = getDate();
        if (date == null) {
            System.out.println("잘못된 형식의 입력입니다.");
            return;
        }

        // 저장 경로 설정
        File path = getSavePath();
        if (path == null) {
            System.out.println("잘못된 경로입니다.");
            return;
        }

        // 스크래핑 시작
        List<Post> posts = scraping(date);
        if (posts == null) {
            System.out.println("게시글이 없거나, 문제가 발생했습니다.");
            return;
        }

        // 파일에 저장
        boolean result = save(path, posts);
        if (!result) {
            System.out.println("저장 과정에서 문제가 발생했습니다.");
            return;
        }

        System.out.println("완료했습니다.");
    }

    private static @Nullable LocalDate getDate() {
        System.out.print("시작 일자 6자리 입력(예시: 250101) > ");
        try (Scanner scn = new Scanner(System.in)) {
            try {
                DateTimeFormatter inFmt = DateTimeFormatter.ofPattern("yyMMdd");
                return LocalDate.parse(scn.nextLine(), inFmt);
            } catch (DateTimeParseException e) {
                return null;
            }
        }
    }

    private static @Nullable File getSavePath() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("저장 위치 선택");

        int select = chooser.showSaveDialog(null);
        if (select != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        return chooser.getSelectedFile();
    }

    private static @Nullable List<Post> scraping(@Nonnull LocalDate date) {
        WebDriverManager.chromedriver().setup();

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        Finder finder = getFinder(wait);

        List<Post> posts = new ArrayList<>();

        // 연구 페이지 접속
        driver.get(URL);
        
        for (int page = 1; page < 10; page++) {
            // 유효한 페이지 여부 조사
            try {
                WebElement pageWrap = finder.find(By.cssSelector("#sub-contents > div.page-default > span"));
                pageWrap.findElement(By.tagName("b")); // 유효하지 않은 페이지일 경우 예외 발생
            } catch (NoSuchElementException e) {
                break;
            }

            // 페이지별 10개의 글 존재
            boolean isNext = true;
            for (int j = 1; j <= 10; j++) {
                WebElement colDate = finder.find(By.cssSelector("#sub-contents > div.bbs-default > ul > li:nth-child("+ j + ") > div.col.col-date > span"));
                LocalDate dt = LocalDate.parse(colDate.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                
                // 기준 일자에 부합하지 않을 경우 반복 종료
                if (dt.isBefore(date)) {
                    isNext = false;
                    break;
                }
                
                // 제목, url 추출
                WebElement content = finder.find(By.cssSelector("#sub-contents > div.bbs-default > ul > li:nth-child(" + j + ") > div.col.col-title > span > a"));

                // 작성자 추출
                WebElement author = finder.find(By.cssSelector("#sub-contents > div.bbs-default > ul > li:nth-child(" + j + ") > div.col.col-author > span"));
                Post post = new Post(content.getText(), content.getDomAttribute("href"), author.getText());

                posts.add(post);
            }

            if (!isNext) {
                break;
            }

            // 다음 페이지로 이동
            JavascriptExecutor js = (JavascriptExecutor)driver;
            js.executeScript("pagination(" + (page+1) + ");return false;");
        }

        driver.quit();

        return posts;
    }
    
    private static @NonNull Finder getFinder(@NonNull WebDriverWait wait) {
        return (by) -> wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    private static @NonNull boolean save(@NonNull File path, @NonNull List<Post> posts) {
        try (FileWriter writer = new FileWriter(new File(path, "post.txt"))) {
            for (Post post : posts) {
                String code = Objects.requireNonNullElse(getCode(post.author()), "5, 71, 77\n");

                writer.write(post.title() + "\n");
                writer.write(post.url() + "\n");
                writer.write(code);
                writer.write("\n");
            }

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static @Nullable String getCode(@NonNull String author) {
        Map<String, String> authors = Map.of(
            "해양수산부", "27, 44, 55\n",
            "농촌진흥청", "19, 49, 78\n",
            "보건복지부", "11, 32, 79\n",
            "환경부", "31, 40, 71\n",
            "문화체육관광부", "60, 64, 67\n",
            "과학기술정보통신부", "37, 38, 71\n",
            "산업통상자원부", "45, \n",
            "국토교통부", "45, 51, \n",
            "작성자원자력안전위원회", "34, 43, \n"
        );

        return authors.get(author);
    }
}