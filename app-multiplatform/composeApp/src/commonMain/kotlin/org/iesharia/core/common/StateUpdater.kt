package org.iesharia.core.common

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

// Agregamos restricción de tipo no nulo
class StateUpdater<T : Any>(private val stateFlow: MutableStateFlow<T>) {

    fun update(transform: (T) -> T) {
        stateFlow.update(transform)
    }

    @Suppress("UNCHECKED_CAST")
    fun <P> updateProperty(prop: String, value: P) {
        update { currentState ->
            val copy = currentState::class.members.find { it.name == "copy" }
                ?: throw IllegalArgumentException("State class must be a data class with copy method")

            copy.call(currentState, mapOf(prop to value)) as T
        }
    }

    fun updateProperties(props: Map<String, Any?>) {
        update { currentState ->
            val copy = currentState::class.members.find { it.name == "copy" }
                ?: throw IllegalArgumentException("State class must be a data class with copy method")

            copy.call(currentState, props) as T
        }
    }

    fun getCurrentState(): T = stateFlow.value
}