package com.fehaja.connect4;

import android.net.wifi.WifiConfiguration;

import java.util.Stack;
import java.util.Vector;

/**
 * Created by fguo on 1/6/2016.
 */
public class ConnectFour {

    private static final int SCORE = 100000;
    char[][] m_cGrid = new char[6][7];
    Stack<Integer> m_history = new Stack <Integer>();
    boolean m_bBlue;  // color for next move
    Vector <CFPair> m_winningMoves;
    static int iterations = 0;

    ConnectFour ()
    {
        // blue starts
        this(true);
    }

    public ConnectFour(boolean bBlue) {

        m_bBlue = bBlue;
        /*
        m_cGrid [0][1] = '1';
        m_cGrid [0][2] = '2';
        m_cGrid [0][3] = '3';
        m_cGrid [0][4] = '4';
        m_cGrid [0][5] = '5';
        m_cGrid [0][6] = '6';
        m_cGrid [0][7] = '7';


        m_cGrid [1][0] = 'a';
        m_cGrid [2][0] = 'b';
        m_cGrid [3][0] = 'c';
        m_cGrid [4][0] = 'd';
        m_cGrid [5][0] = 'e';
        m_cGrid [6][0] = 'f';
*/
/*
* - empty
* O blue
* X red
* o,x winning move
* */


        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                m_cGrid[i][j] = '-';
            }
        }

    }

    ConnectFour (boolean bBlue, char[][] cGrid){
        m_bBlue = bBlue;
        m_cGrid = cGrid;
        // not doing anything with history, as it is not involved in AI computing.
    }

    void print() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                System.out.print(m_cGrid[i][j]);
            }
            System.out.println();
        }
    }

    /*
    drops a piece at the given column.
    @return: drop valid? drop is not valid if column is full.
     */
    boolean drop(int nCol) {
        // assuming nCol
        boolean bRet = false;
        int nRow = 5;
        while (nRow >= 0 && m_cGrid[nRow][nCol] != '-') {
            nRow--;
        }

        if (nRow >= 0) {
            m_cGrid[nRow][nCol] = m_bBlue ? 'O' : 'X';
            bRet = true;
           // track history
            m_history.push(nCol);
            // if valid drop, switch player
            m_bBlue = !m_bBlue;
        }

        return bRet;

    }

    boolean isBlueNext ()
    {
        return m_bBlue;
    }
/*
    undo the last move
    @return the color undone (for next move)
 */
/// change undo fun signature
    boolean undo ()
    {

        int nCol = m_history.pop();
        int nRow = 0;
        for (nRow = 0; m_cGrid[nRow][nCol] == '-' && nRow < 5 ; nRow++ );

        boolean bRet = m_cGrid[nRow][nCol] == 'O';
        // switch player back
        m_bBlue = !m_bBlue;

        m_cGrid[nRow][nCol] = '-';

        return bRet;
        /// need to consider undo from one's win
    }
