import java.math.BigInteger;

import static java.lang.Math.abs;
import static java.lang.Math.floorMod;

public final class MathUtils {
    private MathUtils(){
    }

    public static boolean equalsWithShift(int[] array1, int[] array2, int shift){
        if (array1.length != array2.length)
            return false;
        int n = array1.length;
        if (n == 0)
            return true;
        int j = floorMod(shift, n);
        for(int i = 0; i < n; ++i){
            if (array1[i] != array2[j])
                return false;
            j = (j + 1) % n;
        }
        return true;

    }
}
