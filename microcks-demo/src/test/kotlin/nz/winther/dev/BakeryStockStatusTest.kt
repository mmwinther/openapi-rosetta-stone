package nz.winther.dev
import io.micronaut.http.HttpStatus
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.restassured.specification.RequestSpecification
import jakarta.inject.Inject
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@MicronautTest
class BakeryStockStatusTest {
    @Inject
    lateinit var application: EmbeddedApplication<*>

    @Test
    fun testItWorks() {
        Assertions.assertTrue(application.isRunning)
    }

    @Test
    fun `muffins stock status`(spec: RequestSpecification) {
        spec
            .`when`()
            .get("/baked-goods/muffins/stock")
            .then()
            .statusCode(HttpStatus.OK.code)
            .body(equalTo("12"))
    }

    @Test
    fun `unknown stock status`(spec: RequestSpecification) {
        spec
            .`when`()
            .get("/baked-goods/bananas/stock")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.code)
    }
}
