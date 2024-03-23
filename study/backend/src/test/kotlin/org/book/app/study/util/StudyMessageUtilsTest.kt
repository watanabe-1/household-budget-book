package org.book.app.study.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.validation.BeanPropertyBindingResult

internal class StudyMessageUtilsTest {

    @Test
    fun `addError should add an error with the specified code to the result`() {
        val result = BeanPropertyBindingResult(Any(), "objectName")
        StudyMessageUtils.addError(result, "field", "error_code")

        assertFalse(result.allErrors.isEmpty())
        assertEquals("error_code", result.allErrors.first().code)
    }

    @Test
    fun `getArrayFieldName should return the correct field name for array elements`() {
        val result = StudyMessageUtils.getArrayFieldName("arrayField", 3, "subField1", "subField2")
        assertEquals("arrayField[3].subField1.subField2", result)
    }

    @Test
    fun `getFieldName should return the correct field name for single and multiple fields`() {
        val singleFieldResult = StudyMessageUtils.getFieldName("field1")
        assertEquals("field1", singleFieldResult)

        val multipleFieldsResult = StudyMessageUtils.getFieldName("field1", "field2")
        assertEquals("field1.field2", multipleFieldsResult)

        val emptyFieldsResult = StudyMessageUtils.getFieldName()
        assertEquals("", emptyFieldsResult)
    }

    @Test
    fun `addError with two parameters should function correctly`() {
        val result = BeanPropertyBindingResult(Any(), "objectName")
        StudyMessageUtils.addError(result, "field", "error_code")

        assertFalse(result.allErrors.isEmpty())
        assertEquals("error_code", result.allErrors.first().code)
    }

    @Test
    fun `addError with arguments should include arguments in the error`() {
        val result = BeanPropertyBindingResult(Any(), "objectName")
        StudyMessageUtils.addError(result, "field", "error_code", "arg1", "arg2")

        assertFalse(result.allErrors.isEmpty())
        val error = result.getFieldError("field")
        assertNotNull(error)
        assertEquals("error_code", error!!.code)
        assertArrayEquals(arrayOf<Any>("arg1", "arg2"), error.arguments)
    }

    @Test
    fun `addErrorOndefMsg should set the default message and include arguments in the error`() {
        val result = BeanPropertyBindingResult(Any(), "objectName")
        StudyMessageUtils.addErrorOnDefMsg(result, "field", "error_code", "default message", arrayOf<Any>("arg1"))

        assertFalse(result.allErrors.isEmpty())
        val error = result.getFieldError("field")
        assertNotNull(error)
        assertEquals("default message", error!!.defaultMessage)
    }
}
