import java.util.*;
import javalib.impworld.*;
import tester.*;
import javalib.worldimages.*;
import java.awt.Color;

class Cell {
  
  // type (p1, p2, or unowned)
  // p1 is pink, p2 is magenta
  Color color;
  
  // neighbors
  Cell left;
  Cell right;
  Cell top;
  Cell bottom;
  
  // construct a cell
  Cell(Color color) {
    this.color = color;
  }

  // draws the cell
  WorldImage draw(int size) {
    return new RectangleImage(size, size, OutlineMode.SOLID, this.color);
  }
  
  // clicks the cell
  // if p1 is true, then it is pink's turn
  boolean click(boolean p1turn) {
    if (this.color == Color.white) {
      if (p1turn) {
        this.color = Color.pink;
        return !p1turn;
      } else {
        this.color = Color.magenta;
        return !p1turn;
      }
    }
    return p1turn;
  }
  
  // player 1 win condition
  // 0 = move right, 1 = move top, 2 = move bottom
  // accumulator that counts the amount of right movements, needs to hit gridSize - 2
  boolean winPink(int rightCount, int lastMove) {
    if (!this.color.equals(Color.pink)) {
      return false;
    }
    
    if (rightCount == 0) {
      return true; // win
    } 
    
    boolean right = (this.right.winPink(rightCount - 1, 0));
    boolean top = false;
    boolean bottom = false;
    
    if (lastMove == 1) {
      top = this.top.winPink(rightCount, 1);
    }
    else if (lastMove == 2) {
      bottom = this.bottom.winPink(rightCount, 2);
    }
    else {
      top = this.top.winPink(rightCount, 1);
      bottom = this.bottom.winPink(rightCount, 2);
    }
     
    return right || top || bottom;
  }
  
  // player 2 win condition
  // 0 = move bottom, 1 = move left, 2 = move right
  // accumulator that counts the amount of right movements, needs to hit gridSize - 2
  boolean winMagenta(int downCount, int lastMove) {
    if (!this.color.equals(Color.magenta)) {
      return false;
    }
    
    if (downCount == 0) {
      return true; // win
    } 
    
    boolean bottom = (this.bottom.winMagenta(downCount - 1, 0));
    boolean left = false;
    boolean right = false;
    
    if (lastMove == 1) {
      left = this.left.winMagenta(downCount, 1);
    }
    else if (lastMove == 2) {
      right = this.right.winMagenta(downCount, 2);
    }
    else {
      left = this.left.winMagenta(downCount, 1);
      right = this.right.winMagenta(downCount, 2);
    }
     
    return bottom || left || right;
  }
  
  // initializes the cell's surrounding cells
  // EFFECT: Changes the top, right, down, left to node's neighbors
  void initSurrounding(int row, int col, ArrayList<ArrayList<Cell>> board) {
    
    // left
    if (col > 0) {
      this.left = board.get(row).get(col - 1);
    } else {
      this.left = null;
    }
    
    // right
    if (col < board.size() - 1) {
      this.right = board.get(row).get(col + 1);
    } else {
      this.right = null;
    }
    
    // top
    if (row > 0) {
      this.top = board.get(row - 1).get(col);
    } else {
      this.top = null;
    }
    
    // bottom
    if (row < board.size() - 1) {
      this.bottom = board.get(row + 1).get(col);
    } else {
      this.bottom = null;
    }
  }
  
}

// the game class
class BridgIt extends World {
  
  // world state
  ArrayList<ArrayList<Cell>> board; // grid of cells
  boolean p1turn; // player 1 is pink, player 2 is magenta
  
  // game details
  int gridSize; // n * n grid
  int cellSize = 30; // cell size

  // constructor for the grid, throws certain exceptions
  BridgIt(int gridSize) {
    if (gridSize < 3 || gridSize % 2 == 0) {
      throw new IllegalArgumentException("Grid size must be an odd number greater than 2.");
    }
    this.gridSize = gridSize;
    this.p1turn = true;
    this.initBoard();
  }
  
  // constructor for the tests
  BridgIt() { }

