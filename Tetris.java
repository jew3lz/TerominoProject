package com.TerominoProject;

import com.TerominoProject.Entity.Cell;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by JEWELZ on 10/4/15.
 * 用于构建俄罗斯方块项目的整体流程
 */
public class Tetris extends JPanel {
    private int score;//分数
    private int lines;//销毁的行数
    private Cell[][] wall;//背景墙
    private Tetromino tetromino;//正在下落的方块
    private Tetromino nextOne;//下一个四格方块

    public static final int ROWS = 20; //背景墙的行数
    public static final int COLS = 10; //背景墙的列数

    //背景图片
    public static BufferedImage backGround;
    public static BufferedImage overImage;
    public static BufferedImage J;
    public static BufferedImage I;
    public static BufferedImage S;
    public static BufferedImage Z;
    public static BufferedImage O;
    public static BufferedImage L;
    public static BufferedImage T;

    static {
        try {
            backGround = ImageIO.read(Tetris.class.getResource("tetris.png"));
            overImage = ImageIO.read(Tetris.class.getResource("game-over.png"));
            I = ImageIO.read(Tetris.class.getResource("I.png"));
            O = ImageIO.read(Tetris.class.getResource("O.png"));
            S = ImageIO.read(Tetris.class.getResource("S.png"));
            T = ImageIO.read(Tetris.class.getResource("T.png"));
            Z = ImageIO.read(Tetris.class.getResource("Z.png"));
            L = ImageIO.read(Tetris.class.getResource("L.png"));
            J = ImageIO.read(Tetris.class.getResource("J.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean gameOver;
    private boolean pause;
    private static Timer timer;
    private long interval = 600;


    /**
     * 重写paint()修改原有的绘制方法
     *
     * @param g
     */
    public void paint(Graphics g) {
        g.drawImage(backGround, 0, 0, null);//画背景
        g.translate(15, 15);//坐标系平移,在中间落下方块
        paintWall(g);
        paintTetromino(g);
        paintNextOne(g);
        paintScore(g);
    if(gameOver){
        g.drawImage(overImage,0,0,null);
    }
    }

    public static final int FONT_COLOR = 0x667799;
    public static final int size = 30;

    /* 绘制分数 */
    private void paintScore(Graphics g) {
        int x = 290;
        int y = 160;
        g.setColor(new Color(FONT_COLOR));
        Font font = g.getFont();
        font = new Font(font.getName(),font.getStyle(),size);
        g.setFont(font);
        String str = "GRADE:" + score;
        g.drawString(str,x,y);
        y += 56;
        str = "LINES:" + lines;
        g.drawString(str,x,y);
        y += 56;
        str = "[P]Pause";
        if(pause){
            str = "[C]Continue";
        }
        if(gameOver){
            str = "S[Start]";
        }
        g.drawString(str,x,y);
    }

    public void action() {
        wall = new Cell[ROWS][COLS];
//        wall[2][2] = new Cell(2, 2, T);
        tetromino = Tetromino.randomOne();
        nextOne = Tetromino.randomOne();

        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int key = e.getKeyCode();
                if(key == KeyEvent.VK_Q){
                    System.exit(0);
                }
                if(gameOver){
                    if(key == KeyEvent.VK_S){
                        startAction();
                        repaint();
                    }
                    return;
                }

                if(pause){
                    if(key == KeyEvent.VK_C){
                        continueAction();
                        repaint();
                    }
                    return;
                }

                switch (key) {
                    case KeyEvent.VK_DOWN:
                        softDropAction();
                        break;
                    case KeyEvent.VK_LEFT:
                        moveLeftAction();
                        break;
                    case KeyEvent.VK_RIGHT:
                        moveRightAction();
                        break;
                    case KeyEvent.VK_SPACE:
                        hardDropAction();
                        break;
                    case KeyEvent.VK_UP:
                        rotateRightAction();
                        break;
                    case KeyEvent.VK_P:
                        pauseAction();
                        break;
                    case KeyEvent.VK_S:
                        startAction();
                        break;
                }
                repaint();
            }
        };
        // 下落流程: 监听键盘事件->如果下箭头按下->
        // 执行下落算法tetromino.softDrop()->
        // 修改每个格子对象的数据->调用repaint()->
        // 尽快调用paint()->paint方法会根据当前的数据
        // 重新绘制界面 -> 看到移动以后的方块了
        // 绑定事件到当前面板
        this.requestFocus();
        this.addKeyListener(keyAdapter);
    }

    private void continueAction() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                softDropAction();
                repaint();
            }
        },interval,interval);
        pause = false;
    }

    private void clearWall(){
        for(Cell [] line : wall){
            Arrays.fill(line,null);
        }
    }


    public void startAction(){
        pause = false;
        gameOver = false;
        score = 0;
        lines = 0;
        clearWall();

        tetromino = Tetromino.randomOne();
        nextOne = Tetromino.randomOne();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                softDropAction();
                repaint();
            }

        },interval,interval);
       // System.out.println(timer);

    }

    public void pauseAction(){
        System.out.println(timer);
        timer.cancel();
        System.out.println("ssss");
        pause = true;
    }


    private void rotateRightAction() {
        tetromino.rotateLeft();
        if(outOfBounds()||coincide()){
            tetromino.rotateLeft();
        }
    }

    private void hardDropAction() {
        while (canDrop()) {
            tetromino.softDrop();
        }
        landIntoWall();
        destroyLines();
            checkGameOverAction();
        tetromino = nextOne;
        nextOne = Tetromino.randomOne();
    }


