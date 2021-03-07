package it.skrape.ktor.client

import io.ktor.http.*

/**
 * Content type matcher for HTML content.
 *
 * @author Timothy Logan
 */
public class SkrapeContentTypeMatcher : ContentTypeMatcher {
    override fun contains(contentType: ContentType): Boolean {
        if (ContentType.Text.Html.match(contentType)
            || ContentType.Application.Xml.match(contentType)
        ) return true

        val value = contentType.withoutParameters().toString()
        return contentType.contentType == "text"
                && value.substringAfterLast('+') in arrayOf("html", "+xml")
    }
}
