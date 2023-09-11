import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import kotlin.test.assertNotNull

class AccountControllerTest {

    @Mock
    val mockConnection: Connection = mock(Connection::class.java)

    @Mock
    val mockPreparedStatement: PreparedStatement = mock(PreparedStatement::class.java)

    @Mock
    val mockResultSet: ResultSet = mock(ResultSet::class.java)

    @Mock
    val mockDataBaseConfig: DataBaseConfigInterface = mock(DataBaseConfigInterface::class.java)

    private lateinit var accountController: AccountController

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        accountController = AccountController(mockDataBaseConfig)

        // configura o mock de conexão
        `when`(mockDataBaseConfig.getConnection()).thenReturn(mockConnection)
    }

    @Test
    fun insertAccountTest() {
        val account = Account(
            id = 1,
            foodBalance = 20.0,
            mealBalance = 30.0,
            cashBalance = 40.0,
        )

        configurePrepareStatementMock()
        configurePreparedStatementMock(account)

        accountController.insertAccount(account)

        // verifica se o preparedStatement fez os chamados como o esperado
        verify(mockPreparedStatement).setInt(1, account.id)
        verify(mockPreparedStatement).setDouble(2, account.foodBalance)
        verify(mockPreparedStatement).setDouble(3, account.mealBalance)
        verify(mockPreparedStatement).setDouble(4, account.cashBalance)
        verify(mockPreparedStatement).executeUpdate()
    }

    @Test
    fun getAccountByIdTest() {
        val expectedAccount = Account(
            id = 1,
            foodBalance = 20.0,
            mealBalance = 30.0,
            cashBalance = 40.0,
        )

        configurePrepareStatementMock()
        configureResultSetMock(expectedAccount)
        `when`(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet)

        // executa função a ser testada
        val accountResult = accountController.getAccountById(1)

        // verifica se o resultado é o esperado
        assertNotNull(accountResult)
        assertEquals(expectedAccount.id, accountResult.id)
        assertEquals(expectedAccount.foodBalance, accountResult.foodBalance)
        assertEquals(expectedAccount.mealBalance, accountResult.mealBalance)
        assertEquals(expectedAccount.cashBalance, accountResult.cashBalance)
    }

    private fun configurePrepareStatementMock() {
        `when`(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement)
    }

    private fun configurePreparedStatementMock(account: Account) {
        doNothing().`when`(mockPreparedStatement).setInt(1, account.id)
        doNothing().`when`(mockPreparedStatement).setDouble(2, account.foodBalance)
        doNothing().`when`(mockPreparedStatement).setDouble(3, account.mealBalance)
        doNothing().`when`(mockPreparedStatement).setDouble(4, account.cashBalance)

        `when`(mockPreparedStatement.executeUpdate()).thenReturn(1)
    }

    private fun configureResultSetMock(account: Account) {
        `when`(mockResultSet.next()).thenReturn(true, false) // retorna apenas um valor
        `when`(mockResultSet.getInt("id")).thenReturn(account.id)
        `when`(mockResultSet.getDouble("foodBalance")).thenReturn(account.foodBalance)
        `when`(mockResultSet.getDouble("mealBalance")).thenReturn(account.mealBalance)
        `when`(mockResultSet.getDouble("cashBalance")).thenReturn(account.cashBalance)
    }
}
