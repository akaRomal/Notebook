package domain.interfaces.storage

import domain.models.Note

interface Storage {
    suspend fun loadNotes(): List<Note>

    suspend fun saveNotes(notes: List<Note>): Boolean
}