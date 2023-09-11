import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import kotlin.test.Test

class TransactionResultControllerTest {

    @Mock
    val mockConnection: Connection = mock(Connection::class.java)

    @Mock
    val mockPreparedStatement: PreparedStatement = mock(PreparedStatement::class.java)

    @Mock
    val mockResultSet: ResultSet = mock(ResultSet::class.java)

    @Mock
    val mockDataBaseConfig: DataBaseConfigInterface = mock(DataBaseConfigInterface::class.java)

    private lateinit var transactionResultController: TransactionResultController

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        transactionResultController = TransactionResultController(mockDataBaseConfig)

        // configuração do mock de conexao com o banco
        `when`(mockDataBaseConfig.getConnection()).thenReturn(mockConnection)
    }


    @Test
    fun insertTransactionResultTest() {
        val transactionResult = TransactionResult(
            id = 1,
            accountId = 1,
            amount = 20.0,
            merchant = "Restaurante Tavares",
            mcc = "5811",
            status = TransactionStatus.APPROVED.status,
            message = TransactionStatusMessage.APPROVED_MESSAGE.statusMessage
        )

        configurePrepareStatementMock()
        configurePreparedStatementMock(transactionResult)

        transactionResultController.insertTransactionResult(transactionResult)

        // verifica se o preparedStatement fez os chamados como o esperado
        verify(mockPreparedStatement).setInt(1, transactionResult.id)
        verify(mockPreparedStatement).setInt(2, transactionResult.accountId)
        verify(mockPreparedStatement).setDouble(3, transactionResult.amount)
        verify(mockPreparedStatement).setString(4, transactionResult.merchant)
        verify(mockPreparedStatement).setString(5, transactionResult.mcc)
        verify(mockPreparedStatement).setString(6, transactionResult.status)
        verify(mockPreparedStatement).setString(7, transactionResult.message)
        verify(mockPreparedStatement).executeUpdate()
    }

    @Test
    fun getTransactionResultByAccountIdTest() {
        val transactionResult = TransactionResult(
            id = 1,
            accountId = 1,
            amount = 20.0,
            merchant = "Restaurante Tavares",
            mcc = "5811",
            status = TransactionStatus.APPROVED.status,
            message = TransactionStatusMessage.APPROVED_MESSAGE.statusMessage
        )

        configurePrepareStatementMock()
        configureResultSetMock(transactionResult)
        `when`(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet)

        val transactionResults = transactionResultController.getTransactionResultByAccountId(1)

        // verifica se o resultado é o esperado
        assertEquals(1, transactionResults.size)
        assertEquals(transactionResult.id, transactionResults[0]?.id)
        assertEquals(transactionResult.accountId, transactionResults[0]?.accountId)
        assertEquals(transactionResult.amount, transactionResults[0]?.amount)
        assertEquals(transactionResult.merchant, transactionResults[0]?.merchant)
        assertEquals(transactionResult.mcc, transactionResults[0]?.mcc)
        assertEquals(transactionResult.status, transactionResults[0]?.status)
        assertEquals(transactionResult.message, transactionResults[0]?.message)
    }

    @Test
    fun getTransactionResultByMerchantTest() {
        val transactionResult = TransactionResult(
            id = 1,
            accountId = 1,
            amount = 20.0,
            merchant = "Restaurante Tavares",
            mcc = "5811",
            status = TransactionStatus.APPROVED.status,
            message = TransactionStatusMessage.APPROVED_MESSAGE.statusMessage
        )

        configurePrepareStatementMock()
        configureResultSetMock(transactionResult)
        `when`(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet)

        val transactionResults = transactionResultController.getTransactionResultByMerchant("Restaurante Tavares")

        // verifica se o resultSet foi processado com os valores esperados
        assertEquals(1, transactionResults.size)
        assertEquals(transactionResult.id, transactionResults[0]?.id)
        assertEquals(transactionResult.accountId, transactionResults[0]?.accountId)
        assertEquals(transactionResult.amount, transactionResults[0]?.amount)
        assertEquals(transactionResult.merchant, transactionResults[0]?.merchant)
        assertEquals(transactionResult.mcc, transactionResults[0]?.mcc)
        assertEquals(transactionResult.status, transactionResults[0]?.status)
        assertEquals(transactionResult.message, transactionResults[0]?.message)
    }

    private fun configureResultSetMock(transactionResult: TransactionResult) {
        `when`(mockResultSet.next()).thenReturn(true, false) // retorna apenas um valor
        `when`(mockResultSet.getInt("id")).thenReturn(transactionResult.id)
        `when`(mockResultSet.getInt("accountId")).thenReturn(transactionResult.accountId)
        `when`(mockResultSet.getDouble("amount")).thenReturn(transactionResult.amount)
        `when`(mockResultSet.getString("merchant")).thenReturn(transactionResult.merchant)
        `when`(mockResultSet.getString("mcc")).thenReturn(transactionResult.mcc)
        `when`(mockResultSet.getString("status")).thenReturn(transactionResult.status)
        `when`(mockResultSet.getString("message")).thenReturn(transactionResult.message)
    }

    private fun configurePrepareStatementMock() {
        `when`(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement)
    }

    private fun configurePreparedStatementMock(transactionResult: TransactionResult) {
        doNothing().`when`(mockPreparedStatement).setInt(1, transactionResult.id)
        doNothing().`when`(mockPreparedStatement).setInt(2, transactionResult.accountId)
        doNothing().`when`(mockPreparedStatement).setDouble(3, transactionResult.amount)
        doNothing().`when`(mockPreparedStatement).setString(4, transactionResult.merchant)
        doNothing().`when`(mockPreparedStatement).setString(5, transactionResult.mcc)
        doNothing().`when`(mockPreparedStatement).setString(6, transactionResult.status)
        doNothing().`when`(mockPreparedStatement).setString(7, transactionResult.message)

        `when`(mockPreparedStatement.executeUpdate()).thenReturn(1)
    }
}