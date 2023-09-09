fun main() {

    val transactions: MutableList<Transaction> = mutableListOf()

    transactions.add(
        Transaction(
            id = 1,
            accountId = 1,
            amount = 20.0,
            merchant = "Restaurante Tavares",
            mcc = "5811"
        )
    )

    val accountController = AccountController()
    val transactionResultController = TransactionResultController()
    val transactionController = TransactionController(accountController, transactionResultController)
    transactionController.processTransaction(transactions[0])

    println("Finish")

    val results = TransactionResultController().getTransactionResultByAccountId(1)
    for (result in results)
        println(result)
}



