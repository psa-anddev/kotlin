// !LANGUAGE: +ReleaseCoroutines

// TARGET_BACKEND: JVM
// WITH_RUNTIME
// WITH_COROUTINES

import helpers.*
import kotlin.coroutines.*
import kotlin.coroutines.intrinsics.*
import kotlin.coroutines.jvm.internal.*

suspend fun getVariableToSpilled() = suspendCoroutineUninterceptedOrReturn<Array<String>> {
    getVariableToSpilledMapping(it)
}

fun Array<String>.toMap(): Map<String, String> {
    val res = hashMapOf<String, String>()
    for (i in 0..(size - 1) step 2) {
        res[get(i)] = get(i + 1)
    }
    return res
}

var continuation: Continuation<*>? = null

suspend fun suspendHere() = suspendCoroutineUninterceptedOrReturn<Unit> {
    continuation = it
    COROUTINE_SUSPENDED
}

suspend fun dummy() {}

suspend fun named(): String {
    dummy()
    val s1 = ""
    val s2 = ""
    val s3 = ""
    val s4 = ""
    val s5 = ""
    val s6 = ""
    val s7 = ""
    val s8 = ""
    val s9 = ""
    val map = getVariableToSpilled().toMap()
    return map["L$0"] + map["L$1"] + map["L$2"] + map["L$3"] + map["L$4"] + map["L$5"] + map["L$6"] + map["L$7"] + map["L$8"]
}

suspend fun suspended() {
    dummy()
    val ss = ""
    suspendHere()
}

fun builder(c: suspend () -> Unit) {
    c.startCoroutine(EmptyContinuation)
}

fun box(): String {
    var res: String = ""
    builder {
        res = named()
    }
    if (res != "s1s2s3s4s5s6s7s8s9") {
        return "" + res
    }
    builder {
        dummy()
        val a = ""
        res = getVariableToSpilled().toMap()["L$0"] ?: "lambda fail"
    }
    if (res != "a") {
        return "" + res
    }

    builder {
        suspended()
    }
    res = getVariableToSpilledMapping(continuation!!).toMap()["L$0"] ?: "suspended fail"
    if (res != "ss") {
        return "" + res
    }
    return "OK"
}