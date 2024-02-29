plugins {
    id("org.projectcheckins.micronaut-http-modules-conventions")
    id("org.projectcheckins.java-warning-errors")
}
dependencies {
    api(project(":security"))
}