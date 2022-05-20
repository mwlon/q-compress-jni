package com.github.mwlon.qcompress;

import org.junit.Assert;
import org.junit.Test;

public class QCompressTest {
  @Test
  public void testAutoBools() {
    boolean[] orig = {false, false, true, true, false};
    byte[] compressed = QCompress.autoCompressBooleans(orig, 6);
    boolean[] recovered = QCompress.autoDecompressBooleans(compressed);
    Assert.assertArrayEquals(orig, recovered);
  }

  @Test
  public void testAutoInts() {
    int[] orig = {1, 2, 3, 4, 5};
    byte[] compressed = QCompress.autoCompressInts(orig, 6);
    int[] recovered = QCompress.autoDecompressInts(compressed);
    Assert.assertArrayEquals(orig, recovered);
  }

  @Test
  public void testAutoLongs() {
    long[] orig = {1, 2, 3, 4, 5};
    byte[] compressed = QCompress.autoCompressLongs(orig, 6);
    long[] recovered = QCompress.autoDecompressLongs(compressed);
    Assert.assertArrayEquals(orig, recovered);
  }

  @Test
  public void testAutoFloats() {
    float[] orig = {1f, 2f, 3f, 4f, 5f};
    byte[] compressed = QCompress.autoCompressFloats(orig, 6);
    float[] recovered = QCompress.autoDecompressFloats(compressed);
    Assert.assertEquals(orig.length, recovered.length);
    for (int i = 0; i < orig.length; i++) {
      Assert.assertEquals(orig[i], recovered[i], 0.0);
    }
  }

  @Test
  public void testAutoDoubles() {
    double[] orig = {1, 2, 3, 4, 5};
    byte[] compressed = QCompress.autoCompressDoubles(orig, 6);
    double[] recovered = QCompress.autoDecompressDoubles(compressed);
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
    byte[] bytes = QCompress.autoCompressLongs(nums, 6);
    Assert.assertTrue(bytes.length < 50);
    Assert.assertTrue(bytes.length > 20);
  }
}
