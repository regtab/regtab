// Generated from D:/yd/code/regtab/src/main/antlr4/RTL.g4 by ANTLR 4.13.1
package com.regtab.core.rtl.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link RTLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface RTLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link RTLParser#table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable(RTLParser.TableContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#subtable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubtable(RTLParser.SubtableContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#rows}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRows(RTLParser.RowsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#label}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabel(RTLParser.LabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#copy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCopy(RTLParser.CopyContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#quantifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuantifier(RTLParser.QuantifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#zeroOrOne}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitZeroOrOne(RTLParser.ZeroOrOneContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#zeroOrMore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitZeroOrMore(RTLParser.ZeroOrMoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#oneOrMore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOneOrMore(RTLParser.OneOrMoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#exactly}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExactly(RTLParser.ExactlyContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#row}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRow(RTLParser.RowContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#subrows}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubrows(RTLParser.SubrowsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#subrow}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubrow(RTLParser.SubrowContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#cells}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCells(RTLParser.CellsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#cell}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCell(RTLParser.CellContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElements(RTLParser.ElementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElement(RTLParser.ElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#elementType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementType(RTLParser.ElementTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#tags}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTags(RTLParser.TagsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#actions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActions(RTLParser.ActionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#action}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAction(RTLParser.ActionContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#actionType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionType(RTLParser.ActionTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#actionBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionBody(RTLParser.ActionBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#struct}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStruct(RTLParser.StructContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#startText}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStartText(RTLParser.StartTextContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#separator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeparator(RTLParser.SeparatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#endText}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEndText(RTLParser.EndTextContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#choice}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChoice(RTLParser.ChoiceContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#choiceBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChoiceBody(RTLParser.ChoiceBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#cond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCond(RTLParser.CondContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#constr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstr(RTLParser.ConstrContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#lookup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLookup(RTLParser.LookupContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#all}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAll(RTLParser.AllContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#direction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirection(RTLParser.DirectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#where}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhere(RTLParser.WhereContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#range}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRange(RTLParser.RangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#rowRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowRange(RTLParser.RowRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#colRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColRange(RTLParser.ColRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#rangeBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangeBody(RTLParser.RangeBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#start}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart(RTLParser.StartContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#end}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnd(RTLParser.EndContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#relative}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelative(RTLParser.RelativeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#elementIndex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementIndex(RTLParser.ElementIndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code strLiteral}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStrLiteral(RTLParser.StrLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code strExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStrExpr(RTLParser.StrExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code thisExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThisExpr(RTLParser.ThisExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arithmExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithmExpr(RTLParser.ArithmExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code notExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpr(RTLParser.NotExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code funcExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncExpr(RTLParser.FuncExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code propExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropExpr(RTLParser.PropExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code compExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompExpr(RTLParser.CompExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code intLiteral}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntLiteral(RTLParser.IntLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code binaryExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryExpr(RTLParser.BinaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code boolLiteral}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolLiteral(RTLParser.BoolLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExpr(RTLParser.ParenExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#compOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompOp(RTLParser.CompOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#binaryOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryOp(RTLParser.BinaryOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc(RTLParser.FuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArg(RTLParser.ArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#prop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProp(RTLParser.PropContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#bool}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBool(RTLParser.BoolContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#arithmOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithmOp(RTLParser.ArithmOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link RTLParser#strOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStrOp(RTLParser.StrOpContext ctx);
}