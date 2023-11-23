package com.github.tnoalex.foundation.metrics

import com.github.tnoalex.analyzer.AbstractAnalyzerTest
import com.github.tnoalex.formatter.FormatterTypeEnum
import com.github.tnoalex.foundation.asttools.processor.AstProcessorContainer
import com.github.tnoalex.foundation.asttools.processor.metrics.KotlinMccabeComplexityProcessor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class KotlinMccabeComplexityProcessorTest : AbstractAnalyzerTest("kotlin") {

    @Test
    fun testMccabe() {
        init()
//        (AstProcessorContainer.getByType(KotlinMccabeComplexityProcessor::class)
//            ?.get(0) as MccabeComplexityProcessor).finishProcess()
        createTestContext(
            "E:\\code\\depends-smell\\src\\test\\resources\\metrics-samples",
            "", "./", FormatterTypeEnum.JSON
        )
        val processor =
            AstProcessorContainer.getByType(KotlinMccabeComplexityProcessor::class)
        val cc = (processor!![0] as KotlinMccabeComplexityProcessor).getMccabeComplex()
        println(cc)
        assertEquals(cc["ccSample0@0"], 3)
        assertEquals(cc["ccSample1@2"], 9)
        assertEquals(cc["ccSample2@1"], 4)
        assertEquals(cc["ccSample3@4"], 8)
        assertEquals(cc["solveSCC@0"], 18)
    }
}