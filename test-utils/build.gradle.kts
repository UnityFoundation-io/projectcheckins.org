plugins {
    id("org.projectcheckins.micronaut-modules-conventions")
}
dependencies {
    // AssertJ
    api("org.assertj:assertj-core")
    api("io.micronaut:micronaut-http-client-core")
    implementation("io.micronaut.reactor:micronaut-reactor");
    implementation("io.micronaut.security:micronaut-security");
}