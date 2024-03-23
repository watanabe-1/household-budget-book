package org.book.app.common.filter

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.slf4j.MDC
import java.util.*

/**
 * ログ出力追跡用にリクエストごとに一意のIDを振り
 * logback-sprig.xml内で%X{requestid}として参照できる
 */
class UniqueRequestIDFilter : Filter {

    companion object {
        const val REQUEST_ID = "requestid"
    }
    
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        try {
            // リクエストに一意のIDを割り当てる
            val uniqueID = UUID.randomUUID().toString()
            MDC.put(REQUEST_ID, uniqueID)

            chain.doFilter(request, response)
        } finally {
            // リクエスト処理が終了したらMDCからIDを削除
            MDC.remove(REQUEST_ID)
        }
    }
}
