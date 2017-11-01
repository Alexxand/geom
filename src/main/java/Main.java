import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Polygon.PolygonBuilder polygonBuilder = Polygon.builder();
        for(int i = 0; i < n; i++ ){
            BigInteger x = new BigInteger(scanner.next());
            BigInteger y = new BigInteger(scanner.next());
            Vector vertex = new Vector(x,y);
            polygonBuilder.append(vertex);
        }
        Polygon polygon = polygonBuilder.build();
        Arrays.stream(polygon.getMinimumBoundingBox(PseudoRational::mul)).map(i -> i + 1).forEach(e -> System.out.printf("%d ", e));
    }
}