/*
    when some one wins or when game is tied.
    @return
    -: not done yet
    O, X: the winning color
    +: board full, it is a tie.
    the winning 4 grids will be lowercased in m_cGrid
 */
    char done() {
        char cRet = '-';
        outerloop:

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (m_cGrid[i][j] != '-') {
                    if (m_cGrid[i][j] == m_cGrid[i][j + 1] && m_cGrid[i][j] == m_cGrid[i][j + 2] && m_cGrid[i][j] == m_cGrid[i][j + 3]) {
                        // - growth
                        cRet = Character.toLowerCase(m_cGrid[i][j]);
                        // change to lowercase for the winning 4 pieces
                        m_cGrid[i][j + 1] = m_cGrid[i][j + 2] = m_cGrid[i][j + 3] = m_cGrid[i][j] = cRet;
                        break outerloop;
                    } else if (m_cGrid[i][j] == m_cGrid[i + 1][j] && m_cGrid[i][j] == m_cGrid[i + 2][j] && m_cGrid[i][j] == m_cGrid[i + 3][j]) {
                        // | growth
                        cRet = Character.toLowerCase(m_cGrid[i][j]);
                        // change to lowercase for the winning 4 pieces
                        m_cGrid[i+1][j] = m_cGrid[i+2][j] = m_cGrid[i+3][j] = m_cGrid[i][j] = cRet;

                        break outerloop;
                    } else if (m_cGrid[i][j] == m_cGrid[i + 1][j + 1] && m_cGrid[i][j] == m_cGrid[i + 2][j + 2] && m_cGrid[i][j] == m_cGrid[i + 3][j + 3]) {
                        // \ growth
                        cRet = Character.toLowerCase(m_cGrid[i][j]);
                        // change to lowercase for the winning 4 pieces
                        m_cGrid[i+1][j + 1] = m_cGrid[i+2][j + 2] = m_cGrid[i+3][j + 3] = m_cGrid[i][j] = cRet;
                        break outerloop;
                    }

                }
            }
            for (int j = 3; j < 7; j++) {
                if (m_cGrid[i][j] != '-') {
                    // | growth (no - growth, already handled in above case)
                    if (m_cGrid[i][j] == m_cGrid[i + 1][j] && m_cGrid[i][j] == m_cGrid[i + 2][j] && m_cGrid[i][j] == m_cGrid[i + 3][j]) {
                        cRet = Character.toLowerCase(m_cGrid[i][j]);
                        // change to lowercase for the winning 4 pieces
                        m_cGrid[i+1][j] = m_cGrid[i+2][j] = m_cGrid[i+3][j] = m_cGrid[i][j] = cRet;
                        break outerloop;
                    }
                    // / growth
                    else if (m_cGrid[i][j] == m_cGrid[i + 1][j - 1] && m_cGrid[i][j] == m_cGrid[i + 2][j - 2] && m_cGrid[i][j] == m_cGrid[i + 3][j - 3]) {
                        cRet = Character.toLowerCase(m_cGrid[i][j]);
                        // change to lowercase for the winning 4 pieces
                        m_cGrid[i+1][j - 1] = m_cGrid[i+2][j - 2] = m_cGrid[i+3][j - 3] = m_cGrid[i][j] = cRet;

                        break outerloop;
                    }
                }
            }

        }
        outerloop2:
        for (int i = 3; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                if (m_cGrid[i][j] != '-') {
                    // - growth only
                    if (m_cGrid[i][j] == m_cGrid[i][j + 1] && m_cGrid[i][j] == m_cGrid[i][j + 2] && m_cGrid[i][j] == m_cGrid[i][j + 3]) {
                        cRet = Character.toLowerCase(m_cGrid[i][j]);
                        // change to lowercase for the winning 4 pieces
                        m_cGrid[i][j + 1] = m_cGrid[i][j + 2] = m_cGrid[i][j + 3] = m_cGrid[i][j] = cRet;

                        break outerloop2;
                    }

                }
            }
        }


        // what about out of move?
        if (isFull())
            cRet = '+';



        return cRet;
    }

    boolean isFull ()
    {
        boolean bFull = true;
        for (int j = 0; j < 6; j++){
            if (m_cGrid[0][j] == '-') {
                bFull = false;
                break;
            }
        }
        return bFull;

    }
    char positionAt (int n)
    {
        return m_cGrid [n/7] [n%7];
    }


    class CFPair {
        int row;
        int col;
        CFPair (int r, int c) {
            row = r;
            col = c;
        }
    }

    static class ColumnScorePair{
        int col;
        int score;
        ColumnScorePair (int c, int s)
        {
            col = c;
            score = s;
        }
    }

    /**
     * The following is learning from https://www.gimu.org/connect-four-js/plain/minimax/index.html
     *
     */

    /*
    @return a score for various positions (either horizontal, vertial or diagonal by moving through the board
    positive score for human, negative score for computer
    assuming blue is human, red is computer
     */
    int scorePosition (int nRow, int nCol, int nDeltaY, int nDeltaX)    {
        int nRet = SCORE;
        int nHumanPoints = 0;
        int nComputerPoints = 0;
        Vector<CFPair> vWinningMovesHuman = new Vector<>();
        Vector<CFPair> vWinningMovesCPU = new Vector<>();

        for (int i = 0; i < 4; i++) {
            if (m_cGrid[nRow][nCol] == 'O'){
                nHumanPoints++;
                vWinningMovesHuman.add(new CFPair(nRow, nCol));

            } else if (m_cGrid [nRow][nCol] == 'X'){
                nComputerPoints++;
                vWinningMovesCPU.add(new CFPair(nRow,nCol));

            }
            nRow += nDeltaY; // Row is vertical
            nCol += nDeltaX;
        }

        // now see if there is any winnning moves

        if(nHumanPoints == 4 )
        {
            m_winningMoves = vWinningMovesHuman;
            nRet = SCORE;

        }
        else if (nComputerPoints == 4)
        {
            m_winningMoves = vWinningMovesCPU;
            nRet = -SCORE;
        } else {
            // nobody wins yet
            nRet = nComputerPoints;
        }
        return nComputerPoints;


    }
    /*
    @return overall score for the board
     */
    int score ()
    {
        int nPoints = 0;
        int nVerticalPoints = 0;
        int nHorizontalPoints = 0;
        int nDiagonalPoints1 = 0;
        int nDiagonalPoints2 = 0;

// |
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 7; j++) {
                int nScore = scorePosition(i, j, 1, 0);
                // somehow has an end
                if (Math.abs(nScore) == SCORE) return nScore;
                else nVerticalPoints += nScore;

            }
        }
