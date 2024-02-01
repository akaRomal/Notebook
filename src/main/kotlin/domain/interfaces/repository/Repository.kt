package domain.interfaces.repository

import domain.models.NewNote
import domain.models.Note

interface Repository {
    suspend fun addNote(newNote: NewNote): Boolean

    suspend fun editNote(note: Note): Boolean

    suspend fun deleteNote(id: Int): Boolean

    suspend fun getAllNotes(): List<Note>?

    suspend fun getNote(id: Int): Note?

    suspend fun findNotes(param: String): List<Note>?

    suspend fun exportNotes(fileName: String): Boolean

    suspend fun sortedByDate(isCreate: Boolean = true): List<Note>

    suspend fun getNotes(): List<Note>

    suspend fun saveNotes(notes: List<Note>): Boolean
}