tasks.withType<JavaCompile> {
    val compilerArgs = options.compilerArgs
    compilerArgs.add("-Werror")
    compilerArgs.add("-Xlint:all,-serial,-processing")
}