package domain.interfaces.repository

import domain.models.NewNote
import domain.models.Note

interface Repository {
    suspend fun addNote(newNote: NewNote): Boolean

    suspend fun updateNote(note: Note): Boolean

    suspend fun deleteNote(id: Int): Boolean

    suspend fun getNote(searchParam: String): List<Note>

    suspend fun getNote(id: Int): Note?

    suspend fun closeApp()

    suspend fun exportNotes(filePathAndName: String)

    suspend fun sortedByDate(isCreate: Boolean = true): List<Note>

}