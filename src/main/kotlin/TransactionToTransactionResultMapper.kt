class TransactionToTransactionResultMapper {

    fun mapperTransactionToTransactionResult(
        transaction: Transaction,
        transactionStatus: TransactionStatus,
        transactionStatusMessage: TransactionStatusMessage,
        ): TransactionResult {
        return TransactionResult(
            id = transaction.id,
            accountId = transaction.id,
            status = transactionStatus.name,
            message = transactionStatusMessage.statusMessage,
        )
    }
}