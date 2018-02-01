package com.wwj;

import com.googlecode.javaewah.EWAHCompressedBitmap;
import org.junit.Test;

import java.util.BitSet;

/**
 * Created by sherry on 2017/12/15.
 */
public class BitSetTest {

    public static void main(String[] args) {

    }

    @Test
    public void GoogleBitMapTest() {
        EWAHCompressedBitmap bitmap1 = new EWAHCompressedBitmap();
        EWAHCompressedBitmap bitmap2 = new EWAHCompressedBitmap();
        bitmap1.set(1);
        bitmap1.set(2);
        System.out.println(bitmap1.get(1));

        //是否相等
        bitmap2.set(1);
        System.out.println(bitmap1.equals(bitmap2));
    }

    /**
     * java版本的bitmap测试
     */
    @Test
    public void JavaBitMapTest() {
        BitSet bitSet = new BitSet();
        show(bitSet);
        bitSet.set(1);
        show(bitSet);
        bitSet.set(3);
        show(bitSet);
        bitSet.set(9);
        show(bitSet);
        bitSet.set(65);
        show(bitSet);
    }

    void show(BitSet bitSet) {
        //初始化bit分配的空间
        System.out.println("bitSet size:" + bitSet.size());
        //bitSet逻辑size，其值为存储的最大值+1
        System.out.println("bitSet length:" + bitSet.length());
        System.out.println("bitSet cardinality:" + bitSet.cardinality());
        for (int i = 0; i < bitSet.length(); i++) {
            System.out.println("bitSize result:" + bitSet.get(i));
        }
        System.out.println("==============================");
    }
}
