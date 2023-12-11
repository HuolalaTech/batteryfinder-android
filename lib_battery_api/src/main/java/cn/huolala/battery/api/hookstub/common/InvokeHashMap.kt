package cn.huolala.battery.api.hookstub.common

import androidx.annotation.Keep
import java.util.concurrent.ConcurrentHashMap
@Keep
class InvokeHashMap<T : Any, F : InvokeData>(
    val block: InvokeHashMap<T, F>.() -> Boolean,
    val dumper: InvokeHashMap<T, F>.(state: InvokeState, invokeData: F) -> Unit
) :
    ConcurrentHashMap<T, F>() {

    // 结束时dumper dump出数据，删除记录map中的数据
    fun updateRecord(key: T, value: F) {
        this[key] = value
        if (block()) {
            dumper.invoke(this, InvokeState.Recording, value)
        }
    }

    fun deleteRecord(key: T, value: F) {
        this[key] = value
        if (block()) {
            dumper.invoke(this, InvokeState.Recorded, value)
        }
        this.remove(key)
    }

}