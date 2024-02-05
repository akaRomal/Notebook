package interfaces.api

import models.Note

interface ApiLocal {
    suspend fun saveToFile(filePath: String, notes: List<Note>)
}