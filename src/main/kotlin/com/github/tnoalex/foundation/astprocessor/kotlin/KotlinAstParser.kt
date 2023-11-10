package com.github.tnoalex.foundation.astprocessor.kotlin

import com.github.tnoalex.foundation.astprocessor.AstParser
import depends.extractor.kotlin.KotlinLexer
import depends.extractor.kotlin.KotlinParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker

object KotlinAstParser : AstParser {
    override fun parseAst(fileName: String) {
        val input = CharStreams.fromFileName(fileName)
        val lexer = KotlinLexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = KotlinParser(tokens)
        val bridge = KotlinAstListener()
        val walker = ParseTreeWalker()
        walker.walk(bridge, parser.kotlinFile())
    }
}