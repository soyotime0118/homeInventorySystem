plugins {
    id("java")
    id("org.springframework.boot") version "2.7.12-SNAPSHOT"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
}

group = "me.mason"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {

    implementation("it.ozimov:embedded-redis:0.7.2")
    testImplementation("it.ozimov:embedded-redis:0.7.2")
    implementation("org.redisson:redisson-spring-boot-starter:3.18.0")
    testImplementation("org.redisson:redisson-spring-boot-starter:3.18.0")

    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("com.h2database:h2:2.1.212")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}