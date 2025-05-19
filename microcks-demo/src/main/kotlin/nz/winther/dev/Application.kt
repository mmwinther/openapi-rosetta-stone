package nz.winther.dev

import io.micronaut.runtime.Micronaut.run
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.info.*
import io.swagger.v3.oas.annotations.servers.Server
import io.swagger.v3.oas.annotations.tags.Tag

@OpenAPIDefinition(
    info =
        Info(
            title = "Bakery",
            description = "See the stock status of baked goods produced by Winther Bakery",
            version = "0.1",
            license =
                License(
                    name = "CC BY 4.0",
                    url = "https://creativecommons.org/licenses/by/4.0/deed.no",
                ),
            contact = Contact(email = "mmw@ssb.no", name = "Miles Winther"),
        ),
    servers = [
        Server(url = "http://localhost:8080"),
    ],
    tags = [
        Tag(name = "Baked Goods", description = "Information about Baked Goods"),
    ],
)
object Api

fun main(args: Array<String>) {
    run(*args)
}
