package nz.winther.dev

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.exceptions.HttpStatusException
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.zalando.problem.Problem

val bakedGoodsStockStatus = mapOf("muffins" to 12)

const val PROBLEM_JSON_NOT_FOUND = """{
  "type": "about:blank",
  "status": 404
}"""

@Tag(name = "Baked Goods")
@Controller("/baked-goods/{productName}/stock")
class BakeryStockController {
    /**
     * The number of items remaining of the requested baked good.
     */
    @ApiResponse(
        responseCode = "200",
        content = [
            Content(
                examples = [ExampleObject(name = "In Stock", value = "75")],
            ),
        ],
    )
    @ApiResponse(
        responseCode = "404",
        content = [
            Content(
                mediaType = "application/json+problem",
                examples = [ExampleObject(name = "Unknown Item", value = PROBLEM_JSON_NOT_FOUND)],
                schema = Schema(implementation = Problem::class),
            ),
        ],
    )
    @Get
    fun bakedGoodsStockStatus(
        @Parameter(
            examples = [
                ExampleObject(
                    name = "In Stock",
                    value = "muffins",
                ), ExampleObject(name = "Unknown Item", value = "bananas"),
            ],
        )
        productName: String,
    ): Int =
        bakedGoodsStockStatus[productName]
            ?: throw HttpStatusException(HttpStatus.NOT_FOUND, "Not found")
}
