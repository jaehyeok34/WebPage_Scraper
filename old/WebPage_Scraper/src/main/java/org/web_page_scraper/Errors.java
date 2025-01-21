package org.web_page_scraper;

public class Errors {
    public static void printNotFoundTagMessage() {
        System.err.println("Tag Error: Tag not found");
    }

    public static void printTimeOutMessage() {
        System.err.println("TimeOut Error: HTML element not found");
    }

    public static void printNotFoundDirectoryMessage() {
        System.err.println("Directory Error: Directory not found");
        System.err.println("Saved to current directory");
    }

    public static void printRunTimeErrorMessage() {
        System.err.println("RunTime Error: Null");
    }

    public static void printInputErrorMessage() {
        System.out.println("Input Error: please format correctly");
    }
}
