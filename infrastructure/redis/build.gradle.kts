dependencies {
    implementation(project(":domain"))
    implementation(project(":shared:common"))

    implementation("org.redisson:redisson-spring-boot-starter:${libs.versions.redisson.get()}")
}
