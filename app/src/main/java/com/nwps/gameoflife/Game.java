package com.nwps.gameoflife;

import java.util.Random;

class Game {
    private int rows;
    private int cols;
    private int[][] greed;
    private int[][] nextGenerationGreed;

    Game(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    void init() {
        greed = new int[rows][cols];
        nextGenerationGreed = new int[rows][cols];
    }

    void fillGridRandomly(double density) throws WrongGridDensityException {
        if (density > 1.0 || density < 0.0)
            throw new WrongGridDensityException(density);
        int cells = (int) (rows > cols ? density*rows : density*cols);
        aliveCellsOnInitRandomly(cells);
    }

    private void aliveCellsOnInitRandomly(int cells) {
        Random rand = new Random();
        while (cells > 0) {
            int r = rand.nextInt(rows);
            int c = rand.nextInt(cols);
            if (!isAlive(r, c)) {
                makeAliveOnInit(r, c);
                cells--;
            }
        }
    }

    int getRows() {
        return rows;
    }

    int getCols() {
        return cols;
    }

    private void setRows(int rows) {
        this.rows = rows;
    }

    private void setCols(int cols) {
        this.cols = cols;
    }

    int[][] getGreed() {
        return greed;
    }

    void makeAliveOnInit(int row, int col) {
        greed[row][col] = 1;
        nextGenerationGreed[row][col] = 1;
    }

    void processCell(int row, int col) {
        int aliveNeighbours = countAliveNeighbours(row, col);
        if (!isAlive(row, col)) {
            if (aliveNeighbours == 3) makeAlive(row, col);
            return;
        }
        if (aliveNeighbours < 2 || aliveNeighbours > 3) killCell(row, col);
    }

    private void makeAlive(int row, int col) {
        nextGenerationGreed[row][col] = 1;
    }

    private int countAliveNeighbours(int row, int col) {
        int aliveCnt = 0;
        if (row-1 >= 0 && col-1 >= 0 && greed[row-1][col-1] == 1) aliveCnt++;
        if (row-1 >= 0 && greed[row-1][col] == 1) aliveCnt++;
        if (row-1 >= 0 && col+1 < cols && greed[row-1][col+1] == 1) aliveCnt++;
        if (col-1 >= 0 && greed[row][col-1] == 1) aliveCnt++;
        if (col+1 < cols && greed[row][col+1] == 1) aliveCnt++;
        if (row+1 < rows && col-1 >= 0 && greed[row+1][col-1] == 1) aliveCnt++;
        if (row+1 < rows && greed[row+1][col] == 1) aliveCnt++;
        if (row+1 < rows && col+1 < cols && greed[row+1][col+1] == 1) aliveCnt++;
        return aliveCnt;
    }

    private void killCell(int row, int col) {
        nextGenerationGreed[row][col] = 0;
    }

    private boolean isAlive(int row, int col) {
        return greed[row][col] == 1;
    }

    int[][] getNextGenerationGreed() {
        return nextGenerationGreed;
    }

    void createNextGeneration() {
        for (int row = 0; row < rows; row++)
            for (int col = 0; col < cols; col++)
                processCell(row, col);
        greed = nextGenerationGreed;
    }
}
