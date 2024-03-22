// Generated from D:/yd/code/tabbyetl/src/main/antlr4/TTL.g4 by ANTLR 4.13.1
package com.regtab.core.rtl.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TTLParser}.
 */
public interface TTLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TTLParser#table}.
	 * @param ctx the parse tree
	 */
	void enterTable(TTLParser.TableContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#table}.
	 * @param ctx the parse tree
	 */
	void exitTable(TTLParser.TableContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#subtable}.
	 * @param ctx the parse tree
	 */
	void enterSubtable(TTLParser.SubtableContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#subtable}.
	 * @param ctx the parse tree
	 */
	void exitSubtable(TTLParser.SubtableContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#rows}.
	 * @param ctx the parse tree
	 */
	void enterRows(TTLParser.RowsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#rows}.
	 * @param ctx the parse tree
	 */
	void exitRows(TTLParser.RowsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#label}.
	 * @param ctx the parse tree
	 */
	void enterLabel(TTLParser.LabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#label}.
	 * @param ctx the parse tree
	 */
	void exitLabel(TTLParser.LabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#replacement}.
	 * @param ctx the parse tree
	 */
	void enterReplacement(TTLParser.ReplacementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#replacement}.
	 * @param ctx the parse tree
	 */
	void exitReplacement(TTLParser.ReplacementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#quantifier}.
	 * @param ctx the parse tree
	 */
	void enterQuantifier(TTLParser.QuantifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#quantifier}.
	 * @param ctx the parse tree
	 */
	void exitQuantifier(TTLParser.QuantifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#zeroOrOne}.
	 * @param ctx the parse tree
	 */
	void enterZeroOrOne(TTLParser.ZeroOrOneContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#zeroOrOne}.
	 * @param ctx the parse tree
	 */
	void exitZeroOrOne(TTLParser.ZeroOrOneContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#zeroOrMore}.
	 * @param ctx the parse tree
	 */
	void enterZeroOrMore(TTLParser.ZeroOrMoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#zeroOrMore}.
	 * @param ctx the parse tree
	 */
	void exitZeroOrMore(TTLParser.ZeroOrMoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#oneOrMore}.
	 * @param ctx the parse tree
	 */
	void enterOneOrMore(TTLParser.OneOrMoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#oneOrMore}.
	 * @param ctx the parse tree
	 */
	void exitOneOrMore(TTLParser.OneOrMoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#exactly}.
	 * @param ctx the parse tree
	 */
	void enterExactly(TTLParser.ExactlyContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#exactly}.
	 * @param ctx the parse tree
	 */
	void exitExactly(TTLParser.ExactlyContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#row}.
	 * @param ctx the parse tree
	 */
	void enterRow(TTLParser.RowContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#row}.
	 * @param ctx the parse tree
	 */
	void exitRow(TTLParser.RowContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#subrows}.
	 * @param ctx the parse tree
	 */
	void enterSubrows(TTLParser.SubrowsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#subrows}.
	 * @param ctx the parse tree
	 */
	void exitSubrows(TTLParser.SubrowsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#subrow}.
	 * @param ctx the parse tree
	 */
	void enterSubrow(TTLParser.SubrowContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#subrow}.
	 * @param ctx the parse tree
	 */
	void exitSubrow(TTLParser.SubrowContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#cells}.
	 * @param ctx the parse tree
	 */
	void enterCells(TTLParser.CellsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#cells}.
	 * @param ctx the parse tree
	 */
	void exitCells(TTLParser.CellsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#cell}.
	 * @param ctx the parse tree
	 */
	void enterCell(TTLParser.CellContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#cell}.
	 * @param ctx the parse tree
	 */
	void exitCell(TTLParser.CellContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#elements}.
	 * @param ctx the parse tree
	 */
	void enterElements(TTLParser.ElementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#elements}.
	 * @param ctx the parse tree
	 */
	void exitElements(TTLParser.ElementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#element}.
	 * @param ctx the parse tree
	 */
	void enterElement(TTLParser.ElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#element}.
	 * @param ctx the parse tree
	 */
	void exitElement(TTLParser.ElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#elementType}.
	 * @param ctx the parse tree
	 */
	void enterElementType(TTLParser.ElementTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#elementType}.
	 * @param ctx the parse tree
	 */
	void exitElementType(TTLParser.ElementTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#tags}.
	 * @param ctx the parse tree
	 */
	void enterTags(TTLParser.TagsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#tags}.
	 * @param ctx the parse tree
	 */
	void exitTags(TTLParser.TagsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#actions}.
	 * @param ctx the parse tree
	 */
	void enterActions(TTLParser.ActionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#actions}.
	 * @param ctx the parse tree
	 */
	void exitActions(TTLParser.ActionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#action}.
	 * @param ctx the parse tree
	 */
	void enterAction(TTLParser.ActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#action}.
	 * @param ctx the parse tree
	 */
	void exitAction(TTLParser.ActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#actionType}.
	 * @param ctx the parse tree
	 */
	void enterActionType(TTLParser.ActionTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#actionType}.
	 * @param ctx the parse tree
	 */
	void exitActionType(TTLParser.ActionTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#actionBody}.
	 * @param ctx the parse tree
	 */
	void enterActionBody(TTLParser.ActionBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#actionBody}.
	 * @param ctx the parse tree
	 */
	void exitActionBody(TTLParser.ActionBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#struct}.
	 * @param ctx the parse tree
	 */
	void enterStruct(TTLParser.StructContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#struct}.
	 * @param ctx the parse tree
	 */
	void exitStruct(TTLParser.StructContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#line}.
	 * @param ctx the parse tree
	 */
	void enterLine(TTLParser.LineContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#line}.
	 * @param ctx the parse tree
	 */
	void exitLine(TTLParser.LineContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#startText}.
	 * @param ctx the parse tree
	 */
	void enterStartText(TTLParser.StartTextContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#startText}.
	 * @param ctx the parse tree
	 */
	void exitStartText(TTLParser.StartTextContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#separator}.
	 * @param ctx the parse tree
	 */
	void enterSeparator(TTLParser.SeparatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#separator}.
	 * @param ctx the parse tree
	 */
	void exitSeparator(TTLParser.SeparatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#endText}.
	 * @param ctx the parse tree
	 */
	void enterEndText(TTLParser.EndTextContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#endText}.
	 * @param ctx the parse tree
	 */
	void exitEndText(TTLParser.EndTextContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#choice}.
	 * @param ctx the parse tree
	 */
	void enterChoice(TTLParser.ChoiceContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#choice}.
	 * @param ctx the parse tree
	 */
	void exitChoice(TTLParser.ChoiceContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#choiceBody}.
	 * @param ctx the parse tree
	 */
	void enterChoiceBody(TTLParser.ChoiceBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#choiceBody}.
	 * @param ctx the parse tree
	 */
	void exitChoiceBody(TTLParser.ChoiceBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterCond(TTLParser.CondContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitCond(TTLParser.CondContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#constr}.
	 * @param ctx the parse tree
	 */
	void enterConstr(TTLParser.ConstrContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#constr}.
	 * @param ctx the parse tree
	 */
	void exitConstr(TTLParser.ConstrContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#lookup}.
	 * @param ctx the parse tree
	 */
	void enterLookup(TTLParser.LookupContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#lookup}.
	 * @param ctx the parse tree
	 */
	void exitLookup(TTLParser.LookupContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#all}.
	 * @param ctx the parse tree
	 */
	void enterAll(TTLParser.AllContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#all}.
	 * @param ctx the parse tree
	 */
	void exitAll(TTLParser.AllContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#direction}.
	 * @param ctx the parse tree
	 */
	void enterDirection(TTLParser.DirectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#direction}.
	 * @param ctx the parse tree
	 */
	void exitDirection(TTLParser.DirectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#where}.
	 * @param ctx the parse tree
	 */
	void enterWhere(TTLParser.WhereContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#where}.
	 * @param ctx the parse tree
	 */
	void exitWhere(TTLParser.WhereContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#range}.
	 * @param ctx the parse tree
	 */
	void enterRange(TTLParser.RangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#range}.
	 * @param ctx the parse tree
	 */
	void exitRange(TTLParser.RangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#rowRange}.
	 * @param ctx the parse tree
	 */
	void enterRowRange(TTLParser.RowRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#rowRange}.
	 * @param ctx the parse tree
	 */
	void exitRowRange(TTLParser.RowRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#colRange}.
	 * @param ctx the parse tree
	 */
	void enterColRange(TTLParser.ColRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#colRange}.
	 * @param ctx the parse tree
	 */
	void exitColRange(TTLParser.ColRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#rangeBody}.
	 * @param ctx the parse tree
	 */
	void enterRangeBody(TTLParser.RangeBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#rangeBody}.
	 * @param ctx the parse tree
	 */
	void exitRangeBody(TTLParser.RangeBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(TTLParser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(TTLParser.StartContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#end}.
	 * @param ctx the parse tree
	 */
	void enterEnd(TTLParser.EndContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#end}.
	 * @param ctx the parse tree
	 */
	void exitEnd(TTLParser.EndContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#relative}.
	 * @param ctx the parse tree
	 */
	void enterRelative(TTLParser.RelativeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#relative}.
	 * @param ctx the parse tree
	 */
	void exitRelative(TTLParser.RelativeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#index}.
	 * @param ctx the parse tree
	 */
	void enterIndex(TTLParser.IndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#index}.
	 * @param ctx the parse tree
	 */
	void exitIndex(TTLParser.IndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code strLiteral}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterStrLiteral(TTLParser.StrLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code strLiteral}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitStrLiteral(TTLParser.StrLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code strExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterStrExpr(TTLParser.StrExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code strExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitStrExpr(TTLParser.StrExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code thisExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterThisExpr(TTLParser.ThisExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code thisExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitThisExpr(TTLParser.ThisExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arithmExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterArithmExpr(TTLParser.ArithmExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arithmExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitArithmExpr(TTLParser.ArithmExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code notExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNotExpr(TTLParser.NotExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code notExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNotExpr(TTLParser.NotExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code funcExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterFuncExpr(TTLParser.FuncExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code funcExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitFuncExpr(TTLParser.FuncExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code propExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterPropExpr(TTLParser.PropExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code propExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitPropExpr(TTLParser.PropExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code compExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCompExpr(TTLParser.CompExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code compExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCompExpr(TTLParser.CompExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intLiteral}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIntLiteral(TTLParser.IntLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intLiteral}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIntLiteral(TTLParser.IntLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBinaryExpr(TTLParser.BinaryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBinaryExpr(TTLParser.BinaryExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code boolLiteral}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBoolLiteral(TTLParser.BoolLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code boolLiteral}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBoolLiteral(TTLParser.BoolLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParenExpr(TTLParser.ParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenExpr}
	 * labeled alternative in {@link TTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParenExpr(TTLParser.ParenExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#compOp}.
	 * @param ctx the parse tree
	 */
	void enterCompOp(TTLParser.CompOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#compOp}.
	 * @param ctx the parse tree
	 */
	void exitCompOp(TTLParser.CompOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#binaryOp}.
	 * @param ctx the parse tree
	 */
	void enterBinaryOp(TTLParser.BinaryOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#binaryOp}.
	 * @param ctx the parse tree
	 */
	void exitBinaryOp(TTLParser.BinaryOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#func}.
	 * @param ctx the parse tree
	 */
	void enterFunc(TTLParser.FuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#func}.
	 * @param ctx the parse tree
	 */
	void exitFunc(TTLParser.FuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#arg}.
	 * @param ctx the parse tree
	 */
	void enterArg(TTLParser.ArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#arg}.
	 * @param ctx the parse tree
	 */
	void exitArg(TTLParser.ArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#prop}.
	 * @param ctx the parse tree
	 */
	void enterProp(TTLParser.PropContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#prop}.
	 * @param ctx the parse tree
	 */
	void exitProp(TTLParser.PropContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#bool}.
	 * @param ctx the parse tree
	 */
	void enterBool(TTLParser.BoolContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#bool}.
	 * @param ctx the parse tree
	 */
	void exitBool(TTLParser.BoolContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#arithmOp}.
	 * @param ctx the parse tree
	 */
	void enterArithmOp(TTLParser.ArithmOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#arithmOp}.
	 * @param ctx the parse tree
	 */
	void exitArithmOp(TTLParser.ArithmOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link TTLParser#strOp}.
	 * @param ctx the parse tree
	 */
	void enterStrOp(TTLParser.StrOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link TTLParser#strOp}.
	 * @param ctx the parse tree
	 */
	void exitStrOp(TTLParser.StrOpContext ctx);
}