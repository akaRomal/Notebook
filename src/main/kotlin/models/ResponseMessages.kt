package models

import INPUT_LINE_TEXT_DEFAULT

data class ResponseMessages(
    val textAboveInput:String,
    val inputLineText: String = INPUT_LINE_TEXT_DEFAULT,
)