  // initializes the board with cells and links neighbors
  // EFFECT: Adds cells to board array of array to create board
  void initBoard() {
    this.board = new ArrayList<>();

    // create cells
    for (int row = 1; row <= this.gridSize; row++) {
      ArrayList<Cell> rowList = new ArrayList<>();
      for (int col = 1; col <= this.gridSize; col++) {
        if (col % 2 == 0 && row % 2 == 1) {
          rowList.add(new Cell(Color.magenta)); // pink cells for odd columns and even rows
        } else if (row % 2 == 0 && col % 2 == 1) {
          rowList.add(new Cell(Color.pink)); // magenta cells for even columns and odd rows
        } else {
          rowList.add(new Cell(Color.WHITE)); // white cells for the rest
        }
      }
      this.board.add(rowList);
    }

    // link neighbors using initSurrounding method
    for (int row = 0; row < this.gridSize; row++) {
      for (int col = 0; col < this.gridSize; col++) {
        Cell current = this.board.get(row).get(col);
        current.initSurrounding(row, col, this.board);
      }
    }
  }

  // Draws the entire game board
  @Override
  public WorldScene makeScene() {
    WorldScene scene = this.getEmptyScene();
    for (int row = 0; row < this.gridSize; row++) {
      for (int col = 0; col < this.gridSize; col++) {
        Cell cell = this.board.get(row).get(col);
        int x = col * this.cellSize + this.cellSize / 2;
        int y = row * this.cellSize + this.cellSize / 2;
        scene.placeImageXY(cell.draw(this.cellSize), x, y);
      }
    }
    return scene;
  }
  
  // handles mouse click
  // EFFECT: Ends world if win, sets player's turn to area clicked if it was valid
  public void onMouseClicked(Posn pos) {
    
    // calculate which square was clicked
    int row = pos.y / this.cellSize;
    int col = pos.x / this.cellSize;
    if (!(row == 0) && !(col == 0) && !(row == this.gridSize - 1) && !(col == gridSize - 1)) {
      Cell clicked = this.board.get(row).get(col);
      this.p1turn = clicked.click(this.p1turn);
      int win = this.win();
      if (win == 1) {
        this.endOfWorld("Player one has won!");
      } else if (win == 2) {
        this.endOfWorld("Player two has won!");
      }
    }
    
  }
  
  // checks both magenta and pink path if there is a win
  // 0 = no win, 1 = pink win, 2 = magenta win
  public int win() {
    Cell c;
    
    // pink
    for (int row = 1; row < this.gridSize; row += 2) {
      c = this.board.get(row).get(1);
      if (c.winPink(this.gridSize - 2, 0)) {
        return 1;
      }
    }
    
    // magenta
    for (int col = 1; col < this.gridSize; col += 2) {
      c = this.board.get(1).get(col);
      if (c.winMagenta(this.gridSize - 2, 0)) {
        return 2;
      }
    }
    
    return 0;
  }
  
  // ending screen, win or loss
  @Override
  public WorldScene lastScene(String message) {
    WorldScene last = this.makeScene();
    Color msgColor = Color.black;
    last.placeImageXY(
        new TextImage(message, this.gridSize * this.cellSize / 12, msgColor), 
        (this.gridSize * this.cellSize) / 2,
        (this.gridSize * this.cellSize) / 2);
    return last;
  }
  
}

// class for testing all functionality of BridgIt
class ExamplesBridgIts {
  
  // test the game
  void testGame(Tester t) {
    
    // n * n
    int n = 11;
    int size = 30;
    
    // world size
    int len = n * size;
    int wid = n * size;
    
    BridgIt b = new BridgIt(n);
    b.bigBang(len, wid);
  }
  
  // test draw for the cell
  void testDraw(Tester t) {
    
    // make an example cell
    Cell aCell = new Cell(Color.pink);
    
    // give it size 30
    int size = 30;
    
    // draw it
    t.checkExpect(aCell.draw(size), 
        new RectangleImage(30, 30, OutlineMode.SOLID, Color.pink));
    
    // make the size larger
    size = 100;
    
    // draw again
    t.checkExpect(aCell.draw(size), 
        new RectangleImage(100, 100, OutlineMode.SOLID, Color.pink));
    
  }
  
