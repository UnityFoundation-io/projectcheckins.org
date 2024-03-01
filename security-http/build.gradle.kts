plugins {
    id("org.projectcheckins.micronaut-http-modules-conventions")
}
dependencies {
    api(project(":security"))
    api(project(":bootstrap"))
    testImplementation("io.micronaut.security:micronaut-security-jwt")
    compileOnly("com.fasterxml.jackson.core:jackson-databind")
    testCompileOnly("com.fasterxml.jackson.core:jackson-databind")
}