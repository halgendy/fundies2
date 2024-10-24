import java.util.function.BiFunction;
import java.util.function.Function;

//REPRESENTS
interface IArithVisitor<R> {
  
  //REPRESENTS
  R visitConst(Const constant);
  
  //REPRESENTS
  R visitUn(UnaryFormula unFormula);
  
  //REPRESENTS
  R visitBi(BinaryFormula biFormula);
  
  // using r as like return type
}

//REPRESENTS
class EvalVistor implements IArithVisitor<Double> {
  
  // REPRESENTS
  public Double visitConst(Const c) {
    return 0.0;
  }
  
  // REPRESENTS  
  public Double visitUn(UnaryFormula unFormula) {
    return 0.0;
  }
  
  // REPRESENTS 
  public Double visitBi(BinaryFormula biFormula) {
    return 0.0;
  }
}

//REPRESENTS
class PrintVistor implements IArithVisitor<String> {
  
  // REPRESENTS
  public String visitConst(Const c) {
    return "";
  }
  
  // REPRESENTS  
  public String visitUn(UnaryFormula unFormula) {
    return "";
  }
  
  // REPRESENTS 
  public String visitBi(BinaryFormula biFormula) {
    return "";
  }
}

//REPRESENTS
class MirrorVisitor implements IArithVisitor<IArith> {
  
  // REPRESENTS
  public IArith visitConst(Const c) {
    return null;
  }
  
  // REPRESENTS  
  public IArith visitUn(UnaryFormula unFormula) {
    return null;
  }
  
  // REPRESENTS 
  public IArith visitBi(BinaryFormula biFormula) {
    return null;
  }
}

//REPRESENTS
class AllEvenVisitor implements IArithVisitor<Boolean> {
  
  // REPRESENTS
  public Boolean visitConst(Const c) {
    return true;
  }
  
  // REPRESENTS  
  public Boolean visitUn(UnaryFormula unFormula) {
    return true;
  }
  
  // REPRESENTS 
  public Boolean visitBi(BinaryFormula biFormula) {
    return true;
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
class Const {
  double num;
  
  Const(double num) {
    this.num = num;
  }
  
  //REPRESENTS
  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitConst(this);
  }
}

//REPRESENTS
class UnaryFormula {
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
class BinaryFormula {
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
    return 0.0;
  }
}

//REPRESENTS
class Sqr implements Function<Double, Double> {

  @Override
  //REPRESENTS
  public Double apply(Double t) {
  return 0.0;
  }
}

/// BINARY FUNCTION INTERFACE ///

//REPRESENTS
class Plus implements BiFunction<Double, Double, Double> {
  
  @Override
  // REPRESENTS
  public Double apply(Double t, Double u) {
    return 0.0;
  }
}
 
//REPRESENTS
class Minus implements BiFunction<Double, Double, Double> {

  @Override
  // REPRESENTS
  public Double apply(Double t, Double u) {
    return 0.0;
  }
}

//REPRESENTS
class Mull implements BiFunction<Double, Double, Double> {

  @Override
  // REPRESENTS
  public Double apply(Double t, Double u) {
    return 0.0;
  }
}

//REPRESENTS
class Div implements BiFunction<Double, Double, Double> {

  @Override
  // REPRESENTS
  public Double apply(Double t, Double u) {
    return 0.0;
  }
}

class ExamplesVistors {
  
  
}
