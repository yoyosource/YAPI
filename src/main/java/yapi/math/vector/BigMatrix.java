package yapi.math.vector;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;

public class BigMatrix {

    private final MathContext mathContext = new MathContext(200);
    private final BigDecimal[][] matrix;

    public BigMatrix(int n, int m) {
        matrix = new BigDecimal[n][m];
        BigDecimal[] temp = new BigDecimal[m];
        Arrays.fill(temp, BigDecimal.ZERO);
        Arrays.fill(matrix, temp);
    }

    public void setNumber(int n, int m, BigDecimal number) {
        matrix[n][m] = number;
    }

    public BigDecimal getNumber(int n, int m) {
        return matrix[n][m];
    }

    private String dimensions() {
        return rows() + ":" + columns();
    }

    public int rows() {
        return matrix.length;
    }

    public int columns() {
        return matrix[0].length;
    }

    public BigMatrix add(BigMatrix matrix) {
        return this;
    }

    public BigMatrix subtract(BigMatrix matrix) {
        return this;
    }

    public BigMatrix multiply(BigDecimal value) {
        BigMatrix result = new BigMatrix(rows(), columns());
        for (int n = 0; n < rows(); n++) {
            for (int m = 0; m < columns(); m++) {
                result.setNumber(n, m, getNumber(n, m).multiply(value, mathContext));
            }
        }
        return result;
    }

    public BigMatrix square() {
        return multiplyQuadratic(this);
    }

    public BigMatrix cube() {
        return multiplyQuadratic(multiplyQuadratic(this));
    }

    public BigMatrix power(int e) {
        if (e == 2) {
            return square();
        }
        if (e == 3) {
            return cube();
        }
        BigMatrix result = this;
        for (int i = 0; i < e; i++) {
            result = multiplyQuadratic(result);
        }
        return result;
    }

    public BigMatrix multiply(BigMatrix matrix) {
        if (matrix.dimensions().equals(dimensions())) {
            return multiplyQuadratic(matrix);
        }
        return null;
    }

    public BigVector multiply(BigVector vector) {
        if (vector.size() == columns()) {
            return multiplyVector(vector);
        }
        return null;
    }

    private BigMatrix multiplyQuadratic(BigMatrix matrix) {
        BigMatrix result = new BigMatrix(rows(), columns());

        for (int n = 0; n < rows(); n++) {
            for (int m = 0; m < columns(); m++) {
                BigDecimal value = BigDecimal.ZERO;

                for (int i = 0; i < rows(); i++) {
                    value = value.add(getNumber(i, m).multiply(matrix.getNumber(n, i), mathContext), mathContext);
                }

                result.setNumber(n, m, value);
            }
        }

        return result;
    }

    private BigVector multiplyVector(BigVector vector) {
        BigVector bigVector = new BigVector(vector.size());

        for (int i = 0; i < vector.size(); i++) {
            BigDecimal value = BigDecimal.ZERO;

            for (int n = 0; n < vector.size(); n++) {
                value = value.add(getNumber(n, i).multiply(vector.get(n), mathContext), mathContext);
            }

            bigVector.setVector(i, value);
        }

        return bigVector;
    }

}
