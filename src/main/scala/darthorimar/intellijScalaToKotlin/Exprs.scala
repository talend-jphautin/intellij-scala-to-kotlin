package darthorimar.intellijScalaToKotlin

import darthorimar.intellijScalaToKotlin.ast._
import darthorimar.intellijScalaToKotlin.types.{KotlinTypes, StdTypes}

object Exprs {
  def is(expr: Expr, ty: Type) =
    simpleInfix(StdTypes.BOOLEAN, "is", expr, TypeExpr(ty))

  def as(expr: Expr, ty: Type) =
    simpleInfix(StdTypes.BOOLEAN, "as", expr, TypeExpr(ty))

  def and(left: Expr, right: Expr) =
    simpleInfix(StdTypes.BOOLEAN, "&&", left, right)

  def or(left: Expr, right: Expr) =
    simpleInfix(StdTypes.BOOLEAN, "||", left, right)

  def letExpr(obj: Expr, lambda: LambdaExpr) =
    CallExpr(lambda.exprType, RefExpr(NoType, Some(obj), "let", Seq.empty, true), Seq(lambda), Seq.empty)

  def simpleInfix(resultType: Type, op: String, left: Expr, right: Expr) =
    InfixExpr(FunctionType(right.exprType, resultType),
      RefExpr(FunctionType(right.exprType, resultType), None, op, Seq.empty, false),
      left,
      right,
      true)

  def emptyList(ty: Type) =
    CallExpr(
      listType(ty),
      RefExpr(ty, None, "emptyList", Seq(ty), true),
      Seq.empty,
      Seq.empty)

  def emptyList =
    CallExpr(
      listType(NoType),
      RefExpr(NoType, None, "emptyList", Seq.empty, true),
      Seq.empty,
      Seq.empty)

  def listType(ty: Type) =
    GenericType(KotlinTypes.LIST, Seq(ty))

  def simpleCall(name: String, returnType: Type, aruments: Seq[Expr]) =
    CallExpr(FunctionType(ProductType(aruments.map(_.exprType)), returnType),
      RefExpr(returnType, None, name, Seq.empty, true),
      aruments, Seq.empty
    )

  def simpleRef(name: String, refType: Type) =
    RefExpr(refType, None, name, Seq.empty, false)

  def runExpr(expr: Expr) =
    simpleCall("run", expr.exprType, Seq(LambdaExpr(expr.exprType, Seq.empty, expr, false)))


  val falseLit = LitExpr(StdTypes.BOOLEAN, "false")
  val trueLit = LitExpr(StdTypes.BOOLEAN, "true")
  val nullLit = LitExpr(NoType, "null") //TODO fix
}
