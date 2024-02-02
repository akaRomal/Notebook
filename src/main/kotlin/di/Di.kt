package di

import com.google.gson.Gson
import data.api.ApiLocalImpl
import data.repository.RepositoryImpl
import data.storage.StorageImpl
import domain.interfaces.api.ApiLocal
import domain.interfaces.repository.Repository
import domain.interfaces.storage.Storage
import java.io.File
/**
 *  Импровизированный dependency injection
 */

private val pathName: String = "${System.getProperty("user.home")}/NotebookConsole.txt"
object Di {

    val repository: Repository by lazy {
        RepositoryImpl(db = db, apiLocal = apiLocal)
    }

    private val apiLocal: ApiLocal by lazy {
        ApiLocalImpl(fileDB = file)
    }

    private val file: File by lazy {
        File(pathName)
    }

    private val db: Storage by lazy {
        StorageImpl(gson = gson, file = file)
    }

    private val gson: Gson by lazy {
        Gson()
    }
}