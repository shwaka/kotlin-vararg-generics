package scratch.callback

// https://stackoverflow.com/questions/9057941/classcastexception-while-using-varargs-and-generics

fun interface Callback<T> {
    fun stuffDone(vararg params: T)
}

class MyCallback<T> : Callback<T> {
    override fun stuffDone(vararg params: T) {
        println("[vararg] params: $params")
    }
}

class Task<T> {
    fun doStuff(param: T, callback: Callback<T>) {
        callback.stuffDone(param)
    }
}

fun <T> doStuff(param: T, callback: Callback<T>) {
    callback.stuffDone(param)
}

inline fun <reified T> doStuffInline(param: T, callback: Callback<T>) {
    callback.stuffDone(param)
}

fun test0() {
    // no error
    val callback: Callback<String> = MyCallback<String>()
    val param = "hoge"
    doStuff(param, callback)
    println("test0 finished")
}

fun test1() {
    // no error
    val callback: Callback<String> = Callback<String> { println("params: $it") }
    val param = "hoge"
    doStuffInline(param, callback)
    println("test1 finished")
}

fun test2() {
    // error
    val callback: Callback<String> = Callback<String> { println("params: $it") }
    val param = "hoge"
    doStuff(param, callback) // error
    println("test2 finished")
}

fun test3() {
    // error
    val task = Task<String>()
    val callback: Callback<String> = Callback<String> { println("params: $it") }
    val param = "hoge"
    task.doStuff(param, callback) // error
    println("test3 finished")
}

fun callbackTest() {
    println("----- running test0, test1")
    test0()
    test1()
    println("----- running failing test test (will throw ClassCastException)")
    test2() // error
    // test3() // error
}
