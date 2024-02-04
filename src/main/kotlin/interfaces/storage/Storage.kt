package interfaces.storage

import models.Note

interface Storage {
    suspend fun loadNotes(): List<Note>

    suspend fun saveNotes(notes: List<Note>): Boolean
}