package data.api

import data.ERROR_COPY_FILE
import domain.interfaces.api.ApiLocal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ApiLocalImpl(private val fileDB: File) : ApiLocal {
    override suspend fun saveToFile(filePathAndName: String) {
        withContext(Dispatchers.IO) {
            try {
                val originalData: String = fileDB.readText()
                File(filePathAndName).writeText(originalData)
            } catch (e: Exception) {
                throw Exception(ERROR_COPY_FILE)
            }
        }
    }
}