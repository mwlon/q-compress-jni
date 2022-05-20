use std::mem;

use jni::JNIEnv;
use jni::objects::JClass;
use jni::sys::{jboolean, jbooleanArray, jbyteArray, jdoubleArray, jfloatArray, jint, jintArray, jlongArray, jobject, jsize};
use q_compress::data_types::NumberLike;

fn auto_decompress<T, F>(
  env: JNIEnv,
  data: jbyteArray,
  create_jobject_fn: F,
) -> jobject
where T: NumberLike, F: FnOnce(&JNIEnv, Vec<T>) -> jobject {
  let bytes = env.convert_byte_array(data)
    .expect("failed to load byte data");
  let nums = q_compress::auto_decompress(&bytes)
    .expect("corrupt data");
  create_jobject_fn(&env, nums)
}

fn auto_compress<T>(
  env: JNIEnv,
  nums: &[T],
  compression_level: jint,
) -> jbyteArray
  where T: NumberLike {
  let bytes = q_compress::auto_compress(nums, compression_level as usize);
  env.byte_array_from_slice(&bytes)
    .expect("unable to allocated compressed bytes")
}

#[no_mangle]
pub extern "system" fn Java_io_github_mwlon_qcompress_QCompress_autoDecompressBooleans(
  env: JNIEnv,
  _: JClass,
  data: jbyteArray,
) -> jbooleanArray {
  auto_decompress::<bool, _>(
    env,
    data,
    |env, nums| {
      let java_values = env.new_boolean_array(nums.len() as jsize)
        .expect("unable to allocate java array");
      let jnums = unsafe {
        mem::transmute::<Vec<bool>, Vec<jboolean>>(nums)
      };
      env.set_boolean_array_region(java_values, 0 as jsize, &jnums)
        .expect("unable to assign to java array");
      java_values
    }
  )
}

#[no_mangle]
pub extern "system" fn Java_io_github_mwlon_qcompress_QCompress_autoDecompressInts(
  env: JNIEnv,
  _: JClass,
  data: jbyteArray,
) -> jintArray {
  auto_decompress::<i32, _>(
    env,
    data,
    |env, nums| {
      let java_values = env.new_int_array(nums.len() as jsize)
        .expect("unable to allocate java array");
      env.set_int_array_region(java_values, 0 as jsize, &nums)
        .expect("unable to assign to java array");
      java_values
    }
  )
}

#[no_mangle]
pub extern "system" fn Java_io_github_mwlon_qcompress_QCompress_autoDecompressLongs(
  env: JNIEnv,
  _: JClass,
  data: jbyteArray,
) -> jlongArray {
  auto_decompress::<i64, _>(
    env,
    data,
    |env, nums| {
      let java_values = env.new_long_array(nums.len() as jsize)
        .expect("unable to allocate java array");
      env.set_long_array_region(java_values, 0 as jsize, &nums)
        .expect("unable to assign to java array");
      java_values
    }
  )
}

#[no_mangle]
pub extern "system" fn Java_io_github_mwlon_qcompress_QCompress_autoDecompressFloats(
  env: JNIEnv,
  _: JClass,
  data: jbyteArray,
) -> jfloatArray {
  auto_decompress::<f32, _>(
    env,
    data,
    |env, nums| {
      let java_values = env.new_float_array(nums.len() as jsize)
        .expect("unable to allocate java array");
      env.set_float_array_region(java_values, 0 as jsize, &nums)
        .expect("unable to assign to java array");
      java_values
    }
  )
}

#[no_mangle]
pub extern "system" fn Java_io_github_mwlon_qcompress_QCompress_autoDecompressDoubles(
  env: JNIEnv,
  _: JClass,
  data: jbyteArray,
) -> jdoubleArray {
  auto_decompress::<f64, _>(
    env,
    data,
    |env, nums| {
      let java_values = env.new_double_array(nums.len() as jsize)
        .expect("unable to allocate java array");
      env.set_double_array_region(java_values, 0 as jsize, &nums)
        .expect("unable to assign to java array");
      java_values
    }
  )
}

#[no_mangle]
pub extern "system" fn Java_io_github_mwlon_qcompress_QCompress_autoCompressBooleans(
  env: JNIEnv,
  _: JClass,
  nums: jbooleanArray,
  compression_level: jint,
) -> jbyteArray {
  let len = env.get_array_length(nums)
    .expect("unable to get nums length");
  let mut buf = vec![0; len as usize];
  env.get_boolean_array_region(nums, 0, &mut buf)
    .expect("unable to read nums into rust");
  let bools = unsafe {
    mem::transmute::<Vec<jboolean>, Vec<bool>>(buf)
  };
  auto_compress(env, &bools, compression_level)
}

#[no_mangle]
pub extern "system" fn Java_io_github_mwlon_qcompress_QCompress_autoCompressInts(
  env: JNIEnv,
  _: JClass,
  nums: jintArray,
  compression_level: jint,
) -> jbyteArray {
  let len = env.get_array_length(nums)
    .expect("unable to get nums length");
  let mut buf = vec![0; len as usize];
  env.get_int_array_region(nums, 0, &mut buf)
    .expect("unable to read nums into rust");
  auto_compress(env, &buf, compression_level)
}

#[no_mangle]
pub extern "system" fn Java_io_github_mwlon_qcompress_QCompress_autoCompressLongs(
  env: JNIEnv,
  _: JClass,
  nums: jlongArray,
  compression_level: jint,
) -> jbyteArray {
  let len = env.get_array_length(nums)
    .expect("unable to get nums length");
  let mut buf = vec![0; len as usize];
  env.get_long_array_region(nums, 0, &mut buf)
    .expect("unable to read nums into rust");
  auto_compress(env, &buf, compression_level)
}

#[no_mangle]
pub extern "system" fn Java_io_github_mwlon_qcompress_QCompress_autoCompressFloats(
  env: JNIEnv,
  _: JClass,
  nums: jfloatArray,
  compression_level: jint,
) -> jbyteArray {
  let len = env.get_array_length(nums)
    .expect("unable to get nums length");
  let mut buf = vec![0.0; len as usize];
  env.get_float_array_region(nums, 0, &mut buf)
    .expect("unable to read nums into rust");
  auto_compress(env, &buf, compression_level)
}

#[no_mangle]
pub extern "system" fn Java_io_github_mwlon_qcompress_QCompress_autoCompressDoubles(
  env: JNIEnv,
  _: JClass,
  nums: jdoubleArray,
  compression_level: jint,
) -> jbyteArray {
  let len = env.get_array_length(nums)
    .expect("unable to get nums length");
  let mut buf = vec![0.0; len as usize];
  env.get_double_array_region(nums, 0, &mut buf)
    .expect("unable to read nums into rust");
  auto_compress(env, &buf, compression_level)
}
