package ru.rorlov

import org.jooq.meta.jaxb.Generate

class JooqGenerateExtension {
    String packageName
    String directory = "src/main/java"
    String includes = '.*'
    String excludes = 'databasechangelog.*|qrtz_.*|uuid_.*'
    String inputSchema = 'public'
    // List required pg extensions
    List<String> postgresExtensions = []
    Generate generate = new Generate()

    void setGenerate(final Closure closure) {
        closure.setDelegate(generate)
        closure.call()
    }
}
