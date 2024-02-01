package data.storage

import com.google.gson.Gson
import domain.ERROR_LOAD_FROM_FILE
import domain.interfaces.storage.Storage
import domain.models.Note
import domain.models.RootNotes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class StorageImpl(
    private val gson: Gson,
    private val file: File,
) : Storage {

    override suspend fun loadNotes(): List<Note> {
        return withContext(Dispatchers.IO) {
            try {
                val json: String = file.readText()
                gson.fromJson(json, RootNotes::class.java).root
            }catch (e:Exception){
                throw Exception(ERROR_LOAD_FROM_FILE)
            }
        }
    }

    override suspend fun saveNotes(notes: List<Note>): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val string = gson.toJson(
                    RootNotes(root = notes)
                )
                file.writeText(text = string)
                true
            } catch (e: Exception) {
                false
            }
        }
    }
}