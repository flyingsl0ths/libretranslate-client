package org.app.translator.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Language(val code: String = "", val name: String = "") : Serializable

data class TranslatedText(val translatedText: String)

data class Translation(
    @SerializedName("q") val query: String,
    val source: String,
    val target: String,
    val format: String = "text"
) : Serializable
