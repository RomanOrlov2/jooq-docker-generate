package ru.rorlov

import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy
import org.testcontainers.containers.wait.strategy.WaitStrategy
import org.testcontainers.utility.DockerImageName

import java.time.Duration

class PostgresContainer extends PostgreSQLContainer<PostgresContainer> {

    PostgresContainer(List<String> extensions) {
        super(DockerImageName.parse("sameersbn/postgresql:12-20200524").
                asCompatibleSubstituteFor("postgres"))
        addEnv("DB_USER", username)
        addEnv("DB_PASS", password)
        addEnv("DB_NAME", databaseName)
        addEnv("DB_EXTENSION", extensions.join(","))
    }

    WaitStrategy getWaitStrategy() {
        return new LogMessageWaitStrategy()
                .withRegEx(".*database system is ready to accept connections.*")
                .withTimes(1)
                .withStartupTimeout(Duration.ofSeconds(60))
    }

    void close() {
        stop()
    }
}