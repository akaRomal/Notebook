package data.repository

import data.ERROR_AUTO_SAVE
import domain.interfaces.api.ApiLocal
import domain.interfaces.repository.Repository
import domain.interfaces.storage.Storage
import domain.models.NewNote
import domain.models.Note
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
                lastId = temporaryNotes.maxOf {
                    it.id
                }
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
                    temporaryNotes.add(index, note)
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
                searchParam == "" || it.title.contains(searchParam) || it.text.contains(searchParam)
            }
        }
    }

    override suspend fun getNote(id: Int): Note? {
        return withContext(Dispatchers.IO) { temporaryNotes.find { it.id == id } }
    }

    /**
     * @throws Exception
     */
    override suspend fun exportNotes(filePathAndName: String) {
        saveNotes(notes = temporaryNotes)
        apiLocal.saveToFile(filePathAndName = filePathAndName)
    }

    override suspend fun sortedByDate(isCreate: Boolean): List<Note> {
        return withContext(Dispatchers.IO) {
            if (isCreate) {
                temporaryNotes.sortedBy { it.date }
            } else {
                temporaryNotes.sortedBy { it.dataEdit }
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