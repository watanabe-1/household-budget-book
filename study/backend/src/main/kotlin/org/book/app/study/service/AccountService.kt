package org.book.app.study.service

import org.book.app.study.mapper.AccountMapper
import org.book.app.study.model.entity.Account
import org.book.app.study.util.StudyUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * ACCOUNT:アカウント(アカウント情報保持テーブル)のserviceクラス
 */
@Service
class AccountService(
    private val accountMapper: AccountMapper
) {

    /**
     * 全検索
     *
     * @return 検索結果(複数行)
     */
    fun findAll(): List<Account> {
        return accountMapper.findAll()
    }

    /**
     * 1行検索(引数にプライマルキーを指定)
     *
     * @param userId USER_ID(ユーザーID)
     * @return 検索結果(1行)
     */
    fun findOne(userId: String): Account? {
        return accountMapper.findOne(userId)
    }

    /**
     * 複数行insert
     *
     * @param accList entity(Account)のList
     * @return insert行数
     */
    @Transactional
    fun saveBulk(accList: List<Account>): Int {
        return accountMapper.saveBulk(accList)
    }

    /**
     * 1行insert
     *
     * @param acc entity(Account)
     * @return insert行数
     */
    @Transactional
    fun saveOne(acc: Account): Int {
        return accountMapper.saveOne(acc)
    }

    /**
     * 全行update
     *
     * @param acc entity(Account)
     * @return update行数
     */
    @Transactional
    fun updateAll(acc: Account): Int {
        return accountMapper.updateAll(acc)
    }

    /**
     * 1行update プライマルキーをWhere句に指定 プライマルキー：String userId
     *
     * @param acc entity(Account)
     * @param userId USER_ID(ユーザーID)
     * @return update行数
     */
    @Transactional
    fun updateOne(acc: Account, userId: String): Int {
        return accountMapper.updateOne(acc, userId)
    }

    /**
     * リフレッシュトークンアップデート プライマルキーをWhere句に指定 プライマルキー：String userId
     *
     * @param refreshToken リフレッシュトークン
     * @param userId USER_ID(ユーザーID)
     * @return update行数
     */
    @Transactional
    fun updateRefreshToken(refreshToken: String, userId: String): Int {
        val acc = Account(
            "",
            "",
            "",
            "",
            "",
            refreshToken,
            null,
            null,
            StudyUtils.getNowDate().toLocalDateTime(),
            userId
        )

        return accountMapper.updateRefreshToken(acc, userId)
    }

    /**
     * 全行delete
     *
     * @return delete行数
     */
    @Transactional
    fun deleteAll(): Int {
        return accountMapper.deleteAll()
    }

    /**
     * 1行delete(引数にプライマルキーを指定)
     *
     * @param userId USER_ID(ユーザーID)
     * @return delete行数
     */
    @Transactional
    fun deleteOne(userId: String): Int {
        return accountMapper.deleteOne(userId)
    }
}
