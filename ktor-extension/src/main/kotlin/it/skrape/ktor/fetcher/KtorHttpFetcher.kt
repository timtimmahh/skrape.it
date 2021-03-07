package it.skrape.ktor.fetcher

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import it.skrape.core.fetcher.AsyncFetcher
import it.skrape.core.fetcher.Cookie
import it.skrape.core.fetcher.Result

/**
 *
 *
 * @author Timothy Logan
 */
public class KtorHttpFetcher(private val ktorClient: HttpClient) :
    AsyncFetcher<HttpRequestBuilder> {
    override suspend fun fetch(request: HttpRequestBuilder): Result {
        val fullResponse = ktorClient.request<HttpResponse>(request)

        return Result(
            fullResponse.readText(),
            Result.Status(fullResponse.status.value, fullResponse.status.description),
            fullResponse.contentType()?.toString(),
            fullResponse.headers.toMap().mapValues { it.value.firstOrNull().orEmpty() },
            fullResponse.request.url.toString(),
            fullResponse.setCookie() as List<Cookie>
        )
    }

    override val requestBuilder: HttpRequestBuilder
        get() = HttpRequestBuilder()
}
