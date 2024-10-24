import java.util.function.BiFunction;
import java.util.function.Function;
import tester.*;

//REPRESENTS the vistor interface for arith
interface IArithVisitor<R> {
  
  // REPRESENTS the central apply method
  R apply(IArith arith);
  
  //REPRESENTS the central visit for const
  R visitConst(Const constant);
  
  //REPRESENTS the central visit for unary
  R visitUn(UnaryFormula unFormula);
  
  //REPRESENTS the central visit for binary
  R visitBi(BinaryFormula biFormula);
  
  // using r as like return type
}

//REPRESENTS a type of visitor: evaluates
class EvalVisitor implements IArithVisitor<Double> {
  
  //CONSTRUCTOR
  EvalVisitor() {}
  
  // REPRESENTS visiting general arith / initialize
  public Double apply(IArith arith) {
    return arith.accept(this);
  }
  
  // REPRESENTS visiting const case
  public Double visitConst(Const constant) {
    return constant.num;
  }
  
  // REPRESENTS visiting unary case  
  public Double visitUn(UnaryFormula unFormula) {
    return unFormula.func.apply(unFormula.child.accept(this));
  }
  
  // REPRESENTS visiting binary case  
  public Double visitBi(BinaryFormula biFormula) {
    return biFormula.func.apply(biFormula.left.accept(this), biFormula.right.accept(this));
  }
}

//REPRESENTS a type of visitor: prints
class PrintVisitor implements IArithVisitor<String> {
  
  //CONSTRUCTOR
  PrintVisitor() {}
  
  // REPRESENTS visiting general arith / initialize
  public String apply(IArith arith) {
    return arith.accept(this);
  }
  
  // REPRESENTS visiting const case
  public String visitConst(Const constant) {
    return constant.num.toString();
  }
  
  // REPRESENTS visiting unary case 
  public String visitUn(UnaryFormula unFormula) {
    return "(" + unFormula.name + " " + unFormula.child.accept(this) + ")";
  }
  
  // REPRESENTS visiting binary case  
  public String visitBi(BinaryFormula biFormula) {
    return "(" + biFormula.name + " " 
        + biFormula.left.accept(this)
        + biFormula.left.accept(this) + ")";
  }
}

//REPRESENTS a type of visitor: mirrors tree
class MirrorVisitor implements IArithVisitor<IArith> {
  
  //CONSTRUCTOR
  MirrorVisitor() {}
  
  // REPRESENTS visiting general arith / initialize
  public IArith apply(IArith arith) {
    return arith.accept(this);
  }
  
  // REPRESENTS visiting const case
  public IArith visitConst(Const constant) {
    return constant;
  }
  
  // REPRESENTS visiting unary case 
  public IArith visitUn(UnaryFormula unFormula) {
    // func, name child
    unFormula.child = unFormula.child.accept(this);
    
    return unFormula;
  }
  
  // REPRESENTS visiting binary case  
  public IArith visitBi(BinaryFormula biFormula) {
    
    biFormula.left = biFormula.right.accept(this);
    biFormula.right = biFormula.left.accept(this);
    
    return biFormula;
  }
}

//REPRESENTS a type of visitor: tree all even
class AllEvenVisitor implements IArithVisitor<Boolean> {
  
  //CONSTRUCTOR
  AllEvenVisitor() {}
  
  // REPRESENTS visiting general arith / initialize
  public Boolean apply(IArith arith) {
    return arith.accept(this);
  }
  
  // REPRESENTS visiting const case
  public Boolean visitConst(Const constant) {
    return ((constant.num % 2) == 0);
  }
  
  // REPRESENTS visiting unary case 
  public Boolean visitUn(UnaryFormula unFormula) {
    return unFormula.child.accept(this);
  }
  
  // REPRESENTS visiting binary case 
  public Boolean visitBi(BinaryFormula biFormula) {
    return biFormula.left.accept(this) && biFormula.right.accept(this);
  }
}

// REPRESENTS interface for general arith (const, unary, binary)
interface IArith {
  //IArithVisitor<R> accept();
  
  // call accept on an IArith, kind of confirms it's an IArith
  
  // REPRESENTS central accept of visitor
  <R> R accept(IArithVisitor<R> vistor);
}


//REPRESENTS a constant with a set value
class Const implements IArith {
  Double num;
  
  Const(Double num) {
    this.num = num;
  }
  
  //REPRESENTS local accept of visitor
  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitConst(this);
  }
}

//REPRESENTS a unary formula applied to the child
class UnaryFormula implements IArith {
  Function<Double, Double> func;
  String name;
  IArith child;
  
  //CONSTRUCTOR
  UnaryFormula(Function<Double, Double> func, String name, IArith child) {
    this.func = func;
    this.name = name;
    this.child = child;
  }
  
  //REPRESENTS local accept of visitor
  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitUn(this);
  }
}

//REPRESENTS a binary formula applied to left and right
class BinaryFormula implements IArith {
  BiFunction<Double, Double, Double> func;
  String name;
  IArith left;
  IArith right;
  
