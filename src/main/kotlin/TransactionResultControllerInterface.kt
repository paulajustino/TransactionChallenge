interface TransactionResultControllerInterface {

    fun insertTransactionResult(transactionResult: TransactionResult)
    fun getTransactionResultByAccountId(id: Int): List<TransactionResult?>
    fun getTransactionResultByMerchant(merchant: String): List<TransactionResult?>
}