// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.vector;

import yapi.internal.runtimeexceptions.MathException;

public class Matrix {

    public static void main(String[] args) {
        Matrix matrix = new Matrix(2, 3);
        matrix.set(new Vector(1, 0), new Vector(2, -6), new Vector(3, 7));
        System.out.println(matrix);
        System.out.println(matrix.transpose());
    }

    private Vector[] mat;

    private int row;
    private int column;

    public Matrix(int row, int column) {
        mat = new Vector[column];
        this.row = row;
        this.column = column;
    }

    public void set(Vector vector, int column) {
        System.out.println(this.row + " " + this.column + " " + vector.size() + " " + vector + " " + column);
        if (vector.size() != this.row) {
            throw new MathException();
        }
        if (!(column < this.column && column >= 0)) {
            throw new MathException();
        }
        mat[column] = vector;
    }

    public void set(Vector... vectors) {
        if (vectors.length != column) {
            throw new MathException();
        }
        for (int i = 0; i < vectors.length; i++) {
            set(vectors[i], i);
        }
    }

    public void add(Matrix matrix) {
        if (matrix.row != row || matrix.column != column) {
            throw new MathException();
        }
        for (int i = 0; i < matrix.mat.length; i++) {
            mat[i].addVector(matrix.mat[i]);
        }
    }

    public void multiply(double d) {
        for (Vector v : mat) {
            v.multiplyVector(d);
        }
    }

    public Matrix transpose() {
        Matrix matrix = new Matrix(column, row);
        for (int i = 0; i < column; i++) {
            matrix.set(getVectorOfRow(i), i);
        }
        return matrix;
    }

    private Vector getVectorOfRow(int row) {
        if (!(row < column && row >= 0)) {
            throw new MathException();
        }
        Vector vector = new Vector(this.row);
        for (int i = 0; i < this.row; i++) {
            vector.setVector(i, mat[i].get(row));
        }
        return vector;
    }

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        st.append("/");
        for (int i = 0; i < column; i++) {
            if (i != 0) {
                st.append("\n");
            }
            st.append(getVectorOfRow(i).toMatrixString());
        }
        st.append("/");
        return st.toString();
    }

}