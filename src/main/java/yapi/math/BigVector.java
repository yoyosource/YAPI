package yapi.math;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;

public class BigVector {

    private static final MathContext mathContext = new MathContext(200);
    private BigDecimal[] vec;

    public BigVector(int dimensions) {
        vec = new BigDecimal[dimensions];
        Arrays.fill(vec, BigDecimal.ZERO);
    }

    public BigVector(BigDecimal... values) {
        vec = values;
    }


    private BigDecimal[] getVec() {
        return vec;
    }

    /**
     *
     * @since Version 1
     *
     * @param vec
     */
    public void setVec(BigDecimal[] vec) {
        if (this.vec.length == vec.length) {
            this.vec = vec;
        }
    }

    private BigDecimal[] invertVector() {
        BigDecimal[] vector = this.vec;
        for (int i = 0; i < vector.length; i++) {
            vector[i] = vector[i].multiply(BigDecimal.valueOf(-1), mathContext);
        }
        return vector;
    }

    /**
     *
     * @since Version 1
     *
     * @param dimension
     * @param value
     */
    public void setVector(int dimension, BigDecimal value) {
        if (!(dimension < 0 || dimension > vec.length)) {
            vec[dimension] = value;
        }
    }

    /**
     *
     * @since Version 1
     *
     * @param vector
     */
    public void addVector(BigVector vector) {
        if (this.vec.length == vector.vec.length) {
            int i = 0;
            for (BigDecimal j : vector.getVec()) {
                this.vec[i] = this.vec[i].add(j, mathContext);
                i++;
            }
        } else if (this.vec.length > vector.vec.length) {
            try {
                int i = 0;
                for (BigDecimal j : vector.getVec()) {
                    this.vec[i] = this.vec[i].add(j, mathContext);
                    i++;
                }
            } catch (IndexOutOfBoundsException e) {
                // Ignore the IndexOutOfBoundsException
            }
        }
    }

    /**
     *
     * @since Version 1
     *
     * @param vector
     */
    public void subtractVector(BigVector vector) {
        if (this.vec.length == vector.vec.length) {
            int i = 0;
            for (BigDecimal j : vector.invertVector()) {
                this.vec[i] = this.vec[i].add(j, mathContext);
                i++;
            }
        } else if (this.vec.length > vector.vec.length) {
            try {
                int i = 0;
                for (BigDecimal j : vector.invertVector()) {
                    this.vec[i] = this.vec[i].add(j, mathContext);
                    i++;
                }
            } catch (IndexOutOfBoundsException e) {
                // Ignore the IndexOutOfBoundsException
            }
        }
    }

    /**
     *
     * @since Version 1
     *
     * @param r
     */
    public void multiplyVector(BigDecimal r) {
        for (int i = 0; i < vec.length; i++) {
            vec[i] = vec[i].multiply(r, mathContext);
        }
    }

    /**
     *
     * @since Version 1
     *
     * @param r
     */
    public void divideVector(BigDecimal r) {
        for (int i = 0; i < vec.length; i++) {
            vec[i] = vec[i].divide(r, mathContext);
        }
    }

    /**
     *
     * @since Version 1
     *
     * @param vector
     * @return
     */
    public BigDecimal multiplyVectorScalar(BigVector vector) {
        BigDecimal value = BigDecimal.ZERO;
        try {
            for (int i = 0; i < this.vec.length; i++) {
                value = value.add(this.vec[i].multiply(vector.getVec()[i], mathContext), mathContext);
            }
        } catch (IndexOutOfBoundsException e) {
            // Ignore the IndexOutOfBoundsException
        }
        return BigDecimalMath.acos(value.divide(length().multiply(vector.length(), mathContext), mathContext), mathContext);
    }

    /**
     *
     * @since Version 1
     *
     * @param vector
     */
    public BigVector crossProduct(BigVector vector) {
        if (this.vec.length != 3 || vector.vec.length != 3) {
            return null;
        }
        BigDecimal[] doubles = new BigDecimal[3];
        doubles[0] = this.vec[1].multiply(vector.vec[2], mathContext).subtract(this.vec[2].multiply(vector.vec[1], mathContext), mathContext);
        doubles[1] = this.vec[2].multiply(vector.vec[0], mathContext).subtract(this.vec[0].multiply(vector.vec[2], mathContext), mathContext);
        doubles[2] = this.vec[0].multiply(vector.vec[1], mathContext).subtract(this.vec[1].multiply(vector.vec[0], mathContext), mathContext);
        return new BigVector(doubles);
    }

    /**
     *
     * @since Version 1
     *
     * @return
     */
    public BigDecimal length() {
        BigDecimal value = BigDecimal.ZERO;
        for (int i = 0; i < vec.length; i++) {
            value = value.add(vec[i].multiply(vec[i], mathContext), mathContext);
        }
        return BigDecimalMath.sqrt(value, mathContext);
    }

    /**
     *
     * @since Version 1
     *
     * @param i
     * @return
     */
    public BigDecimal get(int i) {
        return vec[i];
    }

    /**
     *
     * @since Version 1
     *
     * @return
     */
    public BigVector copy() {
        return new BigVector(Arrays.copyOf(vec, vec.length));
    }


    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        st.append("Vector[ ");
        for (int i = 0; i < vec.length; i++) {
            if (i == 0) {
                st.append("x=").append(vec[i]);
            } else if (i == 1) {
                st.append("y=").append(vec[i]);
            } else if (i == 2) {
                st.append("z=").append(vec[i]);
            } else if (i == 3) {
                st.append("w=").append(vec[i]);
            } else {
                st.append((i + 1)).append("=").append(vec[i]);
            }
            if (i < vec.length) {
                st.append(" ");
            }
        }
        st.append("]");
        return st.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector)) return false;
        BigVector vector = (BigVector) o;
        return Arrays.equals(vec, vector.vec);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(vec);
    }

}