//    private void dropAction() {
//        if (!outOfBounds() && !coincide()) {
//            tetromino.softDrop();
//        }
//    }

    private void softDropAction() {
        if (canDrop()) {
            tetromino.softDrop();
        } else {
            //一系列流程控制
            landIntoWall();
            destroyLines();
                checkGameOverAction();
            tetromino = nextOne;
            nextOne = Tetromino.randomOne();
        }
    }

    private void checkGameOverAction() {
        if(wall[0][4] != null){
            gameOver = true;
            timer.cancel();
        }
    }

    private void moveLeftAction() {
        tetromino.moveLeft();
        if (outOfBounds() || coincide()) {
            tetromino.moveRight();
        }
    }

    private void moveRightAction() {
        tetromino.moveRight();
        if (outOfBounds() || coincide()) {
            tetromino.moveLeft();
        }
    }

    public final static int CELL_SIZE = 26;

    //画墙方法
    private void paintWall(Graphics g) {
        for (int i = 0; i < wall.length; i++) {
            for (int j = 0; j < wall[i].length; j++) {
                Cell cell = wall[i][j];
                if (cell == null) {
                    g.drawRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                } else {
                    g.drawImage(cell.getBufferedImage(), j * CELL_SIZE - 1, i * CELL_SIZE - 1, null);
                }
            }
        }
    }

    /*
    绘制正在下落的方块
     */
    private void paintTetromino(Graphics g) {
        if (tetromino == null) {
            return;
        }
        Cell[] cells = tetromino.cells;
        for (int i = 0; i < cells.length; i++) {
            Cell cell = cells[i];
            g.drawImage(cell.getBufferedImage(), cell.getCol() * CELL_SIZE - 1, cell.getRow() * CELL_SIZE - 1, null);
        }
    }

    private void paintNextOne(Graphics g) {
        if (nextOne == null) {
            return;
        }
        Cell[] cells = nextOne.cells;
        for (int i = 0; i < cells.length; i++) {
            Cell cell = cells[i];
            int x = (cell.getCol() + 10) * CELL_SIZE;
            int y = (cell.getRow() + 1) * CELL_SIZE;
            g.drawImage(cell.getBufferedImage(), x - 1, y - 1, null);
        }
    }

    /*
    检查当前正在下落的方块是否左右越界
     */
    private boolean outOfBounds() {
        Cell[] cells = tetromino.cells;
        for (int i = 0; i < cells.length; i++) {
            if (cells[i].getCol() < 0 || cells[i].getCol() >= COLS||cells[i].getRow()<0) {
                return true;
            }
        }
        return false;
    }


    /*
    检查是否与墙上的砖块重叠
     */
    private boolean coincide() {
        Cell[] cells = tetromino.cells;
        for (int i = 0; i < cells.length; i++) {
            Cell cell = cells[i];
            if (cell.getRow() >= 0 && cell.getRow() < ROWS && wall[cell.getRow()][cell.getCol()] != null
                    && cell.getCol() >= 0 && cell.getCol() <= COLS)
                return true;
        }
        return false;
    }

    private static int[] scoreTable = {0, 1, 10, 50, 100};

    private void destroyLines() {
        int lines = 0;
        for (int row = 0; row < wall.length; row++) {
            if (fullCells(row)) {
                deleteRow(row);
                lines++;
            }
        }
        this.score += scoreTable[lines];
        this.lines += lines;
    }


    private void deleteRow(int row) {
        for (int i = row; i > 0; i--) {
            System.arraycopy(wall[i - 1], 0, wall[i], 0, COLS);//覆盖
        }
        Arrays.fill(wall[0], null);//填充第一行全为空

    }

    //    private int fullCells(){
//        int acount;
//        for(int i = 0;i<wall[ROWS].length;i++){
//            acount = 0;
//            for(int j=0;j<wall.length;j++){
//                if (wall[i][j] == null){
//                    break;
//                }else {
//                    return i;
//                }
//            }
//        }
//        return -1;
//    }
    private boolean fullCells(int row) {
        Cell[] line = wall[row];
        for (Cell cell : line) {
            if (cell == null) {
                return false;
            }
        }
        return true;
    }

    private boolean canDrop() {
        Cell[] cells = tetromino.cells;
        for (int i = 0; i < cells.length; i++) {
            Cell cell = cells[i];
            int row = cell.getRow();

            if (row == ROWS - 1) {
                return false;
            }
        }
        for (Cell cell : cells) {
            int row = cell.getRow() + 1;
            int col = cell.getCol();

            if (row >= 0 && row < ROWS && col >= 0 && col <= COLS && wall[row][col] != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 实现方块着落在墙上 *
     */
    private void landIntoWall() {
        Cell[] cells = tetromino.cells;
//        for (Cell cell : cells) {
//            int row = cell.getRow();
//            int col = cell.getCol();
//            wall[row][col] = cell;
//        }
        for (int i = 0; i < cells.length; i++) {
            Cell cell = cells[i];
            int row = cell.getRow();
            int col = cell.getCol();
            wall[row][col] = cell;
        }
    }

    /**
     * 判断是否能下落,如果能返回true.与move不同.*
     */
    public static void main(String args[]) {
        JFrame jFrame = new JFrame();
        //在加载Tetris的时候,会执行静态代码块.装载了图片素材为图片对象
        Tetris tetris = new Tetris();
        //面板设置为蓝色进行测试
        tetris.setBackground(new Color(0x2345ff));
        jFrame.add(tetris);
        jFrame.setSize(530, 580);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        tetris.action();
    }
}
