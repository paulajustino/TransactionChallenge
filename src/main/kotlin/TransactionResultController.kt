class TransactionResultController {

    fun insertTransactionResult(transactionResult: TransactionResult) {
        val dbConnection = DataBaseConfig.getConnection()

        dbConnection?.let {
            val query = "INSERT INTO transaction_result (id, accountId, amount, merchant, mcc, status, message) VALUES (?, ?, ?, ?, ?, ?, ?)"

            val preparedStatement = dbConnection.prepareStatement(query)
            preparedStatement.setInt(1, transactionResult.id)
            preparedStatement.setInt(2, transactionResult.accountId)
            preparedStatement.setDouble(3, transactionResult.amount)
            preparedStatement.setString(4, transactionResult.merchant)
            preparedStatement.setString(5, transactionResult.mcc)
            preparedStatement.setString(6, transactionResult.status)
            preparedStatement.setString(7, transactionResult.message)

            preparedStatement.executeUpdate()

            preparedStatement.close()
            dbConnection.close()
        }
    }

    // busca de resultados de transações por id da conta
    fun getTransactionResultByAccountId(id: Int): List<TransactionResult?> {
        val dbConnection = DataBaseConfig.getConnection()
        val transactionResultList: MutableList<TransactionResult?> = mutableListOf()

        dbConnection?.let {
            val query = "SELECT * FROM transaction_result WHERE accountId = ?"

            val preparedStatement = dbConnection.prepareStatement(query)
            preparedStatement.setInt(1, id)

            val resultQuery = preparedStatement.executeQuery()

            while (resultQuery.next()) {
                transactionResultList.add(
                    TransactionResult(
                        id = resultQuery.getInt("id"),
                        accountId = resultQuery.getInt("accountId"),
                        amount = resultQuery.getDouble("amount"),
                        merchant = resultQuery.getString("merchant"),
                        mcc = resultQuery.getString("mcc"),
                        status = resultQuery.getString("status"),
                        message = resultQuery.getString("message"),
                    )
                )
            }

            resultQuery.close()
            preparedStatement.close()
            dbConnection.close()
        }
        return transactionResultList
    }

    // busca de resultados de transações pelo nome do estabelecimento
    fun getTransactionResultByMerchant(merchant: String): List<TransactionResult?> {
        val dbConnection = DataBaseConfig.getConnection()
        val transactionResultList: MutableList<TransactionResult?> = mutableListOf()

        dbConnection?.let {
            val query = "SELECT * FROM transaction_result WHERE merchant = ?"

            val preparedStatement = dbConnection.prepareStatement(query)
            preparedStatement.setString(1, merchant)

            val resultQuery = preparedStatement.executeQuery()

            while (resultQuery.next()) {
                transactionResultList.add(
                    TransactionResult(
                        id = resultQuery.getInt("id"),
                        accountId = resultQuery.getInt("accountId"),
                        amount = resultQuery.getDouble("amount"),
                        merchant = resultQuery.getString("merchant"),
                        mcc = resultQuery.getString("mcc"),
                        status = resultQuery.getString("status"),
                        message = resultQuery.getString("message"),
                    )
                )
            }

            resultQuery.close()
            preparedStatement.close()
            dbConnection.close()
        }
        return transactionResultList
    }
}