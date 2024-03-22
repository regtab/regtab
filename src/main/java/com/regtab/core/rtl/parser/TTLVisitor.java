// Generated from D:/yd/code/tabbyetl/src/main/antlr4/TTL.g4 by ANTLR 4.13.1
package com.regtab.core.rtl.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link TTLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface TTLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link TTLParser#table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable(TTLParser.TableContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#subtable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubtable(TTLParser.SubtableContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#rows}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRows(TTLParser.RowsContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#label}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabel(TTLParser.LabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#replacement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplacement(TTLParser.ReplacementContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#quantifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuantifier(TTLParser.QuantifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#zeroOrOne}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitZeroOrOne(TTLParser.ZeroOrOneContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#zeroOrMore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitZeroOrMore(TTLParser.ZeroOrMoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#oneOrMore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOneOrMore(TTLParser.OneOrMoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#exactly}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExactly(TTLParser.ExactlyContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#row}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRow(TTLParser.RowContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#subrows}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubrows(TTLParser.SubrowsContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#subrow}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubrow(TTLParser.SubrowContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#cells}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCells(TTLParser.CellsContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#cell}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCell(TTLParser.CellContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#elements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElements(TTLParser.ElementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElement(TTLParser.ElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#elementType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementType(TTLParser.ElementTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#tags}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTags(TTLParser.TagsContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#actions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActions(TTLParser.ActionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#action}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAction(TTLParser.ActionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#actionType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionType(TTLParser.ActionTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#actionBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionBody(TTLParser.ActionBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#struct}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStruct(TTLParser.StructContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#line}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLine(TTLParser.LineContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#startText}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStartText(TTLParser.StartTextContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#separator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeparator(TTLParser.SeparatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#endText}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEndText(TTLParser.EndTextContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#choice}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChoice(TTLParser.ChoiceContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#choiceBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChoiceBody(TTLParser.ChoiceBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#cond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCond(TTLParser.CondContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#constr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstr(TTLParser.ConstrContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#lookup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLookup(TTLParser.LookupContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#all}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAll(TTLParser.AllContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#direction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirection(TTLParser.DirectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#where}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhere(TTLParser.WhereContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#range}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRange(TTLParser.RangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#rowRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowRange(TTLParser.RowRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#colRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColRange(TTLParser.ColRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#rangeBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangeBody(TTLParser.RangeBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#start}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart(TTLParser.StartContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#end}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnd(TTLParser.EndContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#relative}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelative(TTLParser.RelativeContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#index}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex(TTLParser.IndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code strLiteral}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStrLiteral(TTLParser.StrLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code strExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStrExpr(TTLParser.StrExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code thisExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThisExpr(TTLParser.ThisExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arithmExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithmExpr(TTLParser.ArithmExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code notExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpr(TTLParser.NotExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code funcExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncExpr(TTLParser.FuncExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code propExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropExpr(TTLParser.PropExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code compExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompExpr(TTLParser.CompExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code intLiteral}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntLiteral(TTLParser.IntLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code binaryExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryExpr(TTLParser.BinaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code boolLiteral}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolLiteral(TTLParser.BoolLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExpr(TTLParser.ParenExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#compOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompOp(TTLParser.CompOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#binaryOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryOp(TTLParser.BinaryOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc(TTLParser.FuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArg(TTLParser.ArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#prop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProp(TTLParser.PropContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#bool}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBool(TTLParser.BoolContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#arithmOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithmOp(TTLParser.ArithmOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link TTLParser#strOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStrOp(TTLParser.StrOpContext ctx);
}