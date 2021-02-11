`./gradlew run` will throw `java.lang.ClassCastException`

```bash
Exception in thread "main" java.lang.ClassCastException: [Lscratch.matrixSpace.Vector; cannot be cast to [Lscratch.matrixSpace.MyVector;
        at scratch.matrixSpace.MyMatrixSpace.fromVectors(MatrixSpace.kt:41)
        at scratch.matrixSpace.MatrixSpaceKt.testFail(MatrixSpace.kt:117)
        at scratch.matrixSpace.MatrixSpaceKt.matrixSpaceTest(MatrixSpace.kt:136)
        at scratch.MainKt.main(Main.kt:7)
        at scratch.MainKt.main(Main.kt)
```

エラーメッセージ中の `[Lscratch.matrixSpace.MyVector` については，以下のあたりを参照:
- [JDI Type Signatures](https://docs.oracle.com/javase/10/docs/api/com/sun/jdi/doc-files/signature.html)
- [Type Signature](https://www.ueda.info.waseda.ac.jp/~yoshida/java/jni/type.html)
