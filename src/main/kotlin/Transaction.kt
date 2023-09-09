data class Transaction(
    val id: Int,
    val accountId: Int,
    val amount: Double,
    val merchant: String,
    val mcc: String,
)
