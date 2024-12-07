plugins {
    id("java")
    id("com.diffplug.spotless") version "7.0.0.BETA4"
}

group = "uk.zootm.interview"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

spotless {
    java {
        palantirJavaFormat()
    }
}

dependencies {
    implementation("com.google.guava:guava:33.3.1-jre")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.26.3")
    testImplementation("org.quicktheories:quicktheories:0.26")
}

tasks.test {
    useJUnitPlatform()
}