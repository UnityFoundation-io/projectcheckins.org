plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.minimal.application") version "4.3.4"
}
version = "0.1"
group = "org.projectcheckins"
repositories {
        mavenCentral()
}

dependencies {
    implementation(project(":thymeleaf-fieldset"))
    implementation(project(":security-http"))
    implementation(project(":email-http"))
    implementation(project(":http"))
    testImplementation(project(":test-utils"))
    implementation("io.micronaut.security:micronaut-security-session")
    developmentOnly("io.micronaut.security:micronaut-security-jwt")
    runtimeOnly("ch.qos.logback:logback-classic")

    // HTTP Client
    testImplementation("io.micronaut:micronaut-http-client-jdk")

    // AssertJ
    testImplementation("org.assertj:assertj-core")

    // Persistence
    implementation(project(":repository-eclipsestore"))
}
application {
    mainClass.set("org.projectcheckins.netty.Application")
}
java {
    sourceCompatibility = JavaVersion.toVersion("21")
    targetCompatibility = JavaVersion.toVersion("21")
}
micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("org.projectcheckins.*")
    }
}
