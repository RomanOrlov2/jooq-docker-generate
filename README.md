# groovy-jooq-docker-codegen

example for configuration:

```groovy
jooqDockerGenerate {
    // required    
    packageName = 'ru.rorlov.stickers.database'
    // not required, '.*' by default 
    includes = '.*'
    // not required
    directory = 'src/main/java'
    // not required, databasechangelog.*|qrtz_.*|uuid_.*' by default, see JooqGenerateExtension
    excludes = 'databasechangelog.*|sport|tag.old_id|playlist.old_id|theme.old_id|rubric.old_id|tag.sport_id'
    // not required
    inputSchema = 'public'
    // not required
    postgresExtensions = ["pg_trgm", "\"uuid-ossp\""]
    // same as generate extension for jooq plugin
    generate {
        sequences = false
        indexes = false
        relations = true
        deprecated = false
        records = true
        immutablePojos = false
        routines = false
        fluentSetters = true
        javaTimeTypes = true
        generatedSerialVersionUID = 'OFF'
    }
}
```

To apply plugin set:

```groovy
plugins {
    id "ru.rorlov.jooq-docker-codegen" version "1.0.0"
}
```

To run call
`./gradlew jooqDockerGenerate`