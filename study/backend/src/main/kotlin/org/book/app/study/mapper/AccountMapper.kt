package org.book.app.study.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.book.app.study.model.entity.Account

/**
 * ACCOUNT:アカウント(アカウント情報保持テーブル)のmapperクラス
 */
@Mapper
interface AccountMapper {
    /**
     * 全検索
     *
     * @return 検索結果(複数行)
     */
    fun findAll(): List<Account>

    /**
     * 1行検索(引数にプライマルキーを指定)
     *
     * @param userId USER_ID(ユーザーID)
     * @return 検索結果(1行)
     */
    fun findOne(@Param("userId") userId: String): Account?

    /**
     * 複数行insert
     *
     * @param accList entity(Account)のList
     * @return insert行数
     */
    fun saveBulk(@Param("accList") accList: List<Account>): Int

    /**
     * 1行insert
     *
     * @param acc entity(Account)
     * @return insert行数
     */
    fun saveOne(acc: Account): Int

    /**
     * 全行update
     *
     * @param acc entity(Account)
     * @return update行数
     */
    fun updateAll(acc: Account): Int

    /**
     * 1行update プライマルキーをWhere句に指定 プライマルキー：@Param("userIdWhere")String userId
     *
     * @param acc    entity(Account)
     * @param userId USER_ID(ユーザーID)
     * @return update行数
     */
    fun updateOne(@Param("acc") acc: Account, @Param("userIdWhere") userId: String): Int

    /**
     * リフレッシュトークンアップデート プライマルキーをWhere句に指定 プライマルキー：@Param("userIdWhere")String userId
     *
     * @param acc    entity(Account)
     * @param userId USER_ID(ユーザーID)
     * @return update行数
     */
    fun updateRefreshToken(@Param("acc") acc: Account, @Param("userIdWhere") userId: String): Int

    /**
     * 全行delete
     *
     * @return delete行数
     */
    fun deleteAll(): Int

    /**
     * 1行delete(引数にプライマルキーを指定)
     *
     * @param userId USER_ID(ユーザーID)
     * @return delete行数
     */
    fun deleteOne(@Param("userId") userId: String): Int
}