  //CONSTRUCTOR
  BinaryFormula(BiFunction<Double, Double, Double>  func, String name, IArith left, IArith right) {
    this.func = func;
    this.name = name;
    this.left = left;
    this.right = right;
  }
  
  //REPRESENTS local accept of visitor
  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitBi(this);
  }
}

/// UNARY FUNCTION INTERFACE ///

//REPRESENTS a function that negates a double
class Neg implements Function<Double, Double> {

  @Override
  // REPRESENTS
  public Double apply(Double t) {
    return -t;
  }
}

//REPRESENTS a function that squares a double
class Sqr implements Function<Double, Double> {

  @Override
  //REPRESENTS
  public Double apply(Double t) {
  return t * t;
  }
}

/// BINARY FUNCTION INTERFACE ///

//REPRESENTS a function that pluses two doubles
class Plus implements BiFunction<Double, Double, Double> {
  
  @Override
  // REPRESENTS
  public Double apply(Double t, Double u) {
    return t + u;
  }
}
 
//REPRESENTS a function that minuses two doubles
class Minus implements BiFunction<Double, Double, Double> {

  @Override
  // REPRESENTS
  public Double apply(Double t, Double u) {
    return t - u;
  }
}

//REPRESENTS a function that multiplies two doubles
class Mul implements BiFunction<Double, Double, Double> {

  @Override
  // REPRESENTS
  public Double apply(Double t, Double u) {
    return t * u;
  }
}

//REPRESENTS a function that divides two doubles
class Div implements BiFunction<Double, Double, Double> {

  @Override
  // REPRESENTS
  public Double apply(Double t, Double u) {
    return t / u;
  }
}

// REPRESENTS all examples and tests for Visitors.java
class ExamplesVisitors {
  
  // create test examples for all types of visitors
  EvalVisitor ev = new EvalVisitor();
  PrintVisitor pv = new PrintVisitor();
  MirrorVisitor mv = new MirrorVisitor();
  AllEvenVisitor aev = new AllEvenVisitor();
  
  // const examples
  Const zero = new Const(0.0);
  Const posNum = new Const(100.0);
  Const even = new Const(2.0);
  Const negNum = new Const(-5.0);
  Const odd = new Const(1.0);
  
  // Function test examples
  Function<Double, Double> neg = new Neg();
  Function<Double, Double> sqr = new Sqr();
  BiFunction<Double, Double, Double> plus = new Plus();
  BiFunction<Double, Double, Double> minus = new Minus();
  BiFunction<Double, Double, Double> mul = new Mul();
  BiFunction<Double, Double, Double> div = new Div();
  
  // unary examples
  
  // unary examples with const children
  UnaryFormula uf0 = new UnaryFormula(this.neg, "neg", zero);
  UnaryFormula ufPos = new UnaryFormula(this.neg, "neg", posNum);
  UnaryFormula ufNeg = new UnaryFormula(this.neg, "neg", negNum);
  
  // unary examples with unary children
  UnaryFormula unun = new UnaryFormula(this.neg, "neg", zero);
  
  // unary examples with binary children
  UnaryFormula unbi = new UnaryFormula(this.neg, "neg", zero);
  
  // binary examples
  
  // binary examples with const children
  UnaryFormula bi0 = new UnaryFormula(this.neg, "neg", zero);
  UnaryFormula biPos = new UnaryFormula(this.neg, "neg", posNum);
  UnaryFormula biNeg = new UnaryFormula(this.neg, "neg", negNum);
  
  // binary examples with unary children
  UnaryFormula biun = new UnaryFormula(this.neg, "neg", zero);
  
  // binary examples with binary children
  UnaryFormula bibi = new UnaryFormula(this.neg, "neg", zero);
  
  // tests the apply method using the 4 different visitor types
  boolean testApply(Tester t) {
    
    // tests the eval visitor type, negative, positive, and 
    return t.checkExpect(this.ev.apply(this.zero), 0.0)
        && t.checkExpect(this.ev.apply(this.posNum), 100.0)
        && t.checkExpect(this.ev.apply(this.even), 2.0)
        && t.checkExpect(this.ev.apply(this.negNum), -5.0)
        && t.checkExpect(this.ev.apply(this.odd), 1.0)
        
        && t.checkExpect(this.pv.apply(this.zero), "0.0")
        && t.checkExpect(this.pv.apply(this.posNum), "100.0")
        && t.checkExpect(this.pv.apply(this.even), "2.0")
        && t.checkExpect(this.pv.apply(this.negNum), "-5.0")
        && t.checkExpect(this.pv.apply(this.odd), "1.0")
        
        && t.checkExpect(this.mv.apply(this.zero), this.zero)
        && t.checkExpect(this.mv.apply(this.posNum), this.posNum)
        && t.checkExpect(this.mv.apply(this.even), this.even)
        && t.checkExpect(this.mv.apply(this.negNum), this.negNum)
        && t.checkExpect(this.mv.apply(this.odd), this.odd)
        
        && t.checkExpect(this.aev.apply(this.zero), true)
        && t.checkExpect(this.aev.apply(this.posNum), true)
        && t.checkExpect(this.aev.apply(this.even), true)
        && t.checkExpect(this.aev.apply(this.negNum), false)
        && t.checkExpect(this.aev.apply(this.odd), false);
  }
  
  // REPRESENTS testing the visit of a const
  boolean testVisitConst(Tester t) {
    return true;
  }
  
  // REPRESENTS testing the visit of a unary
  boolean testVisitUn(Tester t) {
    return true;
  }
  
  // REPRESENTS testing the visit of a binary
  boolean testVisitBi(Tester t) {
    return true;
  }
  
  Function<Double, Double> negF = new Neg();
  Function<Double, Double> sqrF = new Sqr();
  
  BiFunction<Double, Double, Double> plusF = new Plus();
  BiFunction<Double, Double, Double> minusF = new Minus();
  BiFunction<Double, Double, Double> mulF = new Mul();
  BiFunction<Double, Double, Double> divF = new Div();
  
  // REPRESENTS testing the neg function
  boolean testNeg(Tester t) {
    // Negate by 0
    return t.checkExpect(this.negF.apply(0.0), 0.0)
    // Negate positive
    && t.checkExpect(this.negF.apply(2.0), -2.0)
    // Negate negative
    && t.checkExpect(this.negF.apply(-3.0), 3.0)
    // Negate positive
    && t.checkExpect(this.negF.apply(2.5), -2.5)
    // Negate negative
    && t.checkExpect(this.negF.apply(-6.25), 6.25);
  }
  
  // REPRESENTS testing the sqr function
  boolean testSqr(Tester t) {
    // Square by 0
    return t.checkExpect(this.sqrF.apply(0.0), 0.0)
    // Square positive
    && t.checkExpect(this.sqrF.apply(2.0), 4.0)
    // Square negative
    && t.checkExpect(this.sqrF.apply(-3.0), 9.0)
    // Square positive
    && t.checkExpect(this.sqrF.apply(2.5), 6.25)
    // Square negative
    && t.checkExpect(this.sqrF.apply(-6.25), 39.0625);
  }
  
  // REPRESENTS testing the plus biFunction
  boolean testPlus(Tester t) {
    // Plus by 0
    return t.checkExpect(this.plusF.apply(0.0, -3.0), -3.0)
    // Plus positive by negative
    && t.checkExpect(this.plusF.apply(2.0, -1.0), 1.0)
    // Plus negative by positive
    && t.checkExpect(this.plusF.apply(-3.0, 6.0), 3.0)
    // Plus positive by positive
    && t.checkExpect(this.plusF.apply(2.5, 5.0), 7.5)
    // Plus negative by negative
    && t.checkExpect(this.plusF.apply(-6.25, -3.125), -9.375);
  }
  
  // REPRESENTS testing the minus biFunction
  boolean testMinus(Tester t) {
    // Minus from 0
    return t.checkExpect(this.minusF.apply(0.0, -3.0), 3.0)
    // Minus by 0
    && t.checkExpect(this.minusF.apply(2.0, 0.0), 2.0)
    // Minus positive by negative
    && t.checkExpect(this.minusF.apply(2.0, -1.0), 3.0)
    // Minus negative by positive
    && t.checkExpect(this.minusF.apply(-3.0, 6.0), -9.0)
    // Minus positive by positive
    && t.checkExpect(this.minusF.apply(2.5, 5.0), -2.5)
    // Minus negative by negative
    && t.checkExpect(this.minusF.apply(-6.25, -3.125), -3.125);
  }
  
  // REPRESENTS testing the multiply biFunction
  boolean testMul(Tester t) {
    // Multiply by 0
    return t.checkExpect(this.mulF.apply(0.0, -3.0), 0.0)
    // Multiply positive by negative
    && t.checkExpect(this.mulF.apply(2.0, -1.0), -2.0)
    // Multiply negative by positive
    && t.checkExpect(this.mulF.apply(-3.0, 6.0), -18.0)
    // Multiply positive by positive
    && t.checkExpect(this.mulF.apply(2.5, 5.0), 12.5)
    // Multiply negative by negative
    && t.checkExpect(this.mulF.apply(-6.25, -3.125), 19.53125);
  }
  
  // REPRESENTS testing the divide biFunction
  boolean testDiv(Tester t) {
    // Divide by 0
    return t.checkExpect(
        this.divF.apply(1.0, 0.0).equals(
            Double.POSITIVE_INFINITY), true)
    // Divide positive by negative
    && t.checkExpect(this.divF.apply(2.0, -1.0), -2.0)
    // Divide negative by positive
    && t.checkExpect(this.divF.apply(-3.0, 6.0), -0.5)
    // Divide positive by positive
    && t.checkExpect(this.divF.apply(2.5, 5.0), 0.5)
    // Divide negative by negative
    && t.checkExpect(this.divF.apply(-6.25, -3.125), 2.0);
  }
}
