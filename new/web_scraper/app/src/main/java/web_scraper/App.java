package web_scraper;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.NonNull;

public class App {

    private final String URL = "https://research.hanbat.ac.kr/ko/projects";

    public static void main(String[] args) {
        // 시작 일자 설정
        LocalDate date = getDate();

        // 스크래핑 시작
        start(date);

        // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // driver.get(url);

        // WebElement list = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#sub-contents > div.bbs-default > ul")));
        // List<WebElement> contents = list.findElements(By.tagName("a"));
        // contents.forEach((x) -> {
        //     System.out.println(x.getText());
        //     System.out.println(x.getDomAttribute("href"));
        // });

        // Thread.sleep(1000);
        // driver.quit();
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

    private static void start(@Nullable LocalDate date) {
        if (date == null) {
            return;
        }

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        Finder finder = getFinder(wait);
        
        // 날짜 비교 → 제목, url 추출
        while (true) {
        }
    }
    
    // By를 인자로 받고, WebElement를 반환하는 함수를 반환하는 함수
    private static @NonNull Finder getFinder(@NonNull WebDriverWait wait) {
        return (by) -> wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }
}

// #sub-contents > div.bbs-default > ul

// 제목 링크 번호
// 2025년 해외우수과학자유치사업(Brain Pool/Brain Pool+) 2025년도 신규과제 공고 예고
// https://www.ntis.go.kr/rndgate/eg/un/ra/view.do?roRndUid=1197886
// 5, 71, 77