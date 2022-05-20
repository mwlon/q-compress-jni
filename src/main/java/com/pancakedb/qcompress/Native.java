package com.pancakedb.qcompress;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Native {
  private static String getArchitectureName() {
    return System.getProperty("os.arch");
  }

  private static String getOsName() {
    String os = System.getProperty("os.name")
        .toLowerCase()
        .replace(' ', '_');
    if (os.startsWith("win")) {
      return "windows";
    }
    if (os.startsWith("mac")) {
      return "darwin";
    }
    return os;
  }

  private static String getLibExtension() {
    String osName = getOsName();
    if (osName.equals("darwin")) {
      return "dylib";
    } else if (osName.equals("windows")) {
      return "dll";
    } else {
      return "so";
    }
  }

  static {
    try {
      Path tmpFolder = Files.createTempDirectory("q-compress-jni-");
      File tmpLibFile = tmpFolder.resolve("native").toFile();
      tmpLibFile.deleteOnExit();

      String resourceName = "native/" +
          getArchitectureName() +
          "-" +
          getOsName() +
          "/libq_compress_jni_native." +
          getLibExtension();
      InputStream in = Native.class.getClassLoader().getResourceAsStream(resourceName);
      if (in == null) {
        throw new RuntimeException(
            "q_compress native library " + resourceName + " not available for your architecture"
        );
      }
      Files.copy(in, tmpLibFile.toPath());
      System.load(tmpLibFile.getAbsolutePath());
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public static native boolean[] autoDecompressBooleans(byte[] data);
  public static native int[] autoDecompressInts(byte[] data);
  public static native long[] autoDecompressLongs(byte[] data);
  public static native float[] autoDecompressFloats(byte[] data);
  public static native double[] autoDecompressDoubles(byte[] data);

  public static native byte[] autoCompressBooleans(boolean[] nums, int compressionLevel);
  public static native byte[] autoCompressInts(int[] nums, int compressionLevel);
  public static native byte[] autoCompressLongs(long[] nums, int compressionLevel);
  public static native byte[] autoCompressFloats(float[] nums, int compressionLevel);
  public static native byte[] autoCompressDoubles(double[] nums, int compressionLevel);
}
