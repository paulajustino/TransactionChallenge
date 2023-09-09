import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class AccountControllerTest {

    @Mock
    lateinit var mockConnection: Connection

    @Mock
    lateinit var mockPreparedStatement: PreparedStatement

    @Mock
    lateinit var mockResultSet: ResultSet

    @Mock
    lateinit var mockDataBaseConfig: DataBaseConfigInterface

    private lateinit var accountController: AccountController

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        // configura o mock de conexão
        `when`(mockDataBaseConfig.getConnection()).thenReturn(mockConnection)

        // configura o mock de consulta
        `when`(mockConnection.prepareStatement("SELECT * FROM account WHERE id = ?")).thenReturn(mockPreparedStatement)
        `when`(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet)

        // inicializa classe com mock
        accountController = AccountController(mockDataBaseConfig)
    }

    @Test
    fun getAccountByIdTest() {
        // configura mock esperado
        `when`(mockResultSet.next()).thenReturn(true) // Simular que existe um resultado
        `when`(mockResultSet.getInt("id")).thenReturn(1)
        `when`(mockResultSet.getDouble("foodBalance")).thenReturn(20.0)
        `when`(mockResultSet.getDouble("mealBalance")).thenReturn(30.0)
        `when`(mockResultSet.getDouble("cashBalance")).thenReturn(40.0)

        // executa função a ser testada
        val account = accountController.getAccountById(1)

        // verifica se o resultado é o esperado
        assert(account != null)
        assert(account?.id == 1)
        assert(account?.foodBalance == 20.0)
        assert(account?.mealBalance == 30.0)
        assert(account?.cashBalance == 40.0)
    }
}
