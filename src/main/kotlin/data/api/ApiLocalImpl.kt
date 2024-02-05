package data.api

import data.ERROR_COPY_FILE
import interfaces.api.ApiLocal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import models.Note
import java.io.File

class ApiLocalImpl : ApiLocal {
    override suspend fun saveToFile(filePath: String, notes: List<Note>) {
        withContext(Dispatchers.IO) {
            try {
                val stringNotes: String = notes.joinToString("") { note ->
                    "Заголовок: ${note.title}\n" +
                            "Сообщение:${note.text}\n" +
                            "Дата создания: ${note.date}\n" +
                            "Дата изменения: ${note.dateEdit ?: note.date}\n" +
                            "*****************************************************\n"
                }
                File("${filePath}Notes.txt").writeText(stringNotes)
            } catch (e: Exception) {
                throw Exception(ERROR_COPY_FILE)
            }
        }
    }
}