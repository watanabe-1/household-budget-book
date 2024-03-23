package org.book.app.common.logging

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.book.app.study.util.logger
import org.springframework.context.annotation.PropertySource
import org.springframework.core.MethodParameter
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.util.*
import java.util.function.Supplier
import java.util.stream.Collectors

@PropertySource("classpath:config/properties/logger.properties")
class ReauestLoggingListener : HandlerInterceptor {

    private val logger = logger<ReauestLoggingListener>()

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse, handler: Any
    ): Boolean {
        logger.trace("1.02.01.1001", Supplier<Array<Any>?> {
            if (handler is HandlerMethod) {
                val startTime = System.nanoTime()
                request.setAttribute(START_TIME, startTime)

                val methodParams = getMethodParams(handler)
                val simpleName: String = handler.bean.javaClass.simpleName
                val methodName: String = handler.method.name

                arrayOf<Any>(simpleName, methodName, methodParams)
            }
            null
        })

        return true
    }

    override fun postHandle(
        request: HttpServletRequest, response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        logger.trace("1.02.01.1002", Supplier<Array<Any>?> {
            if (handler is HandlerMethod) {
                val startTime =
                    if (request.getAttribute(START_TIME) != null) request.getAttribute(START_TIME) as Long else 0
                val endTime = System.nanoTime()
                val duration = endTime - startTime
                request.removeAttribute(START_TIME)

                val methodParams = getMethodParams(handler)
                val viewName = if (modelAndView != null) modelAndView.viewName else "null"
                val model = modelAndView?.model?.toString() ?: "{}"
                val simpleName: String = handler.bean.javaClass.simpleName
                val methodName: String = handler.method.name

                arrayOf<Any?>(simpleName, methodName, methodParams, duration, viewName, model)
            }
            null
        })
    }

    private fun getMethodParams(handlerMethod: HandlerMethod): String {
        return Arrays.stream(handlerMethod.methodParameters)
            .map { p: MethodParameter -> p.parameterType.simpleName }
            .collect(Collectors.joining(", "))
    }

    companion object {
        private const val START_TIME = "startTime"
    }
}
