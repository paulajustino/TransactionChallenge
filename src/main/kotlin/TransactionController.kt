class TransactionController(
    private val accountController: AccountControllerInterface,
    private val transactionResultController: TransactionResultControllerInterface
) {

    // processa uma transação
    fun processTransaction(transaction: Transaction) {
        val transactionResult: TransactionResult

        with(transaction) {
            // busca conta relacionada a transação
            val account = accountController.getAccountById(accountId = accountId)

            // verifica se a conta existe e registra o resultado da transação
            if (account == null) {
                transactionResult = createTransactionResult(
                    transaction = this,
                    status = TransactionStatus.DENIED,
                    message = TransactionStatusMessage.INVALID_ACCOUNT_ID_MESSAGE
                )
                transactionResultController.insertTransactionResult(transactionResult)
                return
            }

            // calcula novo saldo da conta
            val updatedBalance = hasAccountSufficientBalance(transaction = this, account = account)

            // verifica se a conta tem saldo suficiente e registra o resultado da transação
            if (updatedBalance == null) {
                transactionResult = createTransactionResult(
                    transaction = this,
                    status = TransactionStatus.DENIED,
                    message = TransactionStatusMessage.INSUFFICIENT_BALANCE_MESSAGE
                )
                transactionResultController.insertTransactionResult(transactionResult)
                return
            }

            // atualiza o saldo da conta e registra o resultado da transação
            val balanceType = checkBalanceTypeUsedInTransaction(transactionMcc = mcc)
            accountController.updateBalanceAccount(
                accountId = account.id,
                balance = updatedBalance,
                balanceType = balanceType
            )

            transactionResult = createTransactionResult(
                transaction = this,
                status = TransactionStatus.APPROVED,
                message = TransactionStatusMessage.APPROVED_MESSAGE
            )

            transactionResultController.insertTransactionResult(transactionResult)
        }
    }

    private fun hasAccountSufficientBalance(transaction: Transaction, account: Account): Double? {
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

    private fun createTransactionResult(
        transaction: Transaction,
        status: TransactionStatus,
        message: TransactionStatusMessage
    ): TransactionResult {
        return TransactionResult(
            id = transaction.id,
            accountId = transaction.accountId,
            amount = transaction.amount,
            merchant = transaction.merchant,
            mcc = transaction.mcc,
            status = status.status,
            message = message.statusMessage
        )
    }
}