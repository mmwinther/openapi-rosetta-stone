package nz.winther.dev

import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType.APPLICATION_JSON
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.exceptions.HttpStatusException
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag

val bakedGoodsStockStatus = mapOf("muffins" to 12)

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
                mediaType = APPLICATION_JSON,
                examples = [ExampleObject(name = "In Stock", value = "75")],
            ),
        ],
    )
    @Get
    fun bakedGoodsStockStatus(
        @Parameter(
            examples = [ExampleObject(name = "In Stock", value = "muffins")],
        )
        productName: String,
    ): Int =
        bakedGoodsStockStatus[productName]
            ?: throw HttpStatusException(HttpStatus.NOT_FOUND, "Not found")
}
