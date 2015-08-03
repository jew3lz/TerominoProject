package com.TerominoProject;

import java.awt.image.BufferedImage;

/**
 * Created by JEWELZ on 10/4/15.
 * 格子的类
 */
public class Cell {
    private int row;//行
    private int col;//列
    private BufferedImage bufferedImage;//格子的图

    public Cell(int row, int col, BufferedImage bufferedImage) {
        this.row = row;
        this.col = col;
        this.bufferedImage = bufferedImage;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "col=" + col +
                ", row=" + row +
                '}';
    }

    public void drop() {
        row++;
    }

    public void moveLeft(){
        col--;
    }

    public void moveRight(){
        col++;
    }
}
