plugins {
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("org.web_page_scraper.Main")
}


repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.github.bonigarcia:webdrivermanager:5.8.0")

    implementation(files("libs/webdrivermanager-5.8.0-fat.jar"))
    implementation("com.google.guava:guava:31.1-jre")
    implementation("org.seleniumhq.selenium:selenium-java:4.19.1")
}


tasks.test {
    useJUnitPlatform()
}