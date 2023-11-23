package com.github.tnoalex.foundation.asttools.processor

import com.github.tnoalex.foundation.common.CollectionContainer
import com.github.tnoalex.utils.loadServices
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.reflect.KClass

object AstProcessorContainer : CollectionContainer<String, AstProcessor> {
    private val processors = HashMap<String, LinkedList<AstProcessor>>()
    private val logger = LoggerFactory.getLogger(AstProcessorContainer::class.java)

    init {
        loadServices(AstProcessor::class.java).forEach {
            register(it)
        }
    }

    override fun register(entity: AstProcessor) {
        entity.supportLanguage.forEach {
            if (processors[it] == null) {
                processors[it] = LinkedList(listOf(entity))
            } else {
                processors[it]!!.add(entity)
            }
        }

    }

    override fun getByKey(key: String): List<AstProcessor>? = processors[key]?.sortedByDescending { it.order }

    override fun getKeys(): List<String> = processors.keys.toList()

    override fun getByType(clazz: KClass<out AstProcessor>): List<AstProcessor>? {
        processors.values.forEach {
            val res = it.filter { p ->
                p::class == clazz
            }
            if (res.isNotEmpty()) {
                return res
            }
        }
        return null
    }


    fun registerAstByLang(lang: String) {
        processors[lang]?.run {
            sortedByDescending { it.order }
            forEach {
                it.registerListener()
                logger.info("Hooked ${it::class.simpleName}")
            }
        }
    }

    fun registerAllProcessor() {
        processors.values.forEach {
            it.sortedByDescending { p -> p.order }
                .forEach { p ->
                    p.registerListener()
                }
        }
    }

    fun unregistersByLang(lang: String) {
        processors[lang]?.forEach {
            it.unregisterListener()
        }
    }

    fun unregisterAllProcessor() {
        processors.values.forEach {
            it.forEach { p ->
                p.unregisterListener()
            }
        }
    }
}