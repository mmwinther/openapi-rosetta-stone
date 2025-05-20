package nz.winther.dev

import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@MicronautTest(startApplication = false)
class BakeryStockStatusMicrocksTest :
    MicrocksServiceMock(
        micronautServiceName = "bakery",
        openApiDefinitionFile = "META-INF/swagger/bakery-0.1.yml",
    ) {
    private lateinit var baseSpec: RequestSpecification

    @BeforeEach()
    fun createBaseSpec() {
        baseSpec =
            RequestSpecBuilder()
                .setBaseUri(
                    this.serviceUrl + this.servicePath,
                ).addFilter(RequestLoggingFilter())
                .addFilter(ResponseLoggingFilter())
                .build()
    }

    @Test
    fun `muffins stock status`() {
        RestAssured
            .given()
            .spec(baseSpec)
            .`when`()
            .get(
                "/baked-goods/muffins/stock",
            ).then()
            .statusCode(HttpStatus.OK.code)
            .body(equalTo("75"))
    }

    @Test
    fun `unknown stock status`() {
        RestAssured
            .given()
            .spec(baseSpec)
            .`when`()
            .get("/baked-goods/bananas/stock")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.code)
    }
}