  // test initializing surroundings of nodes
  void testInitSurrounding(Tester t) {
    
    // create a small board
    ArrayList<Cell> row1 = new ArrayList<Cell>(Arrays.asList(new Cell(Color.white), 
        new Cell(Color.magenta), new Cell(Color.white)));
    ArrayList<Cell> row2 = new ArrayList<Cell>(Arrays.asList(new Cell(Color.pink), 
        new Cell(Color.white), new Cell(Color.pink)));
    ArrayList<Cell> row3 = new ArrayList<Cell>(Arrays.asList(new Cell(Color.white), 
        new Cell(Color.magenta), new Cell(Color.white)));
    ArrayList<ArrayList<Cell>> board = new ArrayList<ArrayList<Cell>>(Arrays.asList(row1, 
        row2, row3));
    
    // use initSurrounding on all of the cells
    for (int row = 0; row < board.size(); row++) {
      for (int col = 0; col < board.size(); col++) {
        board.get(row).get(col).initSurrounding(row, col, board);
      }
    }
    
    // check the links (36 tests)
    for (int row = 0; row < board.size(); row++) {
      for (int col = 0; col < board.size(); col++) {
        
        // check top border
        if (row > 0) {
          t.checkExpect(board.get(row).get(col).top, board.get(row - 1).get(col));
        } else {
          t.checkExpect(board.get(row).get(col).top, null);
        }
        
        // check bottom border
        if (row < board.size() - 1) {
          t.checkExpect(board.get(row).get(col).bottom, board.get(row + 1).get(col));
        } else {
          t.checkExpect(board.get(row).get(col).bottom, null);
        }
        
        // check left border
        if (col > 0) {
          t.checkExpect(board.get(row).get(col).left, board.get(row).get(col - 1));
        } else {
          t.checkExpect(board.get(row).get(col).left, null);
        }
        
        // check right border
        if (col < board.size() - 1) {
          t.checkExpect(board.get(row).get(col).right, board.get(row).get(col + 1));
        } else {
          t.checkExpect(board.get(row).get(col).right, null);
        }
        
      }
    }
  }
  
  // test BridgIt constructor
  void testBridgIt(Tester t) {
    
    String constructorException = "Grid size must be an odd number greater than 2.";
    
    // under 3 test
    t.checkConstructorException(new IllegalArgumentException(constructorException), 
        "BridgIt", 1);
    
    // even test
    t.checkConstructorException(new IllegalArgumentException(constructorException), 
        "BridgIt", 10);
  }
  
  // tests initializing the board
  void testInitBoard(Tester t) {
    
    // create a bridgIt without using init instantly
    BridgIt b = new BridgIt();
    
    // use fields of fields to initialize gridSize
    b.gridSize = 3;
    
    // right now BridgIt's board should be null, since it isn't initialized
    t.checkExpect(b.board, null);
    
    // initialize the board using initboard
    b.initBoard();
    
    // make an expected copy of the board
    ArrayList<Cell> row1 = new ArrayList<Cell>(Arrays.asList(new Cell(Color.white), 
        new Cell(Color.magenta), new Cell(Color.white)));
    ArrayList<Cell> row2 = new ArrayList<Cell>(Arrays.asList(new Cell(Color.pink), 
        new Cell(Color.white), new Cell(Color.pink)));
    ArrayList<Cell> row3 = new ArrayList<Cell>(Arrays.asList(new Cell(Color.white), 
        new Cell(Color.magenta), new Cell(Color.white)));
    ArrayList<ArrayList<Cell>> board = new ArrayList<ArrayList<Cell>>(Arrays.asList(row1, 
        row2, row3));
    
    // link the cells
    for (int row = 0; row < board.size(); row++) {
      for (int col = 0; col < board.size(); col++) {
        board.get(row).get(col).initSurrounding(row, col, board);
      }
    }
    
    // check if the expect copy matches the one we just initialized
    t.checkExpect(b.board, board);
  }
  
  // tests the MakeScene method
  void testMakeScene(Tester t) {
    
    // create a bridgIt of size 3
    BridgIt b = new BridgIt(3);
    
    // create the expected scene
    WorldScene scene = b.getEmptyScene();
    for (int row = 0; row < b.board.size(); row++) {
      for (int col = 0; col < b.board.size(); col++) {
        Cell cell = b.board.get(row).get(col);
        int x = col * b.cellSize + b.cellSize / 2;
        int y = row * b.cellSize + b.cellSize / 2;
        scene.placeImageXY(cell.draw(b.cellSize), x, y);
      }
    }
    
    // test if the scene matches the expected scene
    t.checkExpect(b.makeScene(), scene);
  }
  
