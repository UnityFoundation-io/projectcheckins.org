plugins {
    id("org.projectcheckins.micronaut-http-modules-conventions")
    id("org.projectcheckins.java-warning-errors")
}
dependencies {
    api(project(":core"))

    // OpenAPI https://micronaut-projects.github.io/micronaut-openapi/latest/guide/
    annotationProcessor("io.micronaut.openapi:micronaut-openapi")
    compileOnly("io.micronaut.openapi:micronaut-openapi-annotations")
}