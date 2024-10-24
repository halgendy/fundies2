import java.util.function.BiFunction;
import java.util.function.Function;
import tester.*;

//REPRESENTS
interface IArithVisitor<R> {
  
  // REPRESENTS
  R apply(IArith arith);
  
  //REPRESENTS
  R visitConst(Const constant);
  
  //REPRESENTS
  R visitUn(UnaryFormula unFormula);
  
  //REPRESENTS
  R visitBi(BinaryFormula biFormula);
  
  // using r as like return type
}

//REPRESENTS
class EvalVisitor implements IArithVisitor<Double> {
  
  //CONSTRUCTOR
  EvalVisitor() {}
  
  // REPRESENTS
  public Double apply(IArith arith) {
    return arith.accept(this);
  }
  
  // REPRESENTS
  public Double visitConst(Const constant) {
    return constant.num;
  }
  
  // REPRESENTS  
  public Double visitUn(UnaryFormula unFormula) {
    return unFormula.func.apply(unFormula.child.accept(this));
  }
  
  // REPRESENTS 
  public Double visitBi(BinaryFormula biFormula) {
    return biFormula.func.apply(biFormula.left.accept(this), biFormula.right.accept(this));
  }
}

//REPRESENTS
class PrintVisitor implements IArithVisitor<String> {
  
  //CONSTRUCTOR
  PrintVisitor() {}
  
  // REPRESENTS
  public String apply(IArith arith) {
    return arith.accept(this);
  }
  
  // REPRESENTS
  public String visitConst(Const constant) {
    return constant.num.toString();
  }
  
  // REPRESENTS  
  public String visitUn(UnaryFormula unFormula) {
    return "(" + unFormula.name + " " + unFormula.child.accept(this) + ")";
  }
  
  // REPRESENTS 
  public String visitBi(BinaryFormula biFormula) {
    return "(" + biFormula.name + " " 
        + biFormula.left.accept(this)
        + biFormula.left.accept(this) + ")";
  }
}

//REPRESENTS
class MirrorVisitor implements IArithVisitor<IArith> {
  
  //CONSTRUCTOR
  MirrorVisitor() {}
  
  // REPRESENTS
  public IArith apply(IArith arith) {
    return arith.accept(this);
  }
  
  // REPRESENTS
  public IArith visitConst(Const constant) {
    return constant;
  }
  
  // REPRESENTS  
  public IArith visitUn(UnaryFormula unFormula) {
    // func, name child
    unFormula.child = unFormula.child.accept(this);
    
    return unFormula;
  }
  
  // REPRESENTS 
  public IArith visitBi(BinaryFormula biFormula) {
    
    biFormula.left = biFormula.right.accept(this);
    biFormula.right = biFormula.left.accept(this);
    
    return biFormula;
  }
}

//REPRESENTS
class AllEvenVisitor implements IArithVisitor<Boolean> {
  
  //CONSTRUCTOR
  AllEvenVisitor() {}
  
  // REPRESENTS
  public Boolean apply(IArith arith) {
    return arith.accept(this);
  }
  
  // REPRESENTS
  public Boolean visitConst(Const constant) {
    return ((constant.num % 2) == 0);
  }
  
  // REPRESENTS  
  public Boolean visitUn(UnaryFormula unFormula) {
    return unFormula.child.accept(this);
  }
  
  // REPRESENTS 
  public Boolean visitBi(BinaryFormula biFormula) {
    return biFormula.left.accept(this) && biFormula.right.accept(this);
  }
}

// REPRESENTS
interface IArith {
  //IArithVisitor<R> accept();
  
  // call accept on an IArith, kind of confirms it's an IArith
  
  // REPRESENTS
  <R> R accept(IArithVisitor<R> vistor);
}


//REPRESENTS
class Const implements IArith {
  Double num;
  
  Const(Double num) {
    this.num = num;
  }
  
  //REPRESENTS
  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitConst(this);
  }
}

//REPRESENTS
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
  
  //REPRESENTS
  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitUn(this);
  }
}

//REPRESENTS
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
  
  //REPRESENTS
  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitBi(this);
  }
}

/// UNARY FUNCTION INTERFACE ///

//REPRESENTS
class Neg implements Function<Double, Double> {

  @Override
  // REPRESENTS
  public Double apply(Double t) {
    return -t;
  }
}

//REPRESENTS
class Sqr implements Function<Double, Double> {

  @Override
  //REPRESENTS
  public Double apply(Double t) {
  return t * t;
  }
}

/// BINARY FUNCTION INTERFACE ///

//REPRESENTS
class Plus implements BiFunction<Double, Double, Double> {
  
  @Override
  // REPRESENTS
  public Double apply(Double t, Double u) {
    return t + u;
  }
}
 
//REPRESENTS
class Minus implements BiFunction<Double, Double, Double> {

  @Override
  // REPRESENTS
  public Double apply(Double t, Double u) {
    return t - u;
  }
}

//REPRESENTS
class Mul implements BiFunction<Double, Double, Double> {

  @Override
  // REPRESENTS
  public Double apply(Double t, Double u) {
    return t * u;
  }
}

//REPRESENTS
class Div implements BiFunction<Double, Double, Double> {

  @Override
  // REPRESENTS
  public Double apply(Double t, Double u) {
    return t / u;
  }
}

//
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
  
  //
  boolean testVisitConst(Tester t) {
    return true;
  }
  
  //
  boolean testVisitUn(Tester t) {
    return true;
  }
  
  //
  boolean testVisitBi(Tester t) {
    return true;
  }
  
  //
  boolean testNeg(Tester t) {
    return true;
  }
  
  //
  boolean testSqr(Tester t) {
    return true;
  }
  
  //
  boolean testPlus(Tester t) {
    return true;
  }
  
  //
  boolean testMinus(Tester t) {
    return true;
  }
  
  //
  boolean testMul(Tester t) {
    return true;
  }
  
  //
  boolean testDiv(Tester t) {
    return true;
  }
}
