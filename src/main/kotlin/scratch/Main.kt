package scratch

interface Scalar<S : Scalar<S>> {
    operator fun plus(other: S): S
}

interface Vector<S : Scalar<S>, V : Vector<S, V>> {
    operator fun plus(other: V): V
}

interface VectorSpace<S : Scalar<S>, V : Vector<S, V>> {
    fun fromScalar(a: S): V
}

interface MatrixSpace<S : Scalar<S>, V : Vector<S, V>> {
    val vectorSpace: VectorSpace<S, V>
    fun fromRows(rows: List<List<S>>)
    fun fromRows(vararg rows: List<S>)
    fun fromVectors(vectors: List<V>)
    fun fromVectors(vararg vectors: V)
}

class WrappedInt(private val value: Int) : Scalar<WrappedInt> {
    override fun plus(other: WrappedInt): WrappedInt {
        return WrappedInt(this.value + other.value)
    }
}

class MyVector<S : Scalar<S>>(private val value: S) : Vector<S, MyVector<S>> {
    override fun plus(other: MyVector<S>): MyVector<S> {
        return MyVector(this.value + other.value)
    }
}

class MyVectorSpace<S : Scalar<S>> : VectorSpace<S, MyVector<S>> {
    override fun fromScalar(a: S): MyVector<S> {
        return MyVector(a)
    }
}

class MyMatrixSpace<S : Scalar<S>>(override val vectorSpace: MyVectorSpace<S>) : MatrixSpace<S, MyVector<S>> {
    override fun fromRows(rows: List<List<S>>) {}
    override fun fromRows(vararg rows: List<S>) {}
    override fun fromVectors(vararg vectors: MyVector<S>) {}
    override fun fromVectors(vectors: List<MyVector<S>>) {}
}

@Suppress("UNUSED_PARAMETER")
class Hoge<S : Scalar<S>, V : Vector<S, V>> {
    fun fromRows(rows: List<List<S>>) {}
    fun fromVectors(vectors: List<V>) {}
    fun fromVectors(vararg vectors: V) {}
}

fun test1() {
    // no error
    val hoge = Hoge<WrappedInt, MyVector<WrappedInt>>()
    val zero = WrappedInt(0)
    hoge.fromRows(
        listOf(
            listOf(zero, zero),
            listOf(zero)
        )
    )
    val v = MyVector(zero)
    hoge.fromVectors(v, v, v)
}

@Suppress("UNUSED_PARAMETER")
fun <S : Scalar<S>, V : Vector<S, V>> test2(a: S, v: V) {
    // no error
    val hoge = Hoge<S, V>()
    hoge.fromVectors(v, v, v)
}

fun <S : Scalar<S>, V : Vector<S, V>> test3(a: S, toVector: (x: S) -> V) {
    // no error
    val hoge = Hoge<S, V>()
    val v = toVector(a)
    hoge.fromVectors(v, v, v)
}

fun <S : Scalar<S>, V : Vector<S, V>> test4(matrixSpace: MatrixSpace<S, V>, a: S) {
    // no error
    matrixSpace.fromRows(
        listOf(a, a),
        listOf(a, a)
    )
}

fun <S : Scalar<S>, V : Vector<S, V>> test5(matrixSpace: MatrixSpace<S, V>, a: S) {
    // no error
    val vectorSpace = matrixSpace.vectorSpace
    val v = vectorSpace.fromScalar(a)
    matrixSpace.fromVectors(listOf(v))
}

inline fun <S : Scalar<S>, reified V : Vector<S, V>> test6(matrixSpace: MatrixSpace<S, V>, a: S) {
    // error
    val vectorSpace = matrixSpace.vectorSpace
    val v = vectorSpace.fromScalar(a)
    // matrixSpace.fromVectors(listOf(v))
    matrixSpace.fromVectors(v)
}

fun <S : Scalar<S>, V : Vector<S, V>> testFail(matrixSpace: MatrixSpace<S, V>, a: S) {
    // error
    val vectorSpace = matrixSpace.vectorSpace
    val v = vectorSpace.fromScalar(a)
    // matrixSpace.fromVectors(listOf(v))
    matrixSpace.fromVectors(v)
}

fun main() {
    println("----- running test1, test2, test3, test4, test5, test6")
    test1()
    val zero = WrappedInt(0)
    val v = MyVector(zero)
    test2(zero, v)
    test3(zero, ::MyVector)

    val vectorSpace = MyVectorSpace<WrappedInt>()
    val matrixSpace = MyMatrixSpace<WrappedInt>(vectorSpace)
    test4(matrixSpace, zero)
    test5(matrixSpace, zero)
    test6(matrixSpace, zero)

    println("----- running testFail (will throw ClassCastException)")
    testFail(matrixSpace, zero)
}
