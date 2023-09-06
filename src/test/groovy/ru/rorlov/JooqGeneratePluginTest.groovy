package ru.rorlov

import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertTrue

class JooqGeneratePluginTest {

    @Test
    void generatorTest() {
        def project = ProjectBuilder.builder().build()
        project.pluginManager.apply("ru.sport24.internal.jooq-docker-codegen")
        def generateJooqTask = project.tasks.getByName("jooqDockerGenerate")
        assertTrue(generateJooqTask instanceof JooqGenerateTask)
    }
}
