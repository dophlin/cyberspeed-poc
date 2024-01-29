package org.cyberspeed;

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

    public void fillTestMatrix() {
        matrix[0][0] = "5x";
        matrix[0][1] = "C";
        matrix[0][2] = "F";
        matrix[1][0] = "F";
        matrix[1][1] = "F";
        matrix[1][2] = "B";
        matrix[2][0] = "F";
        matrix[2][1] = "D";
        matrix[2][2] = "F";
    }

    public void printMatrix() {
        System.out.println("");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
