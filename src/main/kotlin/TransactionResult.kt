data class TransactionResult(
    val id: Int,
    val accountId: Int,
    val amount: Double,
    val merchant: String,
    val mcc: String,
    val status: String,
    val message: String,
)
