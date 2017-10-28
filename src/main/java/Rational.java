import java.math.BigInteger;

public class Rational implements Arithmetic<Rational>, Comparable<Rational> {
    private BigInteger p;
    private BigInteger q;

    private static Rational ZERO = new Rational(BigInteger.ZERO);

    public Rational(int p){
        this(BigInteger.valueOf(p));
    }

    public Rational(BigInteger p) {
        this(p, BigInteger.ONE);
    }

    public Rational(int p, int q) {
        this(BigInteger.valueOf(p), BigInteger.valueOf(q));
    }

    public Rational(BigInteger p, BigInteger q) {
        if (q.equals(BigInteger.ZERO)){
            throw new IllegalArgumentException("q == 0");
        }
        this.p = p;
        this.q = q;
        simplify();
    }

    public Rational mul(Rational o){
        return new Rational(p.multiply(o.p), q.multiply(o.q));
    }

    public Rational div(Rational o){
        if (o.equals(ZERO))
            throw new ArithmeticException("/ by zero");
        return new Rational(p.multiply(o.q), q.multiply(o.p));
    }

    public Rational sum(Rational o){
        return new Rational(p.multiply(o.q).add(q.multiply(o.p)), q.multiply(o.q));
    }

    public Rational diff(Rational o){
        return new Rational(p.multiply(o.q).subtract(q.multiply(o.p)), q.multiply(o.q));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rational r = (Rational) o;

        return (p.equals(r.p)) && (q.equals(r.q));
    }

    @Override
    public int hashCode() {
        int result = p.hashCode();
        result = 31 * result + q.hashCode();
        return result;
    }

    private void simplify(){
        BigInteger factor = p.gcd(q);
        if (q.compareTo(BigInteger.ZERO) < 0)
            factor = factor.negate();
        p = p.divide(factor);
        q = q.divide(factor);
    }

    public int compareTo(Rational o) {
        BigInteger left = p.multiply(o.q);
        BigInteger right = q.multiply(o.p);
        return left.compareTo(right);
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getQ() {
        return q;
    }
}
