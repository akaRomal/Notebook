import interfaces.repository.Repository
import models.NewNote
import models.Note
import models.Commands
import models.ResponseMessages

class Handler(private val repository: Repository) {
    private var isCommand: Boolean = true
    private var isTitle: Boolean = true
    private var idUpdateNote: Int? = null
    private var temporaryTitle: String = ""

    suspend fun parser(task: String): ResponseMessages {
        return if (isCommand) {
            val instruction = task.split(" ", limit = 2)
            commandProcessing(instruction = instruction)
        } else {
            if (idUpdateNote == null) {
                createNote(text = task)
            } else {
                updateNote(text = task)
            }
        }
    }

    private suspend fun commandProcessing(instruction: List<String>): ResponseMessages {
        val command: String = instruction[0]
        var value: String = ""
        if (instruction.count() > 1) {
            value = instruction[1]
        }
        val responseMessages: ResponseMessages = when (command) {
            Commands.NOTES.value -> getNotes()
            Commands.NOTE.value -> getNote(idNote = value)
            Commands.CREATE.value -> {
                isCommand = !isCommand
                ResponseMessages(
                    textAboveInput = TEXT_ABOVE_INPUT_CREATE_NOTE,
                    inputLineText = INPUT_LINE_TEXT_CREATE_TITLE
                )
            }

            Commands.EDIT.value -> {
                if (isIdNote(id = value)) {
                    idUpdateNote = value.toInt()
                    isCommand = !isCommand
                    ResponseMessages(
                        textAboveInput = TEXT_ABOVE_INPUT_EDIT_NOTE.format(value),
                        inputLineText = INPUT_LINE_TEXT_CREATE_TITLE
                    )
                } else {
                    ResponseMessages(textAboveInput = MASSAGE_EDIT_NOT_ID)
                }
            }

            Commands.DELETE.value -> delete(idNote = value)
            Commands.SEARCH.value -> getNotes(searchParam = value)
            Commands.SORT_DATE.value -> sortBy(isCreate = true)
            Commands.SORT_EDIT.value -> sortBy(isCreate = false)
            Commands.EXPORT.value -> exportNotes(path = value)
            Commands.EXIT.value -> exit()
            Commands.HELP.value -> ResponseMessages(textAboveInput = MASSAGE_HELP)
            else -> ResponseMessages(textAboveInput = ERROR_COMMAND.format(command))
        }
        return responseMessages
    }

    private suspend fun exportNotes(path: String): ResponseMessages {
        return try {
            repository.exportNotes(filePath = path)
            ResponseMessages(textAboveInput = MASSAGE_EXPORT_FILE_OK)
        } catch (e: Exception) {
            ResponseMessages(textAboveInput = MASSAGE_EXPORT_FILE_ERROR)
        }
    }

    private suspend fun createNote(text: String): ResponseMessages {
        var textAboveInput = ""
        var inputLineText = INPUT_LINE_TEXT_CREATE_TEXT

        if (isTitle) {
            temporaryTitle = text
        } else {
            isCommand = !isCommand

            repository.addNote(
                newNote = NewNote(
                    title = temporaryTitle,
                    text = text
                )
            )
            temporaryTitle = ""
            textAboveInput = TEXT_ABOVE_INPUT_CREATED_NOTE
            inputLineText = INPUT_LINE_TEXT_DEFAULT
        }
        isTitle = !isTitle
        return ResponseMessages(textAboveInput = textAboveInput, inputLineText = inputLineText)
    }


    private suspend fun updateNote(text: String): ResponseMessages {

        var textAboveInput = TEXT_ABOVE_INPUT_EDIT_NOTE.format(idUpdateNote)
        var inputLineText = INPUT_LINE_TEXT_CREATE_TEXT

        if (isTitle) {
            temporaryTitle = text
        } else {
            isCommand = !isCommand

            val note: Note = repository.getNote(idUpdateNote!!)!!
            repository.updateNote(
                note = Note(
                    id = note.id,
                    title = temporaryTitle,
                    text = text,
                    date = note.date
                )
            )

            temporaryTitle = ""
            textAboveInput = TEXT_ABOVE_INPUT_EDITED_NOTE
            inputLineText = INPUT_LINE_TEXT_DEFAULT
        }
        isTitle = !isTitle
        return ResponseMessages(textAboveInput = textAboveInput, inputLineText = inputLineText)
    }

    private suspend fun delete(idNote: String): ResponseMessages {
        val id = idNote.toIntOrNull()
        return if (id != null && repository.deleteNote(id = id)) {
            ResponseMessages(textAboveInput = MASSAGE_DELETE_ID.format(id))
        } else
            ResponseMessages(textAboveInput = ERROR_MASSAGE_NOTE_ID)
    }

    private suspend fun getNotes(searchParam: String = ""): ResponseMessages {
        val notes: List<Note> = repository.getNote(searchParam = searchParam)
        var textAboveInput: String = ""
        notes.forEach {
            textAboveInput += NOTES_OUTPUT_TEMPLATE.format(it.id, it.title, it.date, it.dataEdit ?: it.date)
        }
        return ResponseMessages(textAboveInput = textAboveInput)
    }

    private suspend fun getNote(idNote: String): ResponseMessages {
        val id = idNote.toIntOrNull()
        var textAboveInput: String = ERROR_MASSAGE_NOTE_ID
        id?.let { idLet ->
            val note: Note? = repository.getNote(id = idLet)
            note?.let {
                textAboveInput = NOTE_OUTPUT_TEMPLATE.format(it.title, it.text, it.date, it.dataEdit ?: it.date)
            }
        }
        return ResponseMessages(textAboveInput = textAboveInput)
    }

    private suspend fun sortBy(isCreate: Boolean): ResponseMessages {
        val notes: List<Note> = repository.sortedByDate(isCreate = isCreate)
        var textAboveInput: String = ""
        notes.forEach {
            textAboveInput += NOTES_OUTPUT_TEMPLATE.format(it.id, it.title, it.date, it.dataEdit ?: it.date)
        }
        return ResponseMessages(textAboveInput = textAboveInput)
    }

    private suspend fun exit(): ResponseMessages {
        repository.closeApp()
        return ResponseMessages(textAboveInput = MASSAGE_STOP_APP)
    }

    private suspend fun isIdNote(id: String): Boolean {
        return try {
            repository.getNote(id = id.toInt()) != null
        } catch (e: NumberFormatException) {
            false
        }
    }

}