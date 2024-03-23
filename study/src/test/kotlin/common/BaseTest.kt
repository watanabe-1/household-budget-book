package common

import common.util.StudyTestUtil
import org.book.app.study.enums.type.AccountType
import org.book.app.study.model.entity.Account
import org.book.app.study.service.AppUserDetails
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

@SpringBootTest
class BaseTest {
    @BeforeEach
    protected fun setUp() {
        // test用tempディレクトリの作成
        StudyTestUtil.makeTestTempDir()
        // ログイン情報の設定
        val account =
            Account("", "userid", "password", "user", AccountType.ADMIN.code, "", null, null, null, null)
        val userDetails = AppUserDetails(account)
        val authentication = UsernamePasswordAuthenticationToken(
            userDetails, null,
            userDetails.authorities
        )
        SecurityContextHolder.getContext().authentication = authentication
    }

    @AfterEach
    protected fun tearDown() {
        // テンプディレクトリのクリーン
        StudyTestUtil.cleanTestTempDir()
        // ログイン情報のクリア
        SecurityContextHolder.clearContext()
    }
}
