import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class TransactionControllerTest {

    @Mock
    lateinit var mockAccountController: AccountControllerInterface

    @Mock
    lateinit var mockTransactionResultController: TransactionResultControllerInterface

    private lateinit var transactionController: TransactionController

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        // inicializa classe com os mocks
        transactionController = TransactionController(mockAccountController, mockTransactionResultController)
    }

    @Test
    fun processTransactionTest_AccountNotFound() {
        val transaction = Transaction(
            id = 1,
            accountId = 123,
            amount = 50.0,
            merchant = "Açai da Bene",
            mcc = "5411"
        )

        // configura mock para retornar null quando buscar conta
        `when`(mockAccountController.getAccountById(123)).thenReturn(null)

        // executa a função a ser testada
        transactionController.processTransaction(transaction)

        val expectedTransactionResult = TransactionResult(
            id = transaction.id,
            accountId = transaction.accountId,
            amount = transaction.amount,
            merchant = transaction.merchant,
            mcc = transaction.mcc,
            status = TransactionStatus.DENIED.status,
            message = TransactionStatusMessage.INVALID_ACCOUNT_ID_MESSAGE.statusMessage
        )

        // verifica se o insertTransactionResult foi chamado com os parametros esperados
        verify(mockTransactionResultController).insertTransactionResult(expectedTransactionResult)
    }

    @Test
    fun processTransactionTest_InsufficientBalance() {
        val transaction = Transaction(
            id = 1,
            accountId = 123,
            amount = 50.0,
            merchant = "Açai da Bene",
            mcc = "5411"
        )

        // configura mock para retornar saldo insuficiente
        `when`(mockAccountController.getAccountById(123)).thenReturn(
            Account(
                id = 123,
                foodBalance = 20.0,
                mealBalance = 30.0,
                cashBalance = 40.0
            )
        )

        // executa função a ser testada
        transactionController.processTransaction(transaction)

        val expectedTransactionResult = TransactionResult(
            id = transaction.id,
            accountId = transaction.accountId,
            amount = transaction.amount,
            merchant = transaction.merchant,
            mcc = transaction.mcc,
            status = TransactionStatus.DENIED.status,
            message = TransactionStatusMessage.INSUFFICIENT_BALANCE_MESSAGE.statusMessage
        )

        // verifica se o insertTransactionResult foi chamado com os parametros esperados
        verify(mockTransactionResultController).insertTransactionResult(expectedTransactionResult)
    }

    @Test
    fun processTransactionTest_Approved() {
        val transaction = Transaction(
            id = 1,
            accountId = 123,
            amount = 20.0,
            merchant = "Açai da Bene",
            mcc = "5811"
        )

        // configura mock para retornar saldo suficiente e atualizar saldo da conta
        `when`(mockAccountController.getAccountById(123)).thenReturn(
            Account(
                id = 123,
                foodBalance = 50.0,
                mealBalance = 30.0,
                cashBalance = 40.0
            )
        )
        doNothing().`when`(mockAccountController).updateBalanceAccount(123, 30.0, "mealBalance")

        // executa função a ser testada
        transactionController.processTransaction(transaction)

        val expectedTransactionResult = TransactionResult(
            id = 1,
            accountId = 123,
            amount = 20.0,
            merchant = "Açai da Bene",
            mcc = "5811",
            status = TransactionStatus.APPROVED.status,
            message = TransactionStatusMessage.APPROVED_MESSAGE.statusMessage
        )

        // verifica se o insertTransactionResult foi chamado com os argumentos esperados
        verify(mockTransactionResultController).insertTransactionResult(expectedTransactionResult)

        // verifica se o updateBalanceAccount foi chamado com os argumentos esperados
        verify(mockAccountController).updateBalanceAccount(123, 10.0, "mealBalance")
    }
}
