package domain.interfaces.api

interface ApiLocal {
    suspend fun saveToFile(filePathAndName: String)
}