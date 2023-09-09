interface AccountControllerInterface {

    fun insertAccount(account: Account)
    fun getAccountById(accountId: Int): Account?
    fun updateBalanceAccount(accountId: Int, balance: Double, balanceType: String)
}