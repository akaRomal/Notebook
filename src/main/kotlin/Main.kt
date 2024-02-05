import di.appModule
import interfaces.repository.Repository
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import models.Commands
import models.ResponseMessages
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.component.KoinComponent

fun main() {
    startKoin {
        modules(appModule)
    }
    StartApp().execute()
}

class StartApp: KoinComponent{
    private val repository: Repository by inject()
    private val handler: Handler = Handler(repository = repository)
    private var textAboveInput: String = MASSAGE_HELP
    private var inputLineText: String = INPUT_LINE_TEXT_DEFAULT

    fun execute(){
        runBlocking {
            do {
                println(textAboveInput)
                print(inputLineText)
                val instruction: String = readln()
                if (Commands.EXIT.value == instruction) {
                    val massage = async { handler.parser(task = instruction)}.await()
                    println(massage.textAboveInput)
                    break
                }
                val massages: ResponseMessages = handler.parser(task = instruction)
                textAboveInput = massages.textAboveInput
                inputLineText = massages.inputLineText
            } while (true)
        }
    }
}