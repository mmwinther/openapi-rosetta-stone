package nz.winther.dev

import io.github.microcks.testcontainers.MicrocksContainer
import io.micronaut.test.support.TestPropertyProvider
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.slf4j.LoggerFactory
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.shaded.org.yaml.snakeyaml.Yaml
import java.io.File
import java.net.URI
import java.net.URLEncoder

/**
 * Microcks test container
 *
 * Inherit from this class to start a Testcontainer with Microcks to Mock a specific external service based on the
 * service's OpenAPI specification.
 *
 * @property micronautServiceName
 * @property openApiDefinitionFile
 * @constructor Create Microcks service mock for a specific service
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class MicrocksServiceMock(
    private val micronautServiceName: String,
    private val openApiDefinitionFile: String,
) : TestPropertyProvider {
    private val logger = LoggerFactory.getLogger(MicrocksServiceMock::class.java)

    private var microcksContainer: MicrocksContainer =
        MicrocksContainer("quay.io/microcks/microcks-uber:1.11.2")
            .withAccessToHost(true)

    protected var serviceUrl: String? = null
    protected var servicePath: String? = null

    @BeforeAll
    fun configureMicrocksService() {
        val logConsumer =
            Slf4jLogConsumer(
                LoggerFactory.getLogger("io.github.microcks.testcontainers.MicrocksTestContainer"),
            )
        microcksContainer.followOutput(logConsumer)
        microcksContainer.importAsMainArtifact(
            this.javaClass
                .classLoader
                .getResource(openApiDefinitionFile)
                ?.toURI()
                ?.let {
                    File(
                        it,
                    )
                },
        )
    }

    @AfterAll
    fun stopMicrocksService() = microcksContainer.stop()

    /**
     * Configure location of the mocked service.
     *
     * Start the Microcks container, obtain the endpoint of the mocked service and configure the specified Micronaut
     * service to run against the mocked service.
     *
     * @return a map of the necessary property keys and values.
     */
    override fun getProperties(): MutableMap<String, String> {
        val yaml = Yaml()
        val inputStream =
            this.javaClass
                .classLoader
                .getResourceAsStream(openApiDefinitionFile)
        val openApiDefinition = yaml.load<Map<String, Any>>(inputStream)
        microcksContainer.start()
        val mockUrl =
            URI(
                microcksContainer.getRestMockEndpoint(
                    URLEncoder.encode(
                        (openApiDefinition["info"] as Map<*, *>)["title"] as String,
                        "UTF-8",
                    ),
                    (openApiDefinition["info"] as Map<*, *>)["version"] as String,
                ),
            ).toURL()
        logger.info(
            "Configuring $micronautServiceName client at URL: ${microcksContainer.httpEndpoint} Path: ${mockUrl.path}",
        )
        serviceUrl = microcksContainer.httpEndpoint
        servicePath = mockUrl.path
        return mutableMapOf(
            "micronaut.http.services.$micronautServiceName.url" to microcksContainer.httpEndpoint,
            "micronaut.http.services.$micronautServiceName.path" to mockUrl.path,
        )
    }
}
