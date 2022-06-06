package org.app.translator.view

import async
import javafx.beans.binding.Bindings
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.stage.StageStyle
import org.app.translator.controller.MainScreenController
import org.app.translator.model.Translation
import tornadofx.*

class MainScreen : View() {
    companion object {
        const val TEXT_AREA_WIDTH: Double = 400.0
        const val TEXT_AREA_HEIGHT: Double = 400.0
        const val TEXT_AREA_FONT_SIZE: Double = 18.0
        const val TEXT_AREA_STYLE =
            "-fx-control-inner-background:#f3f3f3; -fx-text-fill: #000000;"
        const val DEFAULT_LANG: String = "en"
        const val DEFAULT_TARGET_LANG: String = "en"
        const val LANG_BOX_MARGIN: Double = 10.0
        const val TRANSLATION_CHAR_LIMIT: Int = 250
        const val BUTTON_FONT_SIZE: Double = 15.0
        const val TRANSLATE_LABELS_FONT_SIZE: Double = 18.0
    }

    private val mController: MainScreenController by inject()
    private var mFromComboBox: ComboBox<String>? = null
    private var mToComboBox: ComboBox<String>? = null
    private var mFromTextArea: TextArea? = null
    private var mTranslatedTextArea: TextArea? = null

    override val root =
        borderpane {
            style { backgroundColor += Color.web("#303F9F") }

            top =
                hbox(alignment = Pos.CENTER) {
                    label("Translate") {
                        font = Font(40.0)
                        hboxConstraints {
                            marginBottom = 50.0
                        }
                        style {
                            fontWeight = FontWeight.BOLD
                            textFill = Color.WHITE
                        }
                    }
                }

            center =
                vbox(alignment = Pos.CENTER) {
                    hbox(alignment = Pos.BOTTOM_CENTER) {
                        padding =
                            Insets(
                                10.0,
                                10.0,
                                10.0,
                                10.0
                            )

                        vbox(alignment = Pos.CENTER_LEFT) {
                            hbox {
                                label(
                                    "Translate from"
                                ) {
                                    font =
                                        Font(
                                            TRANSLATE_LABELS_FONT_SIZE
                                        )

                                    style {
                                        textFill =
                                            Color.WHITE
                                    }
                                }

                                mFromComboBox =
                                    combobox {
                                        style {
                                            backgroundColor +=
                                                Color.web(
                                                    "#ffffff"
                                                )
                                        }
                                        value =
                                            DEFAULT_LANG
                                        items =
                                            mController.languages()
                                        hboxConstraints {
                                            marginLeft =
                                                LANG_BOX_MARGIN
                                            marginBottom =
                                                10.0
                                        }
                                    }
                            }

                            mFromTextArea = textarea {
                                font =
                                    Font(
                                        TEXT_AREA_FONT_SIZE
                                    )
                                style = TEXT_AREA_STYLE
                                minWidth =
                                    TEXT_AREA_WIDTH
                                minHeight =
                                    TEXT_AREA_HEIGHT
                                vboxConstraints {
                                    marginRight =
                                        20.0
                                }
                                isWrapText = true
                            }

                            hbox {
                                vboxConstraints {
                                    marginTop =
                                        11.0
                                }
                                spacer()

                                val characterCountLabel =
                                    label(
                                        "0/$TRANSLATION_CHAR_LIMIT"
                                    ) {
                                        style {
                                            fontWeight =
                                                FontWeight.BOLD
                                            textFill =
                                                Color.WHITE
                                        }

                                        hboxConstraints {
                                            marginRight =
                                                20.0
                                        }
                                    }

                                mFromTextArea
                                    ?.let { textArea
                                        ->
                                        textArea
                                            .setOnKeyReleased {
                                                updateCharacterLimitLabel(
                                                    textArea,
                                                    characterCountLabel
                                                )
                                            }
                                    }
                            }
                        }

                        vbox(alignment = Pos.CENTER_RIGHT) {
                            hbox {
                                label(
                                    "Translate to"
                                ) {
                                    font =
                                        Font(
                                            TRANSLATE_LABELS_FONT_SIZE
                                        )
                                    style {
                                        textFill =
                                            Color.WHITE
                                    }
                                }

                                mToComboBox =
                                    combobox {
                                        style {
                                            backgroundColor +=
                                                Color.web(
                                                    "#ffffff"
                                                )
                                        }
                                        value =
                                            DEFAULT_LANG
                                        items =
                                            mController.languages()
                                        hboxConstraints {
                                            marginLeft =
                                                LANG_BOX_MARGIN
                                            marginBottom =
                                                10.0
                                        }
                                    }
                            }

                            mTranslatedTextArea =
                                textarea {
                                    style = TEXT_AREA_STYLE
                                    minWidth =
                                        TEXT_AREA_WIDTH
                                    minHeight =
                                        TEXT_AREA_WIDTH
                                    isWrapText =
                                        true
                                    font =
                                        Font(
                                            TEXT_AREA_FONT_SIZE
                                        )
                                }

                            hbox {
                                spacer()

                                button(
                                    "Fetch translation packs"
                                ) {
                                    style {
                                        backgroundColor +=
                                            Color.web(
                                                "#448AFF"
                                            )
                                        textFill =
                                            Color.WHITE
                                    }

                                    font =
                                        Font(
                                            BUTTON_FONT_SIZE
                                        )

                                    hboxConstraints {
                                        marginRight =
                                            10.0
                                    }

                                    setOnMouseClicked {
                                        async({
                                            mController.fetchAvailableLanguages()
                                        })
                                    }

                                    disableProperty()
                                        .bind(
                                            mController.languages()
                                                .sizeProperty
                                                .isNotEqualTo(
                                                    0
                                                )
                                        )
                                }

                                button(
                                    "Copy text"
                                ) {
                                    font =
                                        Font(
                                            BUTTON_FONT_SIZE
                                        )
                                    style {
                                        backgroundColor +=
                                            Color.web(
                                                "#448AFF"
                                            )
                                        textFill =
                                            Color.WHITE
                                    }

                                    disableProperty()
                                        .bind(
                                            Bindings.or(
                                                mFromTextArea?.textProperty()
                                                    ?.isEmpty,
                                                mTranslatedTextArea
                                                    ?.textProperty()
                                                    ?.isEmpty
                                            )
                                        )

                                    setOnMouseClicked {
                                        mTranslatedTextArea
                                            ?.let { textArea
                                                ->
                                                onTextCopied(
                                                    textArea.text
                                                        .toString()
                                                )
                                            }
                                    }
                                }
                            }
                        }
                    }

                    hbox(alignment = Pos.CENTER) {
                        button("Translate") {
                            font = Font(18.0)

                            style {
                                backgroundColor +=
                                    Color.web(
                                        "#448AFF"
                                    )
                                textFill =
                                    Color.WHITE
                            }

                            hboxConstraints {
                                marginTop = 30.0
                                marginRight = 10.0
                            }

                            disableProperty()
                                .bind(
                                    Bindings.or(
                                        mController.languages()
                                            .sizeProperty
                                            .isEqualTo(
                                                0
                                            ),
                                        mFromTextArea?.textProperty()
                                            ?.isEmpty,
                                    )
                                )

                            setOnMouseClicked {
                                mFromTextArea?.let {
                                    onMakeTranslation(
                                        it.text
                                    )
                                }
                            }
                        }

                        button("Swap") {
                            font = Font(18.0)

                            hboxConstraints {
                                marginTop = 30.0
                            }

                            style {
                                backgroundColor +=
                                    Color.web(
                                        "#448AFF"
                                    )
                                textFill =
                                    Color.WHITE
                            }

                            setOnMouseClicked {
                                swapSelectedLanguages()
                            }

                            disableProperty()
                                .bind(
                                    mController.languages()
                                        .sizeProperty
                                        .isEqualTo(
                                            0
                                        )
                                )
                        }
                    }
                }

            bottom =
                hbox(alignment = Pos.CENTER_RIGHT) {
                    label("Powered by LibreTranslate") {
                        font = Font(15.0)
                        style { textFill = Color.WHITE }
                        hboxConstraints {
                            marginRight = 5.0
                        }
                    }
                }

            async({
                mController.fetchAvailableLanguages()
            })
        }

