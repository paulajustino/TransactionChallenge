import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

const val URL_DB = "jdbc:mysql://localhost:3306/db_transaction_challenge"
const val USER_DB = "root"
const val PASSWORD_DB = "889d9f04b4b69d8a112f4f377ccacca535c8ef16a20a4f1ab61070f37b8d5ac1"

class DataBaseConfig {
    companion object {
        fun getConnection(): Connection? {
            return try {
                DriverManager.getConnection(URL_DB, USER_DB, PASSWORD_DB)
            } catch (e: SQLException) {
                null
            }
        }
    }
}