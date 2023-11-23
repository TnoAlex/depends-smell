package com.github.tnoalex.foundation.asttools.processor.metrics

import com.github.tnoalex.foundation.asttools.processor.AstProcessor

abstract class MccabeComplexityProcessor : AstProcessor {
    private val functionMap = HashMap<String, ArrayList<Int>>()
    private val closureFunctionMap = HashMap<String, ArrayList<String>>()

    private val terminatedMap = HashSet<String>()

    fun getMccabeComplex(): Map<String, Int> {
        return margeFunction(functionMap.map { it.key to it.value[ARC_INDEX] - it.value[NODE_INDEX] + 2 }.toMap())
    }

    private fun margeFunction(functionMap: Map<String, Int>): HashMap<String, Int> {
        val res = HashMap<String, Int>()
        functionMap.forEach { (k, v) ->
            if (hasParentFunction(k)) {
                return@forEach // Closure functions don't need to be split
            }
            var complexity = v
            getClosureFunctions(k).forEach {
                complexity += functionMap[it]!!
            }
            res[k] = complexity
        }
        return res
    }

    private fun hasParentFunction(functionId: String): Boolean {
        closureFunctionMap.values.forEach {
            if (it.contains(functionId))
                return true
        }
        return false
    }

    private fun getClosureFunctions(parentFunction: String): MutableList<String> {
        val childFunctions = mutableListOf<String>()

        if (parentFunction in closureFunctionMap.keys) {
            childFunctions.addAll(closureFunctionMap[parentFunction]!!)
            for (childFunction in closureFunctionMap[parentFunction]!!) {
                childFunctions.addAll(getClosureFunctions(childFunction))
            }
        }

        return childFunctions
    }

    open fun finishProcess() {
        functionMap.clear()
        terminatedMap.clear()
        closureFunctionMap.clear()
    }

    protected fun recordClosurePair(parent: String, current: String) {
        if (closureFunctionMap[parent] == null) {
            closureFunctionMap[parent] = arrayListOf(current)
        } else {
            closureFunctionMap[parent]!!.add(current)
        }
    }

    protected fun addFunction(functionId: String) {
        if (functionMap[functionId] == null) {
            functionMap[functionId] = arrayListOf(1, 1)
        }
    }

    protected fun addTerminatedNode(functionId: String) {
        if (terminatedMap.contains(functionId)) {
            return
        } else {
            terminatedMap.add(functionId)
            addNode(functionId)
        }
    }

    protected fun addNode(functionId: String, nodeNums: Int = 1) {
        functionMap[functionId] ?: throw RuntimeException("Unexpected keys:${functionId}")
        functionMap[functionId]!![NODE_INDEX] += nodeNums
    }

    protected fun addArc(functionId: String, arcNums: Int = 1) {
        functionMap[functionId] ?: throw RuntimeException("Unexpected keys:${functionId}")
        functionMap[functionId]!![ARC_INDEX] += arcNums
    }

    companion object {
        private const val NODE_INDEX = 0
        private const val ARC_INDEX = 1
    }
}