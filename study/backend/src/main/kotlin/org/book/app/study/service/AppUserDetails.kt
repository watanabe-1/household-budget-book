package org.book.app.study.service

import org.book.app.study.enums.type.AccountType
import org.book.app.study.model.entity.Account
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User

/**
 * ユーザ情報
 */
class AppUserDetails(val account: Account) :
    User(account.userId, account.password, AuthorityUtils.createAuthorityList(*getAuthorityList(account.accountType))) {

    companion object {
        private const val serialVersionUID = 1L

        /**
         * コードに応じたアカウント権限を取得
         *
         * @param accountType アカウントタイプ
         * @return 対応する権限
         */
        private fun getAuthorityList(accountType: String): Array<String> {
            return arrayOf(AccountType.codeOf(accountType).role)
        }
    }
}