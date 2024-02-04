package models

data class Note(
    val id: Int,
    val title: String,
    val text: String,
    val date: String,
    val dataEdit: String? = null,
)
