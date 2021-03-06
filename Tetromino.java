package com.TerominoProject;

import com.TerominoProject.Entity.Cell;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by JEWELZ on 10/4/15.
 * 四个格子的方块
 */
public abstract class Tetromino {
    protected Cell[] cells = new Cell[4];//存放四个格子,表示一个方块

    protected State[] states;

    protected int index = 10000;

    protected class State{
        int row0,col0,row1,col1,row2,col2,row3,col3;

        public State(int row0, int col0, int row1, int col1, int row2, int col2, int row3, int col3) {
            this.row0 = row0;
            this.col0 = col0;
            this.row1 = row1;
            this.col1 = col1;
            this.row2 = row2;
            this.col2 = col2;
            this.row3 = row3;
            this.col3 = col3;
        }
    }

    public void rotateRight(){
        index++;
        rotate(index);
    }

    public void rotateLeft(){
        index--;
        rotate(index);
    }

    private void rotate(int index){
        State s =states[index%states.length];
        Cell o = cells[0];
        int row = o.getRow();
        int col = o.getCol();
        cells[1].setRow(row+s.row1);
        cells[1].setCol(col+s.col1);
        cells[2].setRow(row+s.row2);
        cells[2].setCol(col+s.col2);
        cells[3].setRow(row+s.row3);
        cells[3].setCol(col+s.col3);
    }

    /**
     * 工厂方法,随机产生4格方块 *
     */
    public static Tetromino randomOne() {
        Random random = new Random();
        int type = random.nextInt(7);
        switch (type){
            case 0:
                return new T();
            case 1:
                return new I();
            case 2:
                return new S();
            case 3:
                return new O();
            case 4:
                return new Z();
            case 5:
                return new L();
            case 6:
                return new J();
        }
        return null;
    }

    public void softDrop() {
        for(int i=0;i<cells.length;i++){
            cells[i].drop();
        }
    }

    public void moveLeft() {
        for(int i=0;i<cells.length;i++){
            cells[i].moveLeft();
        }

    }

    public void moveRight() {
        for(int i=0;i<cells.length;i++){
            cells[i].moveRight();
        }
    }
    //覆盖toString方法,显示方块中每个格子的位置
    public String toString() {
        return Arrays.toString(cells);
    }
}
/*
S,Z为了旋转的美观cell[0]的坐标轴为(1,4).其余的为(0,0)
 */
class T extends Tetromino {
    public T() {
        cells[0] = new Cell(0,4,Tetris.T);
        cells[1] = new Cell(0,3,Tetris.T);
        cells[2] = new Cell(0,5,Tetris.T);
        cells[3] = new Cell(1,4,Tetris.T);
        states = new State[4];
        states[0] = new State(0, 0, 0, -1, 0, 1, 1, 0);
        states[1] = new State(0, 0, -1, 0, 1, 0, 0, -1);
        states[2] = new State(0, 0, 0, 1, 0, -1, -1, 0);
        states[3] = new State(0, 0, 1, 0, -1, 0, 0, 1);
    }
}

class I extends Tetromino {
    public I() {
        cells[0] = new Cell(0, 4, Tetris.I);
        cells[1] = new Cell(0, 3, Tetris.I);
        cells[2] = new Cell(0, 5, Tetris.I);
        cells[3] = new Cell(0, 6, Tetris.I);
        states = new State[] { new State(0, 0, 0, -1, 0, 1, 0, 2),
                new State(0, 0, -1, 0, 1, 0, 2, 0) };
    }
}

class S extends Tetromino {
    public S() {
        cells[0] = new Cell(1, 4, Tetris.S);
        cells[1] = new Cell(1, 3, Tetris.S);
        cells[2] = new Cell(0, 4, Tetris.S);
        cells[3] = new Cell(0, 5, Tetris.S);
        states = new State[] { new State(0, 0, 0, -1, -1, 0, -1, 1),
                new State(0, 0, -1, 0, 0, 1, 1, 1) };
    }
}

class Z extends Tetromino {
    public Z() {
        cells[0] = new Cell(1, 4, Tetris.Z);
        cells[1] = new Cell(0, 3, Tetris.Z);
        cells[2] = new Cell(0, 4, Tetris.Z);
        cells[3] = new Cell(1, 5, Tetris.Z);
        states = new State[] { new State(0, 0, -1, -1, -1, 0, 0, 1),
                new State(0, 0, -1, 1, 0, 1, 1, 0) };
    }
}

class O extends Tetromino {
    public O() {
        cells[0] = new Cell(0, 4, Tetris.O);
        cells[1] = new Cell(0, 5, Tetris.O);
        cells[2] = new Cell(1, 4, Tetris.O);
        cells[3] = new Cell(1, 5, Tetris.O);
        states = new State[] { new State(0, 0, 0, 1, 1, 0, 1, 1),
                new State(0, 0, 0, 1, 1, 0, 1, 1) };
    }
}

class L extends Tetromino {
    public L() {
        cells[0] = new Cell(0, 4, Tetris.L);
        cells[1] = new Cell(0, 3, Tetris.L);
        cells[2] = new Cell(0, 5, Tetris.L);
        cells[3] = new Cell(1, 3, Tetris.L);
        states = new State[] { new State(0, 0, 0, 1, 0, -1, -1, 1),
                new State(0, 0, 1, 0, -1, 0, 1, 1),
                new State(0, 0, 0, -1, 0, 1, 1, -1),
                new State(0, 0, -1, 0, 1, 0, -1, -1) };
    }
}

class J extends Tetromino {
    public J() {
        cells[0] = new Cell(0, 4, Tetris.J);
        cells[1] = new Cell(0, 3, Tetris.J);
        cells[2] = new Cell(0, 5, Tetris.J);
        cells[3] = new Cell(1, 5, Tetris.J);
        states = new State[] { new State(0, 0, 0, -1, 0, 1, 1, 1),
                new State(0, 0, -1, 0, 1, 0, 1, -1),
                new State(0, 0, 0, 1, 0, -1, -1, -1),
                new State(0, 0, 1, 0, -1, 0, -1, 1) };

    }
}
