plugins {
    id("org.projectcheckins.micronaut-modules-conventions")
    id("org.projectcheckins.java-warning-errors")
}
dependencies {
    implementation(project(":core"))
    implementation(project(":security"))
    annotationProcessor("io.micronaut.eclipsestore:micronaut-eclipsestore-annotations")
    implementation("io.micronaut.eclipsestore:micronaut-eclipsestore")
    implementation("io.micronaut.eclipsestore:micronaut-eclipsestore-annotations")
    implementation("com.github.ksuid:ksuid:${project.properties["ksuidVersion"]}")
}