    private fun swapSelectedLanguages() {
        mFromComboBox?.let { fromComboBox ->
            mToComboBox?.let { toComboBox ->
                val from = fromComboBox.selectionModel.selectedIndex
                val to = toComboBox.selectionModel.selectedIndex
                toComboBox.selectionModel.select(from)
                fromComboBox.selectionModel.select(to)
            }
        }
    }

    private fun onMakeTranslation(
        text: String,
    ) {
        val makeTranslation = {
            var translation: Translation? = null

            mFromComboBox?.let { fromComboBox ->
                mToComboBox?.let { toComboBox ->
                    val source = fromComboBox.selectedItem ?: DEFAULT_LANG
                    val target = toComboBox.selectedItem ?: DEFAULT_TARGET_LANG
                    translation = Translation(text, source, target)
                }
            }

            translation
        }

        async(
            { makeTranslation()?.let { mController.translate(it) } },
            { translatedText ->
                translatedText?.let {
                    mTranslatedTextArea?.let {
                        it.text = translatedText
                    }
                }
            }
        )
    }

    private fun onTextCopied(text: String) {
        if (text.isNotEmpty()) {
            mController.copyToClipboard(text)
            find<TextCopiedToClipboardNotification>()
                .openModal(stageStyle = StageStyle.UTILITY)
        }
    }

    private fun updateCharacterLimitLabel(textArea: TextArea, characterCountLabel: Label) {
        var totalCharacters = textArea.text.length

        if (totalCharacters > TRANSLATION_CHAR_LIMIT) {
            textArea.deleteText(TRANSLATION_CHAR_LIMIT, totalCharacters)

            totalCharacters = TRANSLATION_CHAR_LIMIT
        }

        characterCountLabel.text = "$totalCharacters/$TRANSLATION_CHAR_LIMIT"
    }
}

class TextCopiedToClipboardNotification : Fragment() {
    override val root =
        label("Text copied to clipboard") {
            font = Font(25.0)
            padding = Insets(20.0, 20.0, 20.0, 20.0)
        }
}
