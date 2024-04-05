package org.web_page_scraper;

import java.time.LocalDate;

public class Post {
    private final String title;
    private final String href;
    private final LocalDate date;

    public Post(String title, String href, LocalDate date) {
        this.title = title;
        this.href = href;
        this.date = date;
    }

    public Post(String title, String href, String date) {
        this(title, href, LocalDate.parse(date));
    }

    public String getTitle() {
        return this.title;
    }

    public String getHref() {
        return this.href;
    }

    public LocalDate getDate() {
        return this.date;
    }
}
