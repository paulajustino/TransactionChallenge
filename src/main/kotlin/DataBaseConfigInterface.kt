import java.sql.Connection

interface DataBaseConfigInterface {
    fun getConnection(): Connection?
}