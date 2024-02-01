package data.repository

import domain.interfaces.repository.Repository
import domain.interfaces.storage.Storage
import domain.models.NewNote
import domain.models.Note
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RepositoryImpl(private val db: Storage) : Repository {
    private val formatDate = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
    override suspend fun addNote(newNote: NewNote): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun editNote(note: Note): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNote(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getAllNotes(): List<Note>? {
        TODO("Not yet implemented")
    }

    override suspend fun getNote(id: Int): Note? {
        TODO("Not yet implemented")
    }

    override suspend fun findNotes(param: String): List<Note>? {
        TODO("Not yet implemented")
    }

    override suspend fun exportNotes(fileName: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun sortedByDate(isCreate: Boolean): List<Note> {
        TODO("Not yet implemented")
    }

    /**
     * @throws Exception
     */
    override suspend fun getNotes(): List<Note> {
        return db.loadNotes()
    }

    override suspend fun saveNotes(notes: List<Note>): Boolean {
        return db.saveNotes(notes = notes)
    }

    private fun formatDate(): String{
        return LocalDateTime.now().format(formatDate).toString()
    }
}