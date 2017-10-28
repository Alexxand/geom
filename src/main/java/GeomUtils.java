import java.math.BigInteger;

import static java.lang.Math.abs;

public final class GeomUtils {
    private GeomUtils(){

    }

    public static BigInteger triangleArea(Vector p1, Vector p2, Vector p3){
        return triangleArea(p2.diff(p1), p3.diff(p1));
    }

    public static BigInteger triangleArea(Vector v1, Vector v2){
        return v1.dotProduct(v2.crossProduct());
    }

    /**
     * Calculate the distance between point p and line, which includes points a1 and a2.
     * @param p - just point
     * @param a1 - first point of line
     * @param a2 - second point of line
     * @return the distance
     */
    public static PseudoRational distance(Vector p, Vector a1, Vector a2){
        if (a1.equals(a2))
            throw new IllegalArgumentException("a1 == a2");
        BigInteger area = triangleArea(p, a1, a2).abs();
        BigInteger baseSquareLength = a1.diff(a2).squareLength();
        return new PseudoRational(area, baseSquareLength);
    }
}
