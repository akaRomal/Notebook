package interfaces.api

interface ApiLocal {
    suspend fun saveToFile(filePath: String)
}