import di.Di
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import models.Commands
import models.ResponseMessages

fun main() {
    val handler: Handler = Handler(repository = Di.repository)
    var inputLineText: String = INPUT_LINE_TEXT_DEFAULT
    var textAboveInput: String = MASSAGE_HELP
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
            inputLineText = massages.inputLineText
            textAboveInput = massages.textAboveInput
        } while (true)
    }
}