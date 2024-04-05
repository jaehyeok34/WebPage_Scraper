plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.github.bonigarcia:webdrivermanager:5.7.0")


    implementation("io.github.bonigarcia:webdrivermanager:5.7.0")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("org.seleniumhq.selenium:selenium-java:4.19.1")
}

tasks.test {
    useJUnitPlatform()
}