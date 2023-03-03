package me.egg82.gpt.utils;

import it.unimi.dsi.fastutil.doubles.DoubleArrays;
import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.longs.LongArrays;
import me.egg82.gpt.core.Weighted;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class MathUtil {
    private static final Random rand = new Random();

    private MathUtil() { }

    public static int floor(float f) {
        int i = (int) f;
        return (f < i) ? (i - 1) : i;
    }

    public static long lfloor(double d) {
        long l = (long) d;
        return (d < l) ? (l - 1L) : l;
    }

    // https://stackoverflow.com/questions/41413544/calculate-percentile-from-a-long-array
    public static int percentile(int @NotNull [] list, double percentile) {
        if (list.length == 0) {
            return 0;
        }
        IntArrays.quickSort(list);
        int index = (int) Math.ceil(percentile / 100.0d * list.length);
        return list[index - 1];
    }

    public static long percentile(long @NotNull [] list, double percentile) {
        if (list.length == 0) {
            return 0L;
        }
        LongArrays.quickSort(list);
        int index = (int) Math.ceil(percentile / 100.0d * list.length);
        return list[index - 1];
    }

    public static double percentile(double @NotNull [] list, double percentile) {
        if (list.length == 0) {
            return 0L;
        }
        DoubleArrays.quickSort(list);
        int index = (int) Math.ceil(percentile / 100.0d * list.length);
        return list[index - 1];
    }

    public static double average(long @NotNull [] list) {
        double sum = 0L;
        for (long l : list) {
            sum += l;
        }
        return sum / (double) list.length;
    }

    public static long sum(long @NotNull [] list) {
        long retVal = 0L;
        for (long l : list) {
            retVal += l;
        }
        return retVal;
    }

    public static double clamp(double val, double min, double max) {
        if (val < min) {
            return min;
        }
        return Math.min(val, max);
    }

    public static float clamp(float val, float min, float max) {
        if (val < min) {
            return min;
        }
        return Math.min(val, max);
    }

    // https://www.techiedelight.com/round-next-highest-power-2/
    public static int nextPowerOf2(int n) {
        n--;
        n |= n >> 1;
        n |= n >> 2;
        n |= n >> 4;
        n |= n >> 8;
        n |= n >> 16;
        return ++n;
    }

    // https://stackoverflow.com/questions/6737283/weighted-randomness-in-java
    // Note that when using this you'll want to start with high weights and decrease weights as they're used
    public static <K, T extends Weighted<K>> K weightedRandom(List<T> collection, double increaseWeightBy) {
        double totalWeight = 0.0d;
        for (Weighted<?> obj : collection) {
            totalWeight += obj.getWeight();
        }

        int i = 0;
        for (double r = rand.nextDouble() * totalWeight; i < collection.size() - 1; ++i) {
            r -= collection.get(i).getWeight();
            if (r <= 0.0d) {
                break;
            }
        }

        Weighted<K> weighted = collection.get(i);
        weighted.setWeight(weighted.getWeight() + increaseWeightBy);
        return weighted.getItem();
    }

    // Note that when using this you'll want to start with high weights and decrease weights as they're used
    public static <K, T extends Weighted<K>> K weightedRandom(Weighted<K>[] list, double increaseWeightBy) {
        double totalWeight = 0.0d;
        for (Weighted<?> obj : list) {
            totalWeight += obj.getWeight();
        }

        int i = 0;
        for (double r = rand.nextDouble() * totalWeight; i < list.length - 1; ++i) {
            r -= list[i].getWeight();
            if (r <= 0.0d) {
                break;
            }
        }

        Weighted<K> weighted = list[i];
        weighted.setWeight(weighted.getWeight() + increaseWeightBy);
        return weighted.getItem();
    }
}
