package models

enum class Commands(val value: String) {
    NOTES(value = "notes"),
    NOTE(value = "note"),
    CREATE(value = "create"),
    EDIT(value = "edit"),
    DELETE(value = "delete"),
    SEARCH(value = "search"),
    SORT_DATE(value = "sortDate"),
    SORT_EDIT(value = "sortEdit"),
    EXPORT(value = "export"),
    EXIT(value = "exit"),
    HELP(value = "help"),
}