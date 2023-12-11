package cn.huolala.battery.api.hookstub.common

import java.util.concurrent.CopyOnWriteArrayList

class InvokeArrayList<T : InvokeData>(
    val block: InvokeArrayList<T>.() -> Boolean,
    private val dumper: InvokeArrayList<T>.(state: InvokeState, invokeData: T) -> Unit
) :
    CopyOnWriteArrayList<T>() {
    override fun add(element: T): Boolean {
        val result = super.add(element)
        if (block(this)) {
            dumper.invoke(this, InvokeState.Recorded, element)
        }
        return result
    }
}