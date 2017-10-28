import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;


public class Polygon {
    private Vector[] vertices;

    public Polygon(List<Vector> vertices){
        this(vertices.toArray(new Vector[0]));
    }

    public Polygon(Vector[] vertices){
        this.vertices = vertices;
    }

    private int[] getKeyIndexes(){
        int[] keyIndexes = new int[5];
        int iLeft = -1;
        int iRight = -1;
        int iBottom = -1;
        int iTop = -1;

        for(int i = 0; i < vertices.length; ++i){
            if(iLeft == -1 || vertices[iLeft].getX().compareTo(vertices[i].getX()) > 0){
                iLeft = i;
            }
            if(iRight == -1 || vertices[iRight].getX().compareTo(vertices[i].getX()) < 0){
                iRight = i;
            }
            if(iBottom == -1 || vertices[iBottom].getY().compareTo(vertices[i].getY()) > 0){
                iBottom = i;
            }
            if(iTop == -1 || vertices[iTop].getY().compareTo(vertices[i].getY()) < 0){
                iTop = i;
            }
        }
        keyIndexes[0] = iBottom;
        keyIndexes[1] = iRight;
        keyIndexes[2] = iTop;
        keyIndexes[3] = iLeft;
        keyIndexes[4] = -1;
        return keyIndexes;
    }

    private Vector[] getKeyVectors(int[] keyIndexes, int skipPos){
        Vector[] keyVectors = new Vector[4];
        int j = 0;
        for(int i = 0; i < 5; ++i){
            if (i == skipPos)
                continue;
            int curIndex = keyIndexes[i];
            int nextIndex = (curIndex + 1) % vertices.length;
            keyVectors[j] = vertices[nextIndex].diff(vertices[curIndex]);
            ++j;
        }
        return keyVectors;
    }

    private static Vector[] getAdjointVectors(Vector[] vectors){
        Vector[] adjointVectors = new Vector[4];
        adjointVectors[0] = vectors[0];
        adjointVectors[1] = vectors[1].crossProduct();
        adjointVectors[2] = vectors[2].inverse();
        adjointVectors[3] = vectors[3].crossProduct().inverse();
        return adjointVectors;
    }

    /**
     * returns an index of vector in the given array
     */
    private static int compareAngles(Vector[] vectors){
        int iMin = -1;
        for(int i = 0; i < vectors.length; ++i){
            if(iMin == -1 || GeomUtils.triangleArea(vectors[iMin], vectors[i]).compareTo(BigInteger.ZERO) < 0){
                iMin = i;
            }
        }
        return iMin;
    }

    private int getMinAngleIndexPos(int[] keyIndexes, int skipPos){
        Vector[] keyVectors = getKeyVectors(keyIndexes, skipPos);
        Vector[] adjointVectors = getAdjointVectors(keyVectors);
        int minIndexPos = compareAngles(adjointVectors);
        if (minIndexPos >= skipPos)
            minIndexPos++;
        return minIndexPos;
    }

    private int[] rotate(int[] keyIndexes, int skipPos, int rotatePos){
        int n = keyIndexes.length;
        int[] result = new int[n];
        int j = 0;
        for(int i = 0; i < n; ++i){
            if(i == skipPos)
                continue;
            int curIndex = keyIndexes[i];
            result[j++] = curIndex;
            if(i == rotatePos)
                result[j++] = (curIndex + 1) % vertices.length;
        }
        return result;
    }

    private PseudoRational getHeight(int[] keyIndexes, int skipPos){
        Vector p = vertices[keyIndexes[(skipPos + 3) % 5]];
        Vector a1 = vertices[keyIndexes[skipPos]];
        Vector a2 = vertices[keyIndexes[(skipPos + 1) % 5]];
        return GeomUtils.distance(p, a1, a2);
    }

    private PseudoRational getWidth(int[] keyIndexes, int skipPos){
        Vector p = vertices[keyIndexes[(skipPos + 2) % 5]];
        Vector a1 = vertices[keyIndexes[(skipPos + 4) % 5]];
        Vector ba1 = vertices[keyIndexes[skipPos]];
        Vector ba2 = vertices[keyIndexes[(skipPos + 1) % 5]];
        Vector bv = ba2.diff(ba1);
        Vector a2 = a1.sum(bv.crossProduct());
        return GeomUtils.distance(p, a1, a2);
    }

    private PseudoRational getObjective(int[] keyIndexes, int skipPos, BinaryOperator<PseudoRational> objectiveFunction){
        PseudoRational width = getWidth(keyIndexes, skipPos);
        PseudoRational height = getHeight(keyIndexes, skipPos);
        return objectiveFunction.apply(width, height);
    }

    private int[] getCaliperIndexes(int[] keyIndexes, int skipPos){
        int[] res = new int[4];
        res[0] = keyIndexes[skipPos];
        res[1] = keyIndexes[(skipPos + 4) % 5];
        res[2] = keyIndexes[(skipPos + 3) % 5];
        res[3] = keyIndexes[(skipPos + 2) % 5];
        return res;
    }

    public int[] getMinimumBoundingBox(BinaryOperator<PseudoRational> objectiveFunction){
        int[] keyIndexes = getKeyIndexes();
        int skipPos = 4;
        int[] firstKeyIndexes = null;
        int firstSkipPos = 0;
        boolean firstRotate = true;
        PseudoRational objective;
        PseudoRational minObjective = null;
        int[] minCaliperIndexes = null;
        while(true) {
            int rotatePos = getMinAngleIndexPos(keyIndexes, skipPos);
            assert rotatePos != skipPos;
            keyIndexes = rotate(keyIndexes, skipPos, rotatePos);
            skipPos = rotatePos < skipPos? rotatePos: rotatePos - 1;
            if (firstRotate){
                firstKeyIndexes = keyIndexes.clone();
                firstSkipPos = skipPos;
                minObjective = getObjective(keyIndexes, skipPos, objectiveFunction);
                minCaliperIndexes = getCaliperIndexes(keyIndexes, skipPos);
                firstRotate = false;
            } else {
                if (MathUtils.equalsWithShift(firstKeyIndexes, keyIndexes, skipPos - firstSkipPos)){
                    break;
                }
                objective = getObjective(keyIndexes, skipPos, objectiveFunction);
                if (minObjective.compareTo(objective) > 0) {
                    minObjective = objective;
                    minCaliperIndexes = getCaliperIndexes(keyIndexes, skipPos);
                }
            }
        }
        return minCaliperIndexes;
    }

    public static PolygonBuilder builder(){
        return new PolygonBuilder();
    }

    public static class PolygonBuilder{
        List<Vector> vertices = new ArrayList<>();

        private PolygonBuilder(){
        }

        public void append(Vector vertex){
            vertices.add(vertex);
        }

        public Polygon build(){
            return new Polygon(vertices);
        }
    }
}