  // test the click in cell
  void testClick(Tester t) {
     Cell cell = new Cell(Color.white);
  
     // test clicking an unplaced cell (player 1)
     boolean p1turn = true; // Player 1's turn
     t.checkExpect(cell.click(p1turn), false); // Should return the opposite turn
     t.checkExpect(cell.color, Color.pink); // Cell should now be pink
  
     //test clicking an already unplaced cell (player 1)
     cell = new Cell(Color.white);
     p1turn = false;
     t.checkExpect(cell.click(p1turn), true);
     t.checkExpect(cell.color, Color.magenta);
  
     // test clicking an already placed cell (player 1)
     cell = new Cell(Color.pink);
     p1turn = true;
     t.checkExpect(cell.click(p1turn), true);
     t.checkExpect(cell.color, Color.pink);
  
     // test clicking an already placed cell (player 2)
     cell = new Cell(Color.magenta);
     p1turn = false;
     t.checkExpect(cell.click(p1turn), false);
     t.checkExpect(cell.color, Color.magenta);
  
     // test where the color is invalid idk (not white, pink, or magenta)
     cell = new Cell(Color.red); // Invalid color
     p1turn = true;
     t.checkExpect(cell.click(p1turn), true);
     t.checkExpect(cell.color, Color.red);
  }

  // test the pink's epic win
  void testWinPink(Tester t) {
     BridgIt b = new BridgIt(5); // 5x5 grid
  
     // initially no pink win
     t.checkExpect(b.board.get(1).get(0).winPink(b.gridSize - 2, 0), false);
  
     // player 1 (pink) win
     b.board.get(1).get(1).color = Color.pink;
     b.board.get(1).get(3).color = Color.pink;
  
     // win pink
     t.checkExpect(b.board.get(1).get(0).winPink(b.gridSize - 2, 0), true);
  
     // gets blocked
     b.initBoard();
     b.board.get(1).get(1).color = Color.pink;
     b.board.get(1).get(3).color = Color.magenta; // Interrupt the path
  
     // so no win
     t.checkExpect(b.board.get(1).get(0).winPink(b.gridSize - 2, 0), false);
  }

  
  // test the magenta's epic win
  void testWinMagenta(Tester t) {
     BridgIt b = new BridgIt(5); // 5x5 grid
  
     // initially no magenta win
     t.checkExpect(b.board.get(0).get(1).winMagenta(b.gridSize - 2, 0), false);
  
     // player 1 (magenta) win
     b.board.get(1).get(1).color = Color.magenta;
     b.board.get(3).get(1).color = Color.magenta;
  
     // win magenta
     t.checkExpect(b.board.get(0).get(1).winMagenta(b.gridSize - 2, 0), true);
  
     // gets blocked
     b.initBoard();
     b.board.get(1).get(1).color = Color.magenta;
     b.board.get(3).get(1).color = Color.pink; // Interrupt the path
  
     // so no win
     t.checkExpect(b.board.get(0).get(1).winMagenta(b.gridSize - 2, 0), false);
  }

  // test when player inputs mouse click
  void testOnMouseClick(Tester t) {
    
  }
  
  // test the epic win
  void testWin(Tester t) {
     BridgIt b = new BridgIt(5); // 5x5 grid
  
     // no winner
     t.checkExpect(b.win(), 0);
  
     // player 1 (pink)
     b.board.get(1).get(1).color = Color.pink;
     b.board.get(1).get(3).color = Color.pink;
  
     // player 1 now win
     t.checkExpect(b.win(), 1);
  
     // reset board
     b.initBoard();
  
     // player 2 (magenta)
     b.board.get(1).get(1).color = Color.magenta;
     b.board.get(3).get(1).color = Color.magenta;
  
     // player 2 now win
     t.checkExpect(b.win(), 2);
  
     // now no one win
     b.initBoard();
     t.checkExpect(b.win(), 0);
  }
  
  // tests the last scene
  void testLastScene(Tester t) {
     BridgIt b = new BridgIt(3);
  
     String message = "Player one has won!";
     Color msgColor = Color.black;
  
     WorldScene expectedScene = b.getEmptyScene();
     expectedScene.placeImageXY(
         new TextImage(message, b.gridSize * b.cellSize / 12, msgColor),
         (b.gridSize * b.cellSize) / 2,
         (b.gridSize * b.cellSize) / 2
     );
  
     WorldScene actualScene = b.lastScene(message);
  
     t.checkExpect(actualScene, expectedScene);
  }
}
