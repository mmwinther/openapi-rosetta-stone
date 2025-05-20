package nz.winther.dev

import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.restassured.RestAssured
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test

@MicronautTest(startApplication = false)
class BakeryStockStatusMicrocksTest :
    MicrocksServiceMock(
        micronautServiceName = "bakery",
        openApiDefinitionFile = "META-INF/swagger/bakery-0.1.yml",
    ) {
    @Test
    fun `muffins stock status`() {
        RestAssured
            .given()
            .baseUri(this.serviceUrl + this.servicePath)
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
            .baseUri(this.serviceUrl + this.servicePath)
            .`when`()
            .get("/baked-goods/bananas/stock")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.code)
    }
}
