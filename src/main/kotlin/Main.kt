fun main() {

    // configuraçao de contas testes
    val accountController = AccountController(DataBaseConfig())
    val accounts = accountController.createAccounts()
    for (account in accounts)
        accountController.insertAccount(account)

    // configuração de transações testes
    val transactionResultController = TransactionResultController(DataBaseConfig())
    val transactionController = TransactionController(accountController, transactionResultController)
    val transactions = transactionController.createTransactions()

    // imprime resultados
    for (transaction in transactions) {
        transactionController.processTransaction(transaction)
        val transactionResult = transactionResultController.getTransactionResultById(transaction.id)
        val account = accountController.getAccountById(transaction.accountId)
        println(transactionResult)
        println(account)
    }
}



