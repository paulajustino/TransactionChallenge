class TransactionResultController {

    fun insertTransactionResult(transactionResult: TransactionResult) {
        val dbConnection = DataBaseConfig.getConnection()

        dbConnection?.let {
            val query = "INSERT INTO transaction_result (id, accountId, status, message) VALUES (?, ?, ?, ?)"

            val preparedStatement = dbConnection.prepareStatement(query)
            preparedStatement.setInt(1, transactionResult.id)
            preparedStatement.setInt(2, transactionResult.accountId)
            preparedStatement.setString(3, transactionResult.status)
            preparedStatement.setString(4, transactionResult.message)

            preparedStatement.executeUpdate()

            preparedStatement.close()
            dbConnection.close()
        }
    }
}