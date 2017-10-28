import java.math.BigInteger;

public class Vector {
    private final BigInteger x;
    private final BigInteger y;

    public Vector(int x, int y) {
        this(BigInteger.valueOf(x), BigInteger.valueOf(y));
    }

    public Vector(BigInteger x, BigInteger y){
        this.x = x;
        this.y = y;
    }

    public BigInteger getX() {
        return x;
    }

    public BigInteger getY() {
        return y;
    }

    public static final Vector ZERO = new Vector(0,0);

    public BigInteger squareLength(){
        return x.multiply(x).add(y.multiply(y));
    }

    public Vector diff(Vector o){
        return new Vector(x.subtract(o.x), y.subtract(o.y));
    }

    public Vector sum(Vector o){
        return new Vector(x.add(o.x), y.add(o.y));
    }

    public Vector inverse(){
        return ZERO.diff(this);
    }

    public Vector crossProduct(){
        return new Vector(this.y, this.x.negate());
    }

    public BigInteger dotProduct(Vector other){
        return this.x.multiply(other.x).add(this.y.multiply(other.y));
    }
}
