package data.repository

import data.ERROR_AUTO_SAVE
import interfaces.api.ApiLocal
import interfaces.repository.Repository
import interfaces.storage.Storage
import models.NewNote
import models.Note
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RepositoryImpl(
    private val db: Storage,
    private val apiLocal: ApiLocal
) : Repository {
    private val formatDate = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
    private val temporaryNotes: MutableList<Note> = mutableListOf()
    private var timerAutoSave: Long = System.currentTimeMillis()

    init {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            temporaryNotes.addAll(
                try {
                    getNotes()
                } catch (e: Exception) {
                    emptyList()
                }
            )
        }

        scope.launch {
            while (true) {
                val timeNow: Long = System.currentTimeMillis()
                if (timeNow - timerAutoSave >= 300_000) {
                    try {
                        saveNotes(notes = temporaryNotes)
                        timerAutoSave = timeNow
                    } catch (e: Exception) {
                        println(ERROR_AUTO_SAVE)
                    }
                }
                delay(60_000)
            }
        }
    }

    override suspend fun addNote(newNote: NewNote): Boolean {
        return withContext(Dispatchers.IO) {
            var lastId = 1
            if (temporaryNotes.isNotEmpty()) {
                lastId = temporaryNotes.maxOf { it.id } + 1
            }
            val note = Note(
                id = lastId,
                title = newNote.title,
                text = newNote.text,
                date = getDate(),
            )
            temporaryNotes.add(note)
        }
    }

    override suspend fun updateNote(note: Note): Boolean {
        return withContext(Dispatchers.IO) {
            temporaryNotes.indexOfFirst { it.id == note.id }
                .takeIf { it >= 0 }
                ?.let { index ->
                    temporaryNotes.removeAt(index)
                    temporaryNotes.add(index, Note(
                        id = note.id,
                        text = note.text,
                        title = note.title,
                        date = note.date,
                        dateEdit = getDate()
                    ))
                    return@withContext true
                }
            false
        }
    }

    override suspend fun deleteNote(id: Int): Boolean {
        return withContext(Dispatchers.IO) {
            temporaryNotes.indexOfFirst { it.id == id }
                .takeIf { it >= 0 }
                ?.let { index ->
                    temporaryNotes.removeAt(index)
                    return@withContext true
                }
            false
        }
    }

    override suspend fun getNote(searchParam: String): List<Note> {
        return withContext(Dispatchers.IO) {
            temporaryNotes.filter {
                searchParam == "" || it.title.contains(other = searchParam, ignoreCase = true)
                        || it.text.contains(other = searchParam, ignoreCase = true)
            }
        }
    }

    override suspend fun getNote(id: Int): Note? {
        return withContext(Dispatchers.IO) { temporaryNotes.find { it.id == id } }
    }

    /**
     * @throws Exception
     */
    override suspend fun exportNotes(filePath: String) {
        saveNotes(notes = temporaryNotes)
        apiLocal.saveToFile(filePath = filePath, notes = temporaryNotes)
    }

    override suspend fun sortedByDate(isCreate: Boolean): List<Note> {
        return withContext(Dispatchers.IO) {
            if (isCreate) {
                temporaryNotes.sortedBy { it.date }
            } else {
                temporaryNotes.sortedBy { it.dateEdit }
            }
        }
    }

    /**
     * @throws Exception
     */
    override suspend fun closeApp() {
        saveNotes(notes = temporaryNotes)
    }

    private suspend fun getNotes(): List<Note> {
        return db.loadNotes()
    }

    private suspend fun saveNotes(notes: List<Note>): Boolean {
        return db.saveNotes(notes = notes)
    }

    private fun getDate(): String {
        return LocalDateTime.now().format(formatDate).toString()
    }
}