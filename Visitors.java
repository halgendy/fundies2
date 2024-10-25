import java.util.function.BiFunction;
import java.util.function.Function;
import tester.*;

//REPRESENTS the vistor interface for arith
interface IArithVisitor<R> {

  // REPRESENTS the central apply method
  R apply(IArith arith);

  // REPRESENTS the central visit for const
  R visitConst(Const constant);

  // REPRESENTS the central visit for unary
  R visitUn(UnaryFormula unFormula);

  // REPRESENTS the central visit for binary
  R visitBi(BinaryFormula biFormula);

  // using r as like return type
}

//REPRESENTS a type of visitor: evaluates
class EvalVisitor implements IArithVisitor<Double> {

  // CONSTRUCTOR
  EvalVisitor() {
  }

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

  // CONSTRUCTOR
  PrintVisitor() {
  }

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
    return "(" + biFormula.name + " " + biFormula.left.accept(this) + " "
        + biFormula.right.accept(this) + ")";
  }
}

//REPRESENTS a type of visitor: mirrors tree
class MirrorVisitor implements IArithVisitor<IArith> {

  // CONSTRUCTOR
  MirrorVisitor() {
  }

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

    // IArith holdLeft = biFormula.left;

    // biFormula.left = biFormula.right.accept(this);
    // biFormula.right = holdLeft.accept(this);

    return new BinaryFormula(biFormula.func, biFormula.name, biFormula.right.accept(this),
        biFormula.left.accept(this));
  }
}

//REPRESENTS a type of visitor: tree all even
class AllEvenVisitor implements IArithVisitor<Boolean> {

  // CONSTRUCTOR
  AllEvenVisitor() {
  }

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
  // IArithVisitor<R> accept();

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

  // REPRESENTS local accept of visitor
  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitConst(this);
  }
}

//REPRESENTS a unary formula applied to the child
class UnaryFormula implements IArith {
  Function<Double, Double> func;
  String name;
  IArith child;

  // CONSTRUCTOR
  UnaryFormula(Function<Double, Double> func, String name, IArith child) {
    this.func = func;
    this.name = name;
    this.child = child;
  }

  // REPRESENTS local accept of visitor
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

  // CONSTRUCTOR
  BinaryFormula(BiFunction<Double, Double, Double> func, String name, IArith left, IArith right) {
    this.func = func;
    this.name = name;
    this.left = left;
    this.right = right;
  }

  // REPRESENTS local accept of visitor
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
  // REPRESENTS
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
  Const odd = new Const(3.0);

  // Function test examples
  Function<Double, Double> neg = new Neg();
  Function<Double, Double> sqr = new Sqr();
  BiFunction<Double, Double, Double> plus = new Plus();
  BiFunction<Double, Double, Double> minus = new Minus();
  BiFunction<Double, Double, Double> mul = new Mul();
  BiFunction<Double, Double, Double> div = new Div();

  // unary examples

  // unary examples with const children, neg function, last cases neg of neg is
  // pos
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

        // tests the all even visitor type with a const, negative, positive, 0, even,
        // odd
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
        && t.checkExpect(this.mv.apply(this.ufPosNeg),
            new UnaryFormula(this.neg, "neg", this.posNum))
        && t.checkExpect(this.mv.apply(this.ufNegNeg),
            new UnaryFormula(this.neg, "neg", this.negNum))
        && t.checkExpect(this.mv.apply(this.ufPosSqr),
            new UnaryFormula(this.sqr, "sqr", this.posNum))
        && t.checkExpect(this.mv.apply(this.ufNegSqr),
            new UnaryFormula(this.sqr, "sqr", this.negNum))

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

        // tests the all even visitor type with binary func, plus minus mul divby0 and
        // div
        && t.checkExpect(this.aev.apply(this.biPlus), true)
        && t.checkExpect(this.aev.apply(this.biMinus), false)
        && t.checkExpect(this.aev.apply(this.biMul), false)
        && t.checkExpect(this.aev.apply(this.biDivBy0), true)
        && t.checkExpect(this.aev.apply(this.biDiv), true);
  }

  // REPRESENTS testing of a const visit
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

  // REPRESENTS testing of a unary visit
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
        && t.checkExpect(this.mv.visitUn(this.ufPosNeg),
            new UnaryFormula(this.neg, "neg", this.posNum))
        && t.checkExpect(this.mv.visitUn(this.ufNegNeg),
            new UnaryFormula(this.neg, "neg", this.negNum))
        && t.checkExpect(this.mv.visitUn(this.ufPosSqr),
            new UnaryFormula(this.sqr, "sqr", this.posNum))
        && t.checkExpect(this.mv.visitUn(this.ufNegSqr),
            new UnaryFormula(this.sqr, "sqr", this.negNum))

        // tests the all even visitor type with unary func, neg and sqr, pos and neg
        && t.checkExpect(this.aev.visitUn(this.ufPosNeg), true)
        && t.checkExpect(this.aev.visitUn(this.ufNegNeg), false)
        && t.checkExpect(this.aev.visitUn(this.ufPosSqr), true)
        && t.checkExpect(this.aev.visitUn(this.ufNegSqr), false);
  }

  // REPRESENTS testing of a binary visit
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

        // tests the all even visitor type with binary func, plus minus mul divby0 and
        // div
        && t.checkExpect(this.aev.visitBi(this.biPlus), true)
        && t.checkExpect(this.aev.visitBi(this.biMinus), false)
        && t.checkExpect(this.aev.visitBi(this.biMul), false)
        && t.checkExpect(this.aev.visitBi(this.biDivBy0), true)
        && t.checkExpect(this.aev.visitBi(this.biDiv), true);
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
    return t.checkExpect(this.divF.apply(1.0, 0.0).equals(Double.POSITIVE_INFINITY), true)
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
