class AccountController {

/*    init {
        val accounts = createAccounts()
        for (account in accounts)
            insertAccount(account)
    }*/

    private fun createAccounts(): MutableList<Account> {
        val accounts: MutableList<Account> = mutableListOf()

        accounts.add(Account(id = 1, foodBalance = 0.0, mealBalance = 25.0, cashBalance = 50.0))
        accounts.add(Account(id = 2, foodBalance = 25.0, mealBalance = 50.0, cashBalance = 100.0))
        accounts.add(Account(id = 3, foodBalance = 50.0, mealBalance = 75.0, cashBalance = 150.0))
        accounts.add(Account(id = 4, foodBalance = 100.0, mealBalance = 100.0, cashBalance = 50.0))

        return accounts
    }

    private fun insertAccount(account: Account) {
        val dbConnection = DataBaseConfig.getConnection()

        dbConnection?.let {
            val query = "INSERT INTO account (id, foodBalance, mealBalance, cashBalance) VALUES (?, ?, ?, ?)"

            val preparedStatement = dbConnection.prepareStatement(query)
            preparedStatement.setInt(1, account.id)
            preparedStatement.setDouble(2, account.foodBalance)
            preparedStatement.setDouble(3, account.mealBalance)
            preparedStatement.setDouble(4, account.cashBalance)

            preparedStatement.executeUpdate()

            preparedStatement.close()
            dbConnection.close()
        }
    }

    fun getAccountById(id: Int): Account? {
        val dbConnection = DataBaseConfig.getConnection()
        var account: Account? = null

        dbConnection?.let {
            val query = "SELECT * FROM account WHERE id = ?"

            val preparedStatement = dbConnection.prepareStatement(query)
            preparedStatement.setInt(1, id)

            val resultQuery = preparedStatement.executeQuery()

            if (resultQuery.next()) {
                account = Account(
                    id = resultQuery.getInt("id"),
                    foodBalance = resultQuery.getDouble("foodBalance"),
                    mealBalance = resultQuery.getDouble("mealBalance"),
                    cashBalance = resultQuery.getDouble("cashBalance"),
                )
            }

            resultQuery.close()
            preparedStatement.close()
            dbConnection.close()
        }
        return account
    }

    fun updateBalanceAccount(accountId: Int, balance: Double, balanceType: String) {
        val dbConnection = DataBaseConfig.getConnection()

        dbConnection?.let {
            val query = "UPDATE account SET $balanceType = ? WHERE id = ?"

            val preparedStatement = dbConnection.prepareStatement(query)
                preparedStatement.setDouble(1, balance)
                preparedStatement.setInt(2, accountId)


            val rowUpdated = preparedStatement.executeUpdate()

            dbConnection.close()

            if (rowUpdated > 0) {
                println("Saldo em conta atualizado com sucesso.")
            } else {
                println("Erro: Saldo em conta não atualizado.")
            }
        }
    }
}