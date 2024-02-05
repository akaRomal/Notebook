const val MASSAGE_HELP: String =
        "notes                              - Просмотр списка всех заметок.\n" +
        "note Идентификатор_заметки         - Просмотр конкретной заметки.\n" +
        "create                             - Создание новой заметки.\n" +
        "edit Идентификатор_заметки         - Редактирование существующей заметки.\n" +
        "delete Идентификатор_заметки       - Удаление заметки.\n" +
        "search Текст поиска                - Поиск заметок по заголовку или тексту.\n" +
        "sortDate                           - Сортировка заметок по дате создания.\n" +
        "sortEdit                           - Сортировка заметок по дате последнего редактирования.\n" +
        "export Путь к папке для сохранения - Экспорт заметок в .txt файл(C:\\Folder\\).\n" +
        "exit                               - Выход.\n" +
        "help                               - Вывод команд.\n"

const val INPUT_LINE_TEXT_DEFAULT: String = "->: "
const val NOTE_OUTPUT_TEMPLATE: String =
    "Заголовок: %s\n" +
            "Сообщение: %s\n" +
            "Дата создания: %s\n" +
            "Дата изменения: %s\n" +
            "*****************************************************\n"

const val NOTES_OUTPUT_TEMPLATE: String =
    "Ид: %s\n" +
            "Заголовок: %s\n" +
            "Дата создания: %s " +
            "Дата изменения: %s\n" +
            "*****************************************************\n"


const val ERROR_COMMAND: String = "Команда: \"%s\" не известна.\n"

const val MASSAGE_STOP_APP: String = "Программа завершена!\n"

const val ERROR_MASSAGE_NOTE_ID: String = "Записи с данным идентификатором не найдено!\n"

const val MASSAGE_DELETE_ID: String = "Запись с идентификатором %d удалена!\n"

const val MASSAGE_EDIT_NOT_ID: String = "Запись с указанным идентификатором не найдена!\n"

const val TEXT_ABOVE_INPUT_CREATE_NOTE: String = "Создание новой записи."
const val INPUT_LINE_TEXT_CREATE_TITLE: String = "Заголовок: "
const val INPUT_LINE_TEXT_CREATE_TEXT: String = "Текст: "
const val TEXT_ABOVE_INPUT_CREATED_NOTE: String = "Новая запись создана."

const val TEXT_ABOVE_INPUT_EDIT_NOTE: String = "Редактирование записи c идентификатором %s."
const val TEXT_ABOVE_INPUT_EDITED_NOTE: String = "Записи c идентификатором %s отредактирована."

const val MASSAGE_EXPORT_FILE_ERROR: String ="Ошибка экспортирования заметок в файл!"
const val MASSAGE_EXPORT_FILE_OK: String ="Экспорт заметок в файл завершён!"
