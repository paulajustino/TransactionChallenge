
fun main() {

    //AccountController()
    TransactionController()

    println("Finish")

    val results = TransactionResultController().getTransactionResultByAccountId(1)
    for (result in results)
        println(result)
}



