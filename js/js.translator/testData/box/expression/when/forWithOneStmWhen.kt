// IGNORE_BACKEND: JS_IR
// EXPECTED_REACHABLE_NODES: 1280
package foo


fun box(): String {
    var result = "fail1"
    for (i in arrayOf(1))
        when (i) {
            1 -> result = "OK"
            else -> result = "fail2"
        }
    return result
}

