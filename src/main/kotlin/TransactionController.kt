class TransactionController {

    private lateinit var transactions: MutableList<Transaction>

/*    init {
        createTransactions()
        for (transaction in transactions)
            processTransaction(transaction)
    }*/

    private fun createTransactions(): MutableList<Transaction> {
        transactions = mutableListOf()

        transactions.add(
            Transaction(
                id = 1,
                accountId = 1,
                amount = 20.0,
                merchant = "Restaurante Tavares",
                mcc = "5811"
            )
        )

        return transactions
    }

    // processa uma transação
    private fun processTransaction(transaction: Transaction) {
        val transactionResult: TransactionResult

        with(transaction) {
            // busca conta relacionada a transação
            val account = AccountController().getAccountById(id = accountId)

            // verifica se conta existe
            if (account != null) {

                // verifica se saldo da conta é suficiente para fazer a transação
                val updatedBalance = calculateIfAccountHasSufficientBalance(transaction = this, account = account)
                if (updatedBalance != null) {

                    // atualiza saldo da conta
                    val balanceType = checkBalanceTypeUsedInTransaction(transactionMcc = mcc)
                    AccountController().updateBalanceAccount(
                        accountId = account.id,
                        balance = updatedBalance,
                        balanceType = balanceType
                    )

                    // registra resultado da transação no banco
                    transactionResult = TransactionResult(
                        id = id,
                        accountId = accountId,
                        amount = amount,
                        merchant = merchant,
                        mcc = mcc,
                        status = TransactionStatus.APPROVED.status,
                        message = TransactionStatusMessage.APPROVED_MESSAGE.statusMessage,
                    )

                    TransactionResultController().insertTransactionResult(transactionResult)
                } else {
                    transactionResult = TransactionResult(
                        id = id,
                        accountId = accountId,
                        amount = amount,
                        merchant = merchant,
                        mcc = mcc,
                        status = TransactionStatus.DENIED.status,
                        message = TransactionStatusMessage.INSUFFICIENT_BALANCE_MESSAGE.statusMessage,
                    )

                    TransactionResultController().insertTransactionResult(transactionResult)
                }

            } else {
                transactionResult = TransactionResult(
                    id = id,
                    accountId = accountId,
                    amount = amount,
                    merchant = merchant,
                    mcc = mcc,
                    status = TransactionStatus.DENIED.status,
                    message = TransactionStatusMessage.INVALID_ACCOUNT_ID_MESSAGE.statusMessage,
                )

                TransactionResultController().insertTransactionResult(transactionResult)
            }
        }
    }

    private fun calculateIfAccountHasSufficientBalance(transaction: Transaction, account: Account): Double? {
        return when (transaction.mcc) {
            "5411", "5412" -> calculateAccountBalance(transaction.amount, account.foodBalance)
            "5811", "5812" -> calculateAccountBalance(transaction.amount, account.mealBalance)
            else -> calculateAccountBalance(transaction.amount, account.cashBalance)
        }
    }

    private fun calculateAccountBalance(amount: Double, actualBalance: Double): Double? {
        val finalBalance = actualBalance - amount

        return if (finalBalance >= 0.0) {
            finalBalance
        } else {
            null
        }
    }

    private fun checkBalanceTypeUsedInTransaction(transactionMcc: String): String {
        return when (transactionMcc) {
            "5411", "5412" -> "foodBalance"
            "5811", "5812" -> "mealBalance"
            else -> "cashBalance"
        }
    }
}