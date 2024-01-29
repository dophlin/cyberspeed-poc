package org.cyberspeed.model;

public class Matrix {
    private final String[][] matrix;
    private final int rows;
    private final int cols;

    public Matrix(int rows, int cols) {
        matrix = new String[rows][cols];
        this.rows = rows;
        this.cols = cols;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public void setValue(int row, int col, String value) {
        if (row >= 0 && row < matrix.length && col >= 0 && col < matrix[0].length) {
            matrix[row][col] = value;
        } else {
            System.out.println("Invalid row or column index");
        }
    }

    public String getValue(int row, int col) {
        if (row >= 0 && row < matrix.length && col >= 0 && col < matrix[0].length) {
            return matrix[row][col];
        } else {
            System.out.println("Invalid row or column index");
            throw new RuntimeException("InvalidRowOrColumn");
        }
    }

    public String[][] getMatrix() {
        return matrix;
    }

    public void printMatrix() {
        for (String[] strings : matrix) {
            for (String string : strings) {
                System.out.print(string + "\t");
            }
            System.out.println();
        }
    }
}
