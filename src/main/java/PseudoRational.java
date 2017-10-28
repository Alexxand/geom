import java.math.BigInteger;

/**
 * rational number for which we only know the numerator and square of the denominator
 */
public class PseudoRational implements Arithmetic<PseudoRational>, Comparable<PseudoRational> {

    private final BigInteger p;
    private final BigInteger sq;

    public PseudoRational(int p, int sq) {
        this(BigInteger.valueOf(p), BigInteger.valueOf(sq));
    }

    public PseudoRational(BigInteger p, BigInteger sq){
        if (sq.compareTo(BigInteger.ZERO) <= 0){
            throw new IllegalArgumentException("q <= 0");
        }
        this.p = p;
        this.sq = sq;
    }
    @Override
    public PseudoRational mul(PseudoRational o) {
        return new PseudoRational(p.multiply(o.p), sq.multiply(o.sq));
    }

    @Override
    public PseudoRational div(PseudoRational o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PseudoRational sum(PseudoRational o) {
        if (!sq.equals(o.sq))
            throw new UnsupportedOperationException();
        return new PseudoRational(p.add(o.p), sq);
    }

    @Override
    public PseudoRational diff(PseudoRational o) {
        if (!sq.equals(o.sq))
            throw new UnsupportedOperationException();
        return new PseudoRational(p.subtract(o.p), sq);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PseudoRational that = (PseudoRational) o;

        if (!p.equals(that.p)) return false;
        return sq.equals(that.sq);
    }

    @Override
    public int hashCode() {
        int result = p.hashCode();
        result = 31 * result + sq.hashCode();
        return result;
    }

    @Override
    public int compareTo(PseudoRational o) {
        BigInteger left = p.multiply(p).multiply(o.sq);
        BigInteger right = sq.multiply(o.p).multiply(o.p);
        return left.compareTo(right);
    }
}
