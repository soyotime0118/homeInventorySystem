plugins {
    id("java")
    id("org.springframework.boot") version "2.7.12-SNAPSHOT"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
}

group = "me.mason"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21

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

val MYSQL_CONNECTOR_VERSION = "8.0.33"
val H2_VERSION = "2.1.212"
val SPRINGDOC_OPENAPI_UI_VERSION = "1.8.0"


dependencies {
    implementation("mysql:mysql-connector-java:$MYSQL_CONNECTOR_VERSION")
    implementation("com.h2database:h2:$H2_VERSION")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-ui:$SPRINGDOC_OPENAPI_UI_VERSION")
    implementation("org.postgresql:postgresql")
    compileOnly("org.projectlombok:lombok:latest.release")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.projectlombok:lombok:latest.release")
    testCompileOnly("org.projectlombok:lombok:latest.release")
    testAnnotationProcessor("org.projectlombok:lombok:latest.release")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}