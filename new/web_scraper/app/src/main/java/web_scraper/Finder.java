package web_scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public interface Finder {
    WebElement find(By by);
}
