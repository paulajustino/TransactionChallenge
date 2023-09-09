class TransactionController {

    private val account: Account? = null
    private lateinit var transactionResult: TransactionResult

    // funcao que processa a transacao
    fun processTransaction(transaction: Transaction): TransactionResult {

        // busca dados da conta

        // verifica status da transacao
        return checkTransactionRequirements(transaction = transaction)
    }
    // funcao que atualiza saldo da conta se transacao for aprovada e
    // cria o resultado da transacao

    private fun checkTransactionRequirements(transaction: Transaction): TransactionResult {
        // cenário: account id inválido
        if (account == null) {
            // cria transacao no banco com essas informaçoes
            return TransactionResult(
                id = transaction.id,
                accountId = transaction.accountId,
                status = TransactionStatus.DENIED.name,
                message = TransactionStatusMessage.INVALID_ACCOUNT_ID_MESSAGE.statusMessage,
            )
        }

        account.let {
            if (calculateIfAccountHasSufficientBalance(transaction = transaction) == null) {
                // cria transacao no banco com essas informaçoes
                return TransactionResult(
                    id = transaction.id,
                    accountId = transaction.accountId,
                    status = TransactionStatus.DENIED.name,
                    message = TransactionStatusMessage.INSUFFICIENT_BALANCE_MESSAGE.statusMessage,
                )
            } else {
                // atualiza saldo da conta

                // cria transacao no banco com essas informaçoes
                return TransactionResult(
                    id = transaction.id,
                    accountId = transaction.accountId,
                    status = TransactionStatus.APPROVED.name,
                    message = TransactionStatusMessage.APPROVED_MESSAGE.statusMessage,
                )
            }
        }

    }

    private fun calculateIfAccountHasSufficientBalance(transaction: Transaction): Double? {
        account?.let {
            return when (transaction.mcc) {
                MerchantCategory.FOOD.name -> {
                    calculateAccountBalance(transaction.amount, account.foodBalance)
                }
                MerchantCategory.MEAL.name -> {
                    calculateAccountBalance(transaction.amount, account.mealBalance)
                }
                MerchantCategory.CASH.name -> {
                    calculateAccountBalance(transaction.amount, account.cashBalance)
                }
                else -> throw IllegalStateException()
            }
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

    private fun createTransactionResult(
        transactionId: String,
        accountId: String,
        status: String,
        statusMessage: String
    ): TransactionResult {
        return TransactionResult(
            id = transactionId,
            accountId = accountId,
            status = status,
            message = statusMessage,
        )
    }
}