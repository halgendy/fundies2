import java.util.*;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;


class Tile {
  Color color;
  
  // Tile can be null if on border
  Tile north;
  Tile east;
  Tile south;
  Tile west;
  // if title has null east or west
  // start moving sideways to check if goes to border
  Color noColor = Color.WHITE;
  Color p1Color = Color.PINK;
  Color p2Color = Color.MAGENTA;
  
  // CONSTRUCTOR
  Tile(Color color, int horizontal, int vertical, boolean filled) {
    this.color = color;
    
    // Build the tiles left of this one (if it needs a neighbor
    if (horizontal > 0) {
      this.east = new Tile(color);
    } else {
      this.east = null;
    }
    
    // Build the tiles below this one
    if (vertical > 0) {
      this.south = new Tile(color);
    } else {
      this.south = null;
    }
    
    if (color.equals(p1Color)) {
      this.south = new Tile(noColor, horizontal, vertical - 1);
      this.east = new Tile(noColor,  horizontal - 1, vertical);
    }
    else if (color.equals(p2Color)) {
      this.east = new Tile(noColor, horizontal, vertical);
    }
    else if (color.equals(noColor)) {
      this.south = new Tile(p1Color, horizontal, vertical - 1);
      this.east = new Tile(p2Color, horizontal - 1, vertical);
    }
  }
  
  Boolean FoundTwoEdges(Tile edge1, Tile edge2) {
    // if it's an edge node
    if (this.north == null || this.east == null || this.south == null || this.west == null) {
      return null;
    }
    return false;
  }
}

class BridgIt {
  
  Tile rootTile = new Tile(Color.WHITE, 10, 10);
  
  // CONSTRUCTOR
  BridgIt() {
    
  }
  
  // draw
  WorldImage Draw() {
    // draw the first square / root square with position 0,0
    // overlay next with prev position + sidelength right
    // overlay next with prev position + sidelength down
    // repeat
    return null;
  }
}


//
class ExamplesBridgIt {
  
}
