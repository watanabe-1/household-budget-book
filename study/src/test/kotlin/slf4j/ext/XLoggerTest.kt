package slf4j.ext


import common.BaseTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import org.slf4j.Logger
import org.slf4j.ext.XLogger
import org.springframework.boot.test.context.SpringBootTest
import java.util.function.Supplier

@SpringBootTest(classes = [XLoggerTest::class])
internal class XLoggerTest : BaseTest() {
    private lateinit var mockLogger: Logger
    private lateinit var xLogger: XLogger

    @BeforeEach
    override fun setUp() {
        super.setUp()
        mockLogger = mock(Logger::class.java)
        xLogger = XLogger(mockLogger)
    }

    @Test
    fun `test info logging`() {
        `when`(mockLogger.isInfoEnabled).thenReturn(true)
        xLogger.info("testId", "arg1", "arg2")

        verifyLogMessage("info")
    }

    @Test
    fun `test warn logging`() {
        `when`(mockLogger.isWarnEnabled).thenReturn(true)
        xLogger.warn("testId", "arg1", "arg2")

        verifyLogMessage("warn")
    }

    @Test
    fun `test error logging`() {
        `when`(mockLogger.isErrorEnabled).thenReturn(true)
        xLogger.error("testId", "arg1", "arg2")

        verifyLogMessage("error")
    }

    @Test
    fun `test trace logging`() {
        `when`(mockLogger.isTraceEnabled).thenReturn(true)
        xLogger.trace("testId", "arg1", "arg2")

        verifyLogMessage("trace")
    }

    @Test
    fun `test info logging with supplier`() {
        `when`(mockLogger.isInfoEnabled).thenReturn(true)
        val supplier: Supplier<Array<Any>?> = Supplier { arrayOf("arg1", "arg2") }
        xLogger.info("testId", supplier)

        verifyLogMessage("info")
    }

    @Test
    fun `test warn logging with supplier`() {
        `when`(mockLogger.isWarnEnabled).thenReturn(true)
        val supplier: Supplier<Array<Any>?> = Supplier { arrayOf("arg1", "arg2") }
        xLogger.warn("testId", supplier)

        verifyLogMessage("warn")
    }

    @Test
    fun `test error logging with supplier`() {
        `when`(mockLogger.isErrorEnabled).thenReturn(true)
        val supplier: Supplier<Array<Any>?> = Supplier { arrayOf("arg1", "arg2") }
        xLogger.error("testId", supplier)

        verifyLogMessage("error")
    }

    @Test
    fun `test trace logging with supplier`() {
        `when`(mockLogger.isTraceEnabled).thenReturn(true)
        val supplier: Supplier<Array<Any>?> = Supplier { arrayOf("arg1", "arg2") }
        xLogger.trace("testId", supplier)

        verifyLogMessage("trace")
    }

    @Test
    fun `test warn logging with exception`() {
        `when`(mockLogger.isWarnEnabled).thenReturn(true)
        val testException = Exception("Test Exception")
        xLogger.warn("testId", testException, "arg1", "arg2")

        verify(mockLogger).warn(anyString(), eq(testException))
    }

    @Test
    fun `test error logging with exception`() {
        `when`(mockLogger.isErrorEnabled).thenReturn(true)
        val testException = Exception("Test Exception")
        xLogger.error("testId", testException, "arg1", "arg2")

        verify(mockLogger).error(anyString(), eq(testException))
    }

    private fun verifyLogMessage(logLevel: String) {
        val messageCaptor: ArgumentCaptor<String> = ArgumentCaptor.forClass(String::class.java)
        when (logLevel) {
            "info" -> verify(mockLogger).info(messageCaptor.capture())
            "warn" -> verify(mockLogger).warn(messageCaptor.capture())
            "error" -> verify(mockLogger).error(messageCaptor.capture())
            "trace" -> verify(mockLogger).trace(messageCaptor.capture())
        }
        val loggedMessage: String = messageCaptor.value
        assert(loggedMessage.matches("\\[testId] .*".toRegex()))
        assert(loggedMessage.contains("arg1") || loggedMessage.contains("arg2"))
    }
}
