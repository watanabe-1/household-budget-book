package common.util

import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException

object StudyTestUtil {
    val testTempDirPath: File
        /**
         * test用のtempディレクトリのパスを返す
         */
        get() {
            // システムの一時ディレクトリを取得
            val tempDir = System.getProperty("java.io.tmpdir")
            return File(tempDir, "household-budget-book/test")
        }

    /**
     * test用のtempディレクトリの作成
     */
    fun makeTestTempDir() {
        // テストディレクトリの作成
        if (!testTempDirPath.exists()) {
            val created = testTempDirPath.mkdirs() // ディレクトリを作成
            if (!created) {
                // ディレクトリの作成に失敗した場合のエラーハンドリング
                throw IOException("Failed to create directory ${testTempDirPath.absolutePath}")
            }
        }
    }

    /**
     * test用のtempディレクトリの中のファイルの削除
     */
    fun cleanTestTempDir() {
        // テストディレクトリのクリーンアップ
        try {
            FileUtils.cleanDirectory(testTempDirPath)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
