package org.app.translator.controller

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.app.translator.model.Language
import org.app.translator.model.TranslatedText
import org.app.translator.model.Translation
import tornadofx.Controller

class MainScreenController : Controller() {
    companion object {
        const val API_URL = "https://libretranslate.de/translate"
        const val API_LANGUAGES_URL = "https://libretranslate.com/languages"
    }

    private var mAvailableLanguages: ObservableList<String> = FXCollections.observableArrayList()

    fun translate(translation: Translation): String {
        var translatedText = ""

        val httpClient = OkHttpClient()

        val mediaType = "application/json; charset=utf8".toMediaType()
        val requestBody = Gson().toJson(translation).toRequestBody(mediaType)
        val request = Request.Builder().url(API_URL).post(requestBody).build()

        httpClient.newCall(request).execute().use { response ->
            response.body?.let {
                translatedText =
                        Gson().fromJson(it.string(), TranslatedText::class.java).translatedText
            }
        }

        return translatedText
    }

    fun languagesCollection(): ObservableList<String> = mAvailableLanguages

    fun fetchAvailableLanguages() {
        synchronized(this) {
            if (mAvailableLanguages.isNotEmpty()) {
                return
            }

            val request =
                    Request.Builder()
                            .url(API_LANGUAGES_URL)
                            .addHeader("accept", "application/json")
                            .build()

            var result: ArrayList<Language>? = null

            val httpClient = OkHttpClient()

            try {
                httpClient.newCall(request).execute().use { response ->
                    response.body?.let {
                        val type = object : TypeToken<ArrayList<Language>>() {}.type
                        result = Gson().fromJson(it.string(), type)
                    }
                }
            } catch (e: Exception) {}

            result?.let { it.map { language -> mAvailableLanguages.add(language.code) } }
        }
    }

    fun copyToClipboard(text: String) {
        val clipboard = Clipboard.getSystemClipboard()
        clipboard.clear()
        val content = ClipboardContent()
        content.putString(text)
        clipboard.setContent(content)
    }
}
