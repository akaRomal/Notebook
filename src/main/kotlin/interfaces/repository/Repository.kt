package interfaces.repository

import models.NewNote
import models.Note

interface Repository {
    suspend fun addNote(newNote: NewNote): Boolean

    suspend fun updateNote(note: Note): Boolean

    suspend fun deleteNote(id: Int): Boolean

    suspend fun getNotes(): List<Note>

    suspend fun getNotes(searchParam: String): List<Note>

    suspend fun getNote(id: Int): Note?

    suspend fun closeApp()

    suspend fun exportNotes(filePath: String)

    suspend fun sortedByDate(isCreate: Boolean = true): List<Note>

}