package data.api

import data.ERROR_COPY_FILE
import interfaces.api.ApiLocal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ApiLocalImpl(private val fileDB: File) : ApiLocal {
    override suspend fun saveToFile(filePath: String) {
        withContext(Dispatchers.IO) {
            try {
                val originalData: String = fileDB.readText()
                File("${filePath}Notes.txt").writeText(originalData)
            } catch (e: Exception) {
                throw Exception(ERROR_COPY_FILE)
            }
        }
    }
}