package ru.rorlov

import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.DirectoryResourceAccessor
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.*
import org.postgresql.ds.PGSimpleDataSource

class JooqGenerateTask extends DefaultTask {

    @Internal
    final JooqGenerateExtension extension = project.extensions.getByType(JooqGenerateExtension.class)

    @TaskAction
    def execute() {
        def postgres = new PostgresContainer(extension.postgresExtensions)
        try {
            println("Starting testcontainers postgres image")
            postgres.start()
            println("Postgres started")
            def pgSimpleDataSource = new PGSimpleDataSource()
            pgSimpleDataSource.user = postgres.username
            pgSimpleDataSource.password = postgres.password
            pgSimpleDataSource.setURL(postgres.jdbcUrl)
            def liquibase = new Liquibase(
                    'db/changelog/db.changelog-master.yml',
                    new DirectoryResourceAccessor(project.file("src/main/resources")),
                    new JdbcConnection(pgSimpleDataSource.connection),
            )
            println("Starting liquibase migration")
            liquibase.update()
            println("Liquibase migration complete")
            println("Starting jooq code generation")
            generateJooqClasses(pgSimpleDataSource)
            println("Jooq code generation complete")
        } finally {
            postgres.stop()
            println("Postgres container successfully teared down")
        }
    }

    private void generateJooqClasses(PGSimpleDataSource postgres) {
        new GenerationTool()
                .run(new Configuration()
                        .withLogging(Logging.DEBUG)
                        .withJdbc(new Jdbc()
                                .withDriver('org.postgresql.Driver')
                                .withUrl(postgres.url)
                                .withUser(postgres.user)
                                .withPassword(postgres.password)
                        )
                        .withGenerator(createGenerator()))
    }

    private Generator createGenerator() {
        return new Generator()
                .withDatabase(
                        new Database()
                                .withIncludeExcludeColumns(true)
                                .withName('org.jooq.meta.postgres.PostgresDatabase')
                                .withInputSchema(extension.inputSchema)
                                .withIncludes(extension.includes)
                                .withExcludes(extension.excludes)

                )
                .withGenerate(extension.generate)
                .withTarget(new Target()
                        .withPackageName(extension.packageName)
                        .withDirectory(project.layout.projectDirectory.dir(extension.directory).toString())
                        .withClean(true))
    }
}
