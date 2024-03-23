package org.book.app.study.util

import org.book.app.common.exception.BusinessException
import org.mozilla.universalchardet.UniversalDetector
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.InputStreamSource
import java.io.*
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util.stream.Collectors

/**
 * ファイルを扱うutilクラス
 */
object StudyFileUtils {

    private val logger = logger<StudyFileUtils>()

    /**
     * csvの拡張子
     */
    const val EXTENSION_BY_CSV: String = "csv"

    /**
     * tsvの拡張子
     */
    const val EXTENSION_BY_TSV: String = "tsv"

    /**
     * txtのメディアタイプ
     */
    const val MEDIATYPE_BY_TEXT: String = "text/plain"

    /**
     * csvのメディアタイプ
     */
    const val MEDIATYPE_BY_CSV: String = "text/csv"

    /**
     * tsvのメディアタイプ
     */
    const val MEDIATYPE_BY_TSV: String = "text/tsv"

    /**
     * ファイル関係のエラーメッセージ
     */
    private const val FILE_ERROR_MSG_CD = "1.01.01.1009"

    /**
     * 拡張子を追加
     *
     * @param fileName  追加対象
     * @param extension 追加する拡張子
     * @return 拡張子を追加したファイル名
     */
    fun addExtension(fileName: String, extension: String?): String =
        if (extension != null && !fileName.endsWith(".$extension")) "$fileName.$extension" else fileName

    /**
     * csvファイルを読み込む
     *
     * @param inputSource InputStreamSource
     * @param isHeader ヘッダーをつけるか
     * @return List
     */
    inline fun <reified T : Any> csvFileToList(
        inputSource: InputStreamSource,
        isHeader: Boolean
    ): List<T> = fileToListByCsvMapper(
        inputSource, detectFileEncoding(inputSource), T::class.java,
        StudyStringUtils.SEPARATOR_BY_CSV, isHeader, true
    )


    /**
     * tsvファイルを読み込む
     *
     * @param inputSource InputStreamSource
     * @param isHeader ヘッダーをつけるか
     * @return List
     */
    inline fun <reified T : Any> tsvFileToList(
        inputSource: InputStreamSource,
        isHeader: Boolean
    ): List<T> = fileToListByCsvMapper(
        inputSource, detectFileEncoding(inputSource), T::class.java,
        StudyStringUtils.SEPARATOR_BY_TSV, isHeader, false
    )


    /**
     * テキストファイルを読み込む
     *
     * @param inputSource    InputStreamSource
     * @param charsetName 文字コード
     * @param pojoType    カラム情報が記載されているクラス
     * @param sep         区切り文字
     * @param isHeader    ヘッダーをつけるか
     * @param isQuote     文字列にダブルクオートをつけるか
     * @return List
     */
    fun <T> fileToListByCsvMapper(
        inputSource: InputStreamSource, charsetName: Charset,
        pojoType: Class<T>?, sep: Char, isHeader: Boolean, isQuote: Boolean
    ): List<T> = try {
        inputSource.inputStream.use { inputStream ->
            inputStream.bufferedReader(charsetName).use { reader ->
                StudyJacksonUtils.objectToListByCsvMapper(reader, pojoType, sep, isHeader, isQuote)
            }
        }
    } catch (e: IOException) {
        throw BusinessException(FILE_ERROR_MSG_CD, e.message)
    }

    /**
     * ファイルの文字コードを判定
     *
     * @param file アップロードされたInputStreamSource(MultipartFilefil、ClassPathResourceなど)データ
     * @return result 文字コード
     */
    fun detectFileEncoding(file: InputStreamSource): Charset {
        return try {
            file.inputStream.use { inputStream ->
                detectFileEncoding(inputStream)
            }
        } catch (e: IOException) {
            throw BusinessException(FILE_ERROR_MSG_CD, e.message)
        }
    }

    /**
     * ファイルの文字コードを判定
     *
     * @param file アップロードされたfileデータ
     * @return result 文字コード
     */
    fun detectFileEncoding(file: File?): Charset {
        file ?: throw BusinessException(FILE_ERROR_MSG_CD, "File is null")
        return try {
            FileInputStream(file).use { inputStream ->
                detectFileEncoding(inputStream)
            }
        } catch (e: IOException) {
            throw BusinessException(FILE_ERROR_MSG_CD, e.message)
        }
    }

    /**
     * ファイルの文字コードを判定
     *
     * @param inputStream fileのInputStream
     * @return result 文字コード
     */
    fun detectFileEncoding(inputStream: InputStream): Charset {
        return inputStream.use { stream ->
            val buf = ByteArray(4096)
            val detector = UniversalDetector(null)

            var nread: Int = stream.read(buf)
            while (nread > 0 && !detector.isDone) {
                detector.handleData(buf, 0, nread)
                nread = stream.read(buf)
            }
            detector.dataEnd()

            Charset.forName(detector.detectedCharset ?: StandardCharsets.UTF_8.name())
        }
    }

    /**
     * クラスパス配下のファイルの読み込み
     *
     * @param path        パス
     * @param charsetName 文字コード
     * @return 読み込んだファイルの中身
     */
    fun readClassPathFile(path: String, charsetName: Charset): String? {
        return try {
            ClassPathResource(path).inputStream.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream, charsetName)).use { reader ->
                    reader.lines().collect(Collectors.joining()).also {
                        logger.info("1.03.01.1008", path)
                    }
                }
            }
        } catch (e: IOException) {
            throw BusinessException(FILE_ERROR_MSG_CD, e.message)
        }
    }

    /**
     * ファイルを削除
     *
     * @param file 削除対象ファイル
     */
    fun deleteFile(file: File?) {
        if (file != null && file.exists()) {
            try {
                Files.delete(file.toPath())
            } catch (e: IOException) {
                throw BusinessException(FILE_ERROR_MSG_CD, e.message)
            }
        }
    }
}
