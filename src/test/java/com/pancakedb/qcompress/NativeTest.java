package com.pancakedb.qcompress;

import org.junit.Assert;
import org.junit.Test;

public class NativeTest {
  @Test
  public void testAutoBools() {
    boolean[] orig = {false, false, true, true, false};
    byte[] compressed = Native.autoCompressBooleans(orig, 6);
    boolean[] recovered = Native.autoDecompressBooleans(compressed);
    Assert.assertArrayEquals(orig, recovered);
  }

  @Test
  public void testAutoInts() {
    int[] orig = {1, 2, 3, 4, 5};
    byte[] compressed = Native.autoCompressInts(orig, 6);
    int[] recovered = Native.autoDecompressInts(compressed);
    Assert.assertArrayEquals(orig, recovered);
  }

  @Test
  public void testAutoLongs() {
    long[] orig = {1, 2, 3, 4, 5};
    byte[] compressed = Native.autoCompressLongs(orig, 6);
    long[] recovered = Native.autoDecompressLongs(compressed);
    Assert.assertArrayEquals(orig, recovered);
  }

  @Test
  public void testAutoFloats() {
    float[] orig = {1f, 2f, 3f, 4f, 5f};
    byte[] compressed = Native.autoCompressFloats(orig, 6);
    float[] recovered = Native.autoDecompressFloats(compressed);
    Assert.assertEquals(orig.length, recovered.length);
    for (int i = 0; i < orig.length; i++) {
      Assert.assertEquals(orig[i], recovered[i], 0.0);
    }
  }

  @Test
  public void testAutoDoubles() {
    double[] orig = {1, 2, 3, 4, 5};
    byte[] compressed = Native.autoCompressDoubles(orig, 6);
    double[] recovered = Native.autoDecompressDoubles(compressed);
    Assert.assertEquals(orig.length, recovered.length);
    for (int i = 0; i < orig.length; i++) {
      Assert.assertEquals(orig[i], recovered[i], 0.0);
    }
  }

  @Test
  public void testCompressionRatio() {
    long[] nums = new long[1000];
    for (int i = 0; i < 1000; i++) {
      nums[i] = i;
    }
    byte[] bytes = Native.autoCompressLongs(nums, 6);
    Assert.assertTrue(bytes.length < 50);
    Assert.assertTrue(bytes.length > 20);
  }
}