// -
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                int nScore = scorePosition(i, j, 0, 1);
                // somehow has an end
                if (Math.abs(nScore) == SCORE) return nScore;
                else nHorizontalPoints += nScore;

            }
        }

//  \
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                int nScore = scorePosition(i, j, 1, 1);
                // somehow has an end
                if (Math.abs(nScore) == SCORE) return nScore;
                else nDiagonalPoints1 += nScore;

            }
        }

//  /
        for (int i = 3; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                int nScore = scorePosition(i, j, -1, 1);
                // somehow has an end
                if (Math.abs(nScore) == SCORE) return nScore;
                else nDiagonalPoints2 += nScore;

            }
        }
        nPoints = nHorizontalPoints + nVerticalPoints + nDiagonalPoints1 + nDiagonalPoints2;
        return nPoints;
    }


    ConnectFour copy ()
    {
        char [][] cGridCopy = m_cGrid.clone();
        return new ConnectFour(m_bBlue, cGridCopy);

    }

    boolean isFinished (int nDepth, int nScore)
    {
        boolean bFinished = false;
        if (nDepth == 0 || Math.abs(nScore) == SCORE ||isFull())
            bFinished = true;
        return bFinished;
    }

    static ColumnScorePair maximizePlay (ConnectFour cf, int nDepth)
    {
        int nScore = cf.score();
        ColumnScorePair csp = new ColumnScorePair(-1, -99999);



        if (cf.isFinished( nDepth, nScore)) {
            csp = new ColumnScorePair(-1, nScore);
            return csp;
        }

        for (int i = 0; i < 7; i++) {
            ConnectFour newCF = cf.copy();
            if (newCF.drop(i))
            {
                iterations++;
                /// ??? what's up with that, and iterations for debug???
                ColumnScorePair cspNextMove = minimizePlay (newCF, nDepth -1);
                if (csp.col == -1 || cspNextMove.score > csp.score) {
                    csp.col = i;
                    csp.score = cspNextMove.score;
                }
            }

        }
        return csp;

    }
    static int aiMove (ConnectFour cf){
        /// assuming not finished, can I assume so?
        iterations = 0;
        System.out.println("before aiMove");
        cf.print();
        ColumnScorePair csp = maximizePlay(cf, 4);
        System.out.println("after aiMove");
        cf.print();

        return csp.col;
    }

    static ColumnScorePair minimizePlay (ConnectFour cf, int nDepth)
    {
        int nScore = cf.score();
        ColumnScorePair csp = new ColumnScorePair(-1, 99999);


        if (cf.isFinished( nDepth, nScore)) {
            csp = new ColumnScorePair(-1, nScore);
            return csp;
        }

        for (int i = 0; i < 7; i++) {
            ConnectFour newCF = cf.copy();
            if (newCF.drop(i))
            {
                iterations++;
                /// ??? what's up with that, and iterations for debug???
                ColumnScorePair cspNextMove = maximizePlay (newCF, nDepth -1);
                if (csp.col == -1 || cspNextMove.score < csp.score) {
                    csp.col = i;
                    csp.score = cspNextMove.score;
                }
            }

        }
        return csp;

    }


    static void game() {
/*
        Scanner reader = new Scanner(System.in);
        boolean bBlue = true; // blue starts first, red 2nd.
        char cDone = '-';

        ConnectFour cf = new ConnectFour ();
        cf.print ();
        while ((cDone = cf.done()) == '-')
        {
            System.out.println(bBlue?"Blue turn to pick a column:": "Red turn to pick a column:");
            int n = reader.nextInt();

            while (!cf.drop(n, bBlue))
            {
                System.out.println(bBlue?"Blue turn to pick a column:": "Red turn to pick a column:");
                n = reader.nextInt();
            }
            cf.print();
            bBlue = !bBlue;
        }
        switch (cDone)
        {
            case 'O':
                System.out.println("Blue win.");
                break;
            case 'X':
                System.out.println("Red win.");
                break;
            default:
                System.out.println("Tie");
        }
        reader.close();
*/

    }


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ConnectFour.game();

    }
}