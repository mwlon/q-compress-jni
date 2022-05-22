# Run from the native/ directory.

set -e

sh build_local_darwin.sh
TAG="q-compress-jni-native:latest"
docker build -f ubuntu.Dockerfile -t "$TAG" .

copy_linux () {
  # my sneaky one-liner for copying the binaries out
  DIR="../src/main/resources/native/$2/"
  mkdir -p "$DIR"
  docker run --rm "$TAG" cat "/workdir/target/$1/release/libq_compress_jni_native.so" > "$DIR/libq_compress_jni_native.so"
}
copy_linux aarch64-unknown-linux-gnu aarch64-linux
copy_linux x86_64-unknown-linux-gnu x86_64-linux
