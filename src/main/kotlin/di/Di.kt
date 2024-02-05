package di

import com.google.gson.Gson
import data.api.ApiLocalImpl
import data.repository.RepositoryImpl
import data.storage.StorageImpl
import interfaces.api.ApiLocal
import interfaces.repository.Repository
import interfaces.storage.Storage
import org.koin.dsl.module
import java.io.File

private val pathName: String = "${System.getProperty("user.home")}/NotebookConsole.txt"

val appModule = module {

    single<Repository> { RepositoryImpl(db = get(), apiLocal = get()) }

    single<ApiLocal> { ApiLocalImpl() }

    single<Storage> { StorageImpl(gson = get(), file = get()) }

    single<File> { File(pathName) }

    single<Gson> { Gson() }
}