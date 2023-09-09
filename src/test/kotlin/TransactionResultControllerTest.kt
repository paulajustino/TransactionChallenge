import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import kotlin.test.Test

const val URL_DB = "jdbc:mysql://localhost:3306/db_transaction_challenge"
const val USER_DB = "root"
const val PASSWORD_DB = "889d9f04b4b69d8a112f4f377ccacca535c8ef16a20a4f1ab61070f37b8d5ac1"

class TransactionResultControllerTest {

    @Mock
    lateinit var mockConnection: Connection

    @Mock
    lateinit var mockPreparedStatement: PreparedStatement

    @Mock
    lateinit var mockResultSet: ResultSet

    @Mock
    lateinit var mockDataBaseConfig: DataBaseConfigInterface

    private lateinit var controller: TransactionResultController

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        controller = TransactionResultController()

        // configuração dos mocks
        `when`(mockConnectionProvider.getConnection()).thenReturn(mockConnection)
        `when`(mockConnection.prepareStatement("INSERT INTO transaction_result...")).thenReturn(mockPreparedStatement)
        `when`(mockConnection.prepareStatement("SELECT * FROM transaction_result WHERE accountId = ?")).thenReturn(
            mockPreparedStatement
        )
        `when`(mockConnection.prepareStatement("SELECT * FROM transaction_result WHERE merchant = ?")).thenReturn(
            mockPreparedStatement
        )
        `when`(mockPreparedStatement.executeUpdate()).thenReturn(1)

        `when`(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet)
        `when`(mockResultSet.next()).thenReturn(true, false)
        `when`(mockResultSet.getInt("id")).thenReturn(1)
        `when`(mockResultSet.getInt("accountId")).thenReturn(1)
        `when`(mockResultSet.getDouble("amount")).thenReturn(20.0)
        `when`(mockResultSet.getString("merchant")).thenReturn("Restaurante Tavares")
        `when`(mockResultSet.getString("mcc")).thenReturn("5811")
        `when`(mockResultSet.getString("status")).thenReturn("APPROVED")
        `when`(mockResultSet.getString("message")).thenReturn("Approved Message")
    }

    @Test
    fun testInsertTransactionResult() {
        val transactionResult = TransactionResult(
            id = 1,
            accountId = 1,
            amount = 20.0,
            merchant = "Restaurante Tavares",
            mcc = "5811",
            status = "APPROVED",
            message = "Approved Message"
        )

        controller.insertTransactionResult(transactionResult)

        // Verificação de que o PreparedStatement foi chamado com os parâmetros corretos
        // (Você pode adicionar mais verificações conforme necessário)
        verify(mockPreparedStatement).setInt(1, 1)
        verify(mockPreparedStatement).setInt(2, 1)
        verify(mockPreparedStatement).setDouble(3, 20.0)
        verify(mockPreparedStatement).setString(4, "Restaurante Tavares")
        verify(mockPreparedStatement).setString(5, "5811")
        verify(mockPreparedStatement).setString(6, "APPROVED")
        verify(mockPreparedStatement).setString(7, "Approved Message")
    }

    @Test
    fun testGetTransactionResultByAccountId() {
        val transactionResults = controller.getTransactionResultByAccountId(1)

        // Verificação de que o ResultSet foi processado corretamente
        assertEquals(1, transactionResults.size)
        assertEquals(1, transactionResults[0]?.id)
        assertEquals(1, transactionResults[0]?.accountId)
        assertEquals(20.0, transactionResults[0]?.amount)
        assertEquals("Restaurante Tavares", transactionResults[0]?.merchant)
        assertEquals("5811", transactionResults[0]?.mcc)
        assertEquals("APPROVED", transactionResults[0]?.status)
        assertEquals("Approved Message", transactionResults[0]?.message)
    }

    @Test
    fun testGetTransactionResultByMerchant() {
        val transactionResults = controller.getTransactionResultByMerchant("Restaurante Tavares")

        // Verificação de que o ResultSet foi processado corretamente
        assertEquals(1, transactionResults.size)
        assertEquals(1, transactionResults[0]?.id)
        assertEquals(1, transactionResults[0]?.accountId)
        assertEquals(20.0, transactionResults[0]?.amount)
        assertEquals("Restaurante Tavares", transactionResults[0]?.merchant)
        assertEquals("5811", transactionResults[0]?.mcc)
        assertEquals("APPROVED", transactionResults[0]?.status)
        assertEquals("Approved Message", transactionResults[0]?.message)
    }
}