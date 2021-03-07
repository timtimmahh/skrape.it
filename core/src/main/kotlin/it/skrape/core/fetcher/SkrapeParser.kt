package it.skrape.core.fetcher

import it.skrape.core.htmlDocument
import it.skrape.selects.CssSelector

public abstract class SkrapeParser<T : Any>(
    private val useRelaxed: Boolean = false,
    private var extractStart: String = "",
    private val executeJS: Boolean = false
) {

    public fun extract(
        htmlBody: String,
        baseUri: String,
        parseStart: String,
        useRelaxed: Boolean? = null
    ): T =
        htmlDocument(
            html = editHtmlBody(htmlBody),
            baseUri = baseUri,
            jsExecution = executeJS
        ) {
            relaxed = useRelaxed ?: this@SkrapeParser.useRelaxed
            if (extractStart.isNotBlank())
                "$extractStart $parseStart" { process() }
            else parseStart { process() }
        }

    public fun extract(cssSelector: CssSelector): T =
        cssSelector.process()

    protected open fun editHtmlBody(htmlBody: String): String = htmlBody

    public abstract fun CssSelector.process(): T
}
