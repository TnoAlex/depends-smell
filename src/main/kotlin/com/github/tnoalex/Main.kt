package com.github.tnoalex

import com.github.tnoalex.utils.StdOutErrWrapper
import depends.LangRegister
import java.util.*


fun main(args: Array<String>) {
    showBanner()
    LangRegister.register()
    StdOutErrWrapper.init()
    CommandParser().main(args)
}

private fun showBanner() {
    val inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("banner.txt")
    val banner = StringBuilder()
    if (inputStream != null) {
        val scanner = Scanner(inputStream)
        while (scanner.hasNextLine()) {
            banner.append(scanner.nextLine()).append("\n")
        }
        println(banner.toString())
    }
}