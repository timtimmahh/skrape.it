package it.skrape.ktor.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.utils.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import it.skrape.core.fetcher.SkrapeParser
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance

public val SkrapeStart: AttributeKey<String> = AttributeKey("SkrapeItStart")

/**
 * [HttpClient] feature that parses HTML as custom objects
 * from response bodies using a [parser].
 *
 * [contentTypeMatchers] is a list of content type matchers to use to validate the
 * response type.
 *
 */
public class SkrapeFeature(
    private val contentTypeMatchers: List<ContentTypeMatcher> = listOf(SkrapeContentTypeMatcher()),
    public val parsers: Map<KClass<*>, SkrapeParser<*>> = emptyMap()
) {

    public constructor(vararg parsers: Pair<KClass<*>, SkrapeParser<*>>) : this(
        listOf(SkrapeContentTypeMatcher()),
        parsers.toMap()
    )

    internal constructor(config: Config) : this(
        config.receiveContentTypeMatchers
    )

    public class Config {
        private val _parsers: MutableList<SkrapeParser<*>> = mutableListOf()
        private val _receiveContentTypeMatchers: MutableList<ContentTypeMatcher> =
            mutableListOf(SkrapeContentTypeMatcher())

        public var parsers: List<SkrapeParser<*>>
            set(value) {
                _parsers.clear()
                _parsers.addAll(value)
            }
            get() = _parsers

        /**
         * List of content type matchers that are handled by this feature.
         * Please note that wildcard content types are supported but no quality specification provided.
         */
        public var receiveContentTypeMatchers: List<ContentTypeMatcher>
            set(value) {
                require(value.isNotEmpty()) { "At least one content type should be provided to acceptContentTypes" }
                _receiveContentTypeMatchers.clear()
                _receiveContentTypeMatchers.addAll(value)
            }
            get() = _receiveContentTypeMatchers
    }

    internal fun canHandle(contentType: ContentType): Boolean =
        contentTypeMatchers.any { matcher -> matcher.contains(contentType) }

    internal fun readItem(parser: SkrapeParser<*>, body: Input, url: Url, parseStart: String) =
        parser.extract(
            body.readText(),
            "${url.protocol}:://${url.host}",
            parseStart
        )

    public companion object Feature : HttpClientFeature<Config, SkrapeFeature> {
        override val key: AttributeKey<SkrapeFeature> = AttributeKey("HTML")

        override fun install(feature: SkrapeFeature, scope: HttpClient) {
            scope.responsePipeline.intercept(HttpResponsePipeline.Transform) { (info, body) ->
                if (body !is ByteReadChannel) return@intercept

                val contentType = context.response.contentType() ?: return@intercept
                if (!feature.canHandle(contentType)) return@intercept

                (info.type.companionObjectInstance as? SkrapeParser<*>
                    ?: feature.parsers[info.type])
                    ?.let {
                        val parsedBody = feature.readItem(
                            it,
                            body.readRemaining(),
                            context.request.url,
                            context.attributes.getOrNull(SkrapeStart) ?: ""
                        )
                        proceedWith(
                            HttpResponseContainer(
                                info, parsedBody
                            )
                        )
                    }
            }
        }

        override fun prepare(block: Config.() -> Unit): SkrapeFeature =
            SkrapeFeature(Config().apply(block))
    }
}


/**
 * Install [SkrapeFeature].
 */
public fun HttpClientConfig<*>.SkrapeIt(block: SkrapeFeature.Config.() -> Unit) {
    install(SkrapeFeature, block)
}
