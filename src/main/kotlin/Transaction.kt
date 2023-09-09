data class Transaction(
    val id: String,
    val accountId: String,
    val amount: Double,
    val merchant: String,
    val mcc: String,
)
