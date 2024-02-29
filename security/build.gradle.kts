plugins {
    id("org.projectcheckins.micronaut-modules-conventions")
    id("org.projectcheckins.java-warning-errors")
}

dependencies {
    // Micronaut Security
    api("io.micronaut.security:micronaut-security")
    implementation("io.micronaut.reactor:micronaut-reactor")
    // Security Crypto
    implementation("org.springframework.security:spring-security-crypto:${project.properties["springSecurityCryptoVersion"]}")
    implementation("org.slf4j:jcl-over-slf4j")
}