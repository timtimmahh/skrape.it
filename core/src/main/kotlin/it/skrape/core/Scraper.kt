package it.skrape.core

import it.skrape.SkrapeItDsl
import it.skrape.core.fetcher.AsyncFetcher
import it.skrape.core.fetcher.Fetcher
import it.skrape.core.fetcher.Result

public class Scraper<R>(public val client: Fetcher<R>, internal val preparedRequest: R) {
    public constructor(client: Fetcher<R>) : this(client, client.requestBuilder)

    @SkrapeItDsl
    public fun request(init: R.() -> Unit): Unit =
        this.preparedRequest.run(init)

    public fun scrape(): Result =
        client.fetch(preparedRequest)

    @SkrapeItDsl
    public val preConfigured: Scraper<R>
        get() = this
}

public class AsyncScraper<R>(public val client: AsyncFetcher<R>, internal val preparedRequest: R) {
    public constructor(client: AsyncFetcher<R>) : this(client, client.requestBuilder)

    @SkrapeItDsl
    public fun request(init: R.() -> Unit): Unit =
        this.preparedRequest.run(init)

    public suspend fun scrape(): Result =
        client.fetch(preparedRequest)

    @SkrapeItDsl
    public val preConfigured: AsyncScraper<R>
        get() = this
}
