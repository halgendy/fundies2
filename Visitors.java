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
        + biFormula.left.accept(this) + " "
        + biFormula.right.accept(this) + ")";
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
    
    return new UnaryFormula(unFormula.func, unFormula.name,unFormula.child.accept(this));
  }
  
  // REPRESENTS 
  public IArith visitBi(BinaryFormula biFormula) {
    
    return new BinaryFormula(biFormula.func, biFormula.name, 
        biFormula.right.accept(this), biFormula.left.accept(this));
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
  Const odd = new Const(3.0);
  
  // Function test examples
  Function<Double, Double> neg = new Neg();
  Function<Double, Double> sqr = new Sqr();
  BiFunction<Double, Double, Double> plus = new Plus();
  BiFunction<Double, Double, Double> minus = new Minus();
  BiFunction<Double, Double, Double> mul = new Mul();
  BiFunction<Double, Double, Double> div = new Div();
  
  // unary examples
  
  // unary examples with const children, neg function, last cases neg of neg is pos
  UnaryFormula uf0Neg = new UnaryFormula(this.neg, "neg", this.zero);
  UnaryFormula ufPosNeg = new UnaryFormula(this.neg, "neg", this.posNum);
  UnaryFormula ufNegNeg = new UnaryFormula(this.neg, "neg", this.negNum);
  
  // unary examples with const children, sqr function
  UnaryFormula uf0Sqr = new UnaryFormula(this.sqr, "sqr", this.zero);
  UnaryFormula ufPosSqr = new UnaryFormula(this.sqr, "sqr", this.posNum);
  UnaryFormula ufNegSqr = new UnaryFormula(this.sqr, "sqr", this.negNum);
  
  // unary examples with unary children
  UnaryFormula ununNeg = new UnaryFormula(this.neg, "neg", this.zero);
  UnaryFormula ununSqr = new UnaryFormula(this.sqr, "sqr", this.zero);
  
  // binary examples
  
  // binary examples with const children
  BinaryFormula biPlus = new BinaryFormula(this.plus, "plus", this.zero, this.posNum);
  BinaryFormula biMinus = new BinaryFormula(this.minus, "minus", this.zero, this.negNum);
  BinaryFormula biMul = new BinaryFormula(this.mul, "mul", this.posNum, this.negNum);
  BinaryFormula biDivBy0 = new BinaryFormula(this.div, "div", this.posNum, this.zero);
  BinaryFormula biDiv = new BinaryFormula(this.div, "div", this.posNum, this.even);
  
  // binary examples with unary children
  UnaryFormula biun = new UnaryFormula(this.neg, "neg", this.zero);
  
  // binary examples with binary children
  UnaryFormula bibi = new UnaryFormula(this.neg, "neg", this.zero);
  
  // Combined examples
  
  // unary examples with binary children
  UnaryFormula unbiNeg = new UnaryFormula(this.neg, "neg", this.zero);
  UnaryFormula unbiSqr = new UnaryFormula(this.sqr, "sqr", this.zero);
  
  // binary examples wiht unary children
  BinaryFormula biunNeg = new BinaryFormula(this.plus, "plus", this.zero, this.posNum);
  BinaryFormula biunSqr = new BinaryFormula(this.minus, "minus", this.zero, this.negNum);
  
  // tests the apply method using the 4 different visitor types
  boolean testApply(Tester t) {
    
    // tests the eval visitor type with a const, negative, positive, 0, even, odd
    return t.checkExpect(this.ev.apply(this.zero), 0.0)
        && t.checkExpect(this.ev.apply(this.posNum), 100.0)
        && t.checkExpect(this.ev.apply(this.even), 2.0)
        && t.checkExpect(this.ev.apply(this.negNum), -5.0)
        && t.checkExpect(this.ev.apply(this.odd), 3.0)
        
        // tests the print visitor type with a const, negative, positive, 0, even, odd
        && t.checkExpect(this.pv.apply(this.zero), "0.0")
        && t.checkExpect(this.pv.apply(this.posNum), "100.0")
        && t.checkExpect(this.pv.apply(this.even), "2.0")
        && t.checkExpect(this.pv.apply(this.negNum), "-5.0")
        && t.checkExpect(this.pv.apply(this.odd), "3.0")
        
        // tests the mirror visitor type with a const, negative, positive, 0, even, odd
        && t.checkExpect(this.mv.apply(this.zero), this.zero)
        && t.checkExpect(this.mv.apply(this.posNum), this.posNum)
        && t.checkExpect(this.mv.apply(this.even), this.even)
        && t.checkExpect(this.mv.apply(this.negNum), this.negNum)
        && t.checkExpect(this.mv.apply(this.odd), this.odd)
        
        // tests the all even visitor type with a const, negative, positive, 0, even, odd
        && t.checkExpect(this.aev.apply(this.zero), true)
        && t.checkExpect(this.aev.apply(this.posNum), true)
        && t.checkExpect(this.aev.apply(this.even), true)
        && t.checkExpect(this.aev.apply(this.negNum), false)
        && t.checkExpect(this.aev.apply(this.odd), false)
        
        // test examples of all 4 visitor types with a unary formula with a const child
        
        // tests the eval visitor type with unary func, neg and sqr, pos and neg
        && t.checkExpect(this.ev.apply(this.ufPosNeg), -100.0)
        && t.checkExpect(this.ev.apply(this.ufNegNeg), 5.0)
        && t.checkExpect(this.ev.apply(this.ufPosSqr), 10000.0)
        && t.checkExpect(this.ev.apply(this.ufNegSqr), 25.0)
        
        // tests the print visitor type with unary func, neg and sqr, pos and neg
        && t.checkExpect(this.pv.apply(this.ufPosNeg), "(neg 100.0)")
        && t.checkExpect(this.pv.apply(this.ufNegNeg), "(neg -5.0)")
        && t.checkExpect(this.pv.apply(this.ufPosSqr), "(sqr 100.0)")
        && t.checkExpect(this.pv.apply(this.ufNegSqr), "(sqr -5.0)")
        
        // tests the mirror visitor type with unary func, neg and sqr, pos and neg
        && t.checkExpect(this.mv.apply(this.ufPosNeg), new UnaryFormula(this.neg, "neg", this.posNum))
        && t.checkExpect(this.mv.apply(this.ufNegNeg), new UnaryFormula(this.neg, "neg", this.negNum))
        && t.checkExpect(this.mv.apply(this.ufPosSqr), new UnaryFormula(this.sqr, "sqr", this.posNum))
        && t.checkExpect(this.mv.apply(this.ufNegSqr), new UnaryFormula(this.sqr, "sqr", this.negNum))
        
        // tests the all even visitor type with unary func, neg and sqr, pos and neg
        && t.checkExpect(this.aev.apply(this.ufPosNeg), true)
        && t.checkExpect(this.aev.apply(this.ufNegNeg), false)
        && t.checkExpect(this.aev.apply(this.ufPosSqr), true)
        && t.checkExpect(this.aev.apply(this.ufNegSqr), false)
        
        // test examples of all 4 visitor types with a binary formula with a const child
        
        // tests the eval visitor type with binary func, plus minus mul divby0 and div
        && t.checkExpect(this.ev.apply(this.biPlus), 100.0)
        && t.checkExpect(this.ev.apply(this.biMinus), 5.0)
        && t.checkExpect(this.ev.apply(this.biMul), -500.0)
        && t.checkExpect(this.ev.apply(this.biDivBy0).equals(Double.POSITIVE_INFINITY), true)
        && t.checkExpect(this.ev.apply(this.biDiv), 50.0)
        
        // tests the print visitor type with binary func, plus minus mul divby0 and div
        && t.checkExpect(this.pv.apply(this.biPlus), "(plus 0.0 100.0)")
        && t.checkExpect(this.pv.apply(this.biMinus), "(minus 0.0 -5.0)")
        && t.checkExpect(this.pv.apply(this.biMul), "(mul 100.0 -5.0)")
        && t.checkExpect(this.pv.apply(this.biDivBy0), "(div 100.0 0.0)")
        && t.checkExpect(this.pv.apply(this.biDiv), "(div 100.0 2.0)")
        
        // tests the mirror visitor type with binary func, plus minus mul divby0 and div
        && t.checkExpect(this.mv.apply(this.biPlus),
            new BinaryFormula(this.plus, "plus", this.posNum, this.zero))
        && t.checkExpect(this.mv.apply(this.biMinus),
            new BinaryFormula(this.minus, "minus", this.negNum, this.zero))
        && t.checkExpect(this.mv.apply(this.biMul),
            new BinaryFormula(this.mul, "mul", this.negNum, this.posNum))
        && t.checkExpect(this.mv.apply(this.biDivBy0),
            new BinaryFormula(this.div, "div", this.zero, this.posNum))
        && t.checkExpect(this.mv.apply(this.biDiv),
            new BinaryFormula(this.div, "div", this.even, this.posNum))
        
        // tests the all even visitor type with binary func, plus minus mul divby0 and div
        && t.checkExpect(this.aev.apply(this.biPlus), true)
        && t.checkExpect(this.aev.apply(this.biMinus), false)
        && t.checkExpect(this.aev.apply(this.biMul), false)
        && t.checkExpect(this.aev.apply(this.biDivBy0), true)
        && t.checkExpect(this.aev.apply(this.biDiv), true);
  }
  
  //
  boolean testVisitConst(Tester t) {
    
    // tests the eval visitor type, negative, positive, 0, even, odd
    return t.checkExpect(this.ev.visitConst(this.zero), 0.0)
        && t.checkExpect(this.ev.visitConst(this.posNum), 100.0)
        && t.checkExpect(this.ev.visitConst(this.even), 2.0)
        && t.checkExpect(this.ev.visitConst(this.negNum), -5.0)
        && t.checkExpect(this.ev.visitConst(this.odd), 3.0)
        
        // tests the print visitor type, negative, positive, 0, even, odd
        && t.checkExpect(this.pv.visitConst(this.zero), "0.0")
        && t.checkExpect(this.pv.visitConst(this.posNum), "100.0")
        && t.checkExpect(this.pv.visitConst(this.even), "2.0")
        && t.checkExpect(this.pv.visitConst(this.negNum), "-5.0")
        && t.checkExpect(this.pv.visitConst(this.odd), "3.0")
        
        // tests the mirror visitor type, negative, positive, 0, even, odd
        && t.checkExpect(this.mv.visitConst(this.zero), this.zero)
        && t.checkExpect(this.mv.visitConst(this.posNum), this.posNum)
        && t.checkExpect(this.mv.visitConst(this.even), this.even)
        && t.checkExpect(this.mv.visitConst(this.negNum), this.negNum)
        && t.checkExpect(this.mv.visitConst(this.odd), this.odd)
        
        // tests the all even visitor type, negative, positive, 0, even, odd
        && t.checkExpect(this.aev.visitConst(this.zero), true)
        && t.checkExpect(this.aev.visitConst(this.posNum), true)
        && t.checkExpect(this.aev.visitConst(this.even), true)
        && t.checkExpect(this.aev.visitConst(this.negNum), false)
        && t.checkExpect(this.aev.visitConst(this.odd), false);
  }
  
  //
  boolean testVisitUn(Tester t) {
    // tests the eval visitor type with unary func, neg and sqr, pos and neg
    return t.checkExpect(this.ev.visitUn(this.ufPosNeg), -100.0)
        && t.checkExpect(this.ev.visitUn(this.ufNegNeg), 5.0)
        && t.checkExpect(this.ev.visitUn(this.ufPosSqr), 10000.0)
        && t.checkExpect(this.ev.visitUn(this.ufNegSqr), 25.0)
        
        // tests the print visitor type with unary func, neg and sqr, pos and neg
        && t.checkExpect(this.pv.visitUn(this.ufPosNeg), "(neg 100.0)")
        && t.checkExpect(this.pv.visitUn(this.ufNegNeg), "(neg -5.0)")
        && t.checkExpect(this.pv.visitUn(this.ufPosSqr), "(sqr 100.0)")
        && t.checkExpect(this.pv.visitUn(this.ufNegSqr), "(sqr -5.0)")
        
        // tests the mirror visitor type with unary func, neg and sqr, pos and neg
        && t.checkExpect(this.mv.visitUn(this.ufPosNeg), new UnaryFormula(this.neg, "neg", this.posNum))
        && t.checkExpect(this.mv.visitUn(this.ufNegNeg), new UnaryFormula(this.neg, "neg", this.negNum))
        && t.checkExpect(this.mv.visitUn(this.ufPosSqr), new UnaryFormula(this.sqr, "sqr", this.posNum))
        && t.checkExpect(this.mv.visitUn(this.ufNegSqr), new UnaryFormula(this.sqr, "sqr", this.negNum))
        
        // tests the all even visitor type with unary func, neg and sqr, pos and neg
        && t.checkExpect(this.aev.visitUn(this.ufPosNeg), true)
        && t.checkExpect(this.aev.visitUn(this.ufNegNeg), false)
        && t.checkExpect(this.aev.visitUn(this.ufPosSqr), true)
        && t.checkExpect(this.aev.visitUn(this.ufNegSqr), false);
  }
  
  //
  boolean testVisitBi(Tester t) {
    
    // tests the eval visitor type with binary func, plus minus mul divby0 and div
    return t.checkExpect(this.ev.visitBi(this.biPlus), 100.0)
    && t.checkExpect(this.ev.visitBi(this.biMinus), 5.0)
    && t.checkExpect(this.ev.visitBi(this.biMul), -500.0)
    && t.checkExpect(this.ev.visitBi(this.biDivBy0).equals(Double.POSITIVE_INFINITY), true)
    && t.checkExpect(this.ev.visitBi(this.biDiv), 50.0)
    
    // tests the print visitor type with binary func, plus minus mul divby0 and div
    && t.checkExpect(this.pv.visitBi(this.biPlus), "(plus 0.0 100.0)")
    && t.checkExpect(this.pv.visitBi(this.biMinus), "(minus 0.0 -5.0)")
    && t.checkExpect(this.pv.visitBi(this.biMul), "(mul 100.0 -5.0)")
    && t.checkExpect(this.pv.visitBi(this.biDivBy0), "(div 100.0 0.0)")
    && t.checkExpect(this.pv.visitBi(this.biDiv), "(div 100.0 2.0)")
    
    // tests the mirror visitor type with binary func, plus minus mul divby0 and div
    && t.checkExpect(this.mv.visitBi(this.biPlus),
        new BinaryFormula(this.plus, "plus", this.posNum, this.zero))
    && t.checkExpect(this.mv.visitBi(this.biMinus),
        new BinaryFormula(this.minus, "minus", this.negNum, this.zero))
    && t.checkExpect(this.mv.visitBi(this.biMul),
        new BinaryFormula(this.mul, "mul", this.negNum, this.posNum))
    && t.checkExpect(this.mv.visitBi(this.biDivBy0),
        new BinaryFormula(this.div, "div", this.zero, this.posNum))
    && t.checkExpect(this.mv.visitBi(this.biDiv),
        new BinaryFormula(this.div, "div", this.even, this.posNum))
    
    // tests the all even visitor type with binary func, plus minus mul divby0 and div
    && t.checkExpect(this.aev.visitBi(this.biPlus), true)
    && t.checkExpect(this.aev.visitBi(this.biMinus), false)
    && t.checkExpect(this.aev.visitBi(this.biMul), false)
    && t.checkExpect(this.aev.visitBi(this.biDivBy0), true)
    && t.checkExpect(this.aev.visitBi(this.biDiv), true);
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
