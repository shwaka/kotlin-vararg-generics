package scratch.callback

// https://stackoverflow.com/questions/9057941/classcastexception-while-using-varargs-and-generics

fun interface Callback<T> {
    fun stuffDone(vararg params: T)
}

class MyCallback<T> : Callback<T> {
    override fun stuffDone(vararg params: T) {
        println("[vararg] params: $params")
    }

    // fun stuffDone(params: List<T>) {
    //     println("[list] params: $params")
    // }
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

fun test1() {
    val task = Task<String>()
    val callback: Callback<String> = Callback<String> { println("params: $it") }
    val param = "hoge"
    task.doStuff(param, callback) // error
    // doStuff(param, callback) // error
    // doStuffInline(param, callback)
}

fun test2() {
    val task = Task<String>()
    val callback: Callback<String> = MyCallback<String>()
    val param = "hoge"
    doStuff(param, callback)
}

fun callbackTest() {
    test1()
    // test2()
}
