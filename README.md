[![Maven Central][maven-badge]][maven-url]

[maven-badge]: https://maven-badges.herokuapp.com/maven-central/io.github.mwlon/q-compress-jni/badge.svg?gav=true
[maven-url]: https://search.maven.org/artifact/io.github.mwlon/q-compress-jni


# [Quantile Compression](https://github.com/mwlon/quantile-compression) JVM Bindings

Example:

```java
import io.github.mwlon.qcompress.QCompress;

class Main {
  public static void main(String[] args) {
    int[] orig = {1, 2, 3, 4, 5};
    byte[] compressed = QCompress.autoCompressInts(orig, 6);
    int[] recovered = QCompress.autoDecompressInts(compressed);
    System.out.println(String.join(", ", recovered));
    // prints 1, 2, 3, 4, 5
  }
}
```

This library binds to the Rust code for
[q_compress](https://github.com/mwlon/quantile-compression/tree/main/q_compress).
It currently supports the following OS/architectures pairs, with more
to come in the future:
* x86_64-darwin (Mac)
* x86_64-linux
* aarch64-linux

So far it handles these pieces of functionality from `q_compress`:
* auto compress and decompress

and these Java data types:
* `boolean`
* `int`
* `long`
* `float`
* `double`

For any feature requests, please submit a Github issue.
