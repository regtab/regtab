// Generated from D:/yd/code/regtab/regtab/src/main/antlr4/RTL.g4 by ANTLR 4.13.1
package com.regtab.core.rtl.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link RTLParser}.
 */
public interface RTLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link RTLParser#table}.
	 * @param ctx the parse tree
	 */
	void enterTable(RTLParser.TableContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#table}.
	 * @param ctx the parse tree
	 */
	void exitTable(RTLParser.TableContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#subtable}.
	 * @param ctx the parse tree
	 */
	void enterSubtable(RTLParser.SubtableContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#subtable}.
	 * @param ctx the parse tree
	 */
	void exitSubtable(RTLParser.SubtableContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#rows}.
	 * @param ctx the parse tree
	 */
	void enterRows(RTLParser.RowsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#rows}.
	 * @param ctx the parse tree
	 */
	void exitRows(RTLParser.RowsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#label}.
	 * @param ctx the parse tree
	 */
	void enterLabel(RTLParser.LabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#label}.
	 * @param ctx the parse tree
	 */
	void exitLabel(RTLParser.LabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#copy}.
	 * @param ctx the parse tree
	 */
	void enterCopy(RTLParser.CopyContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#copy}.
	 * @param ctx the parse tree
	 */
	void exitCopy(RTLParser.CopyContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#quantifier}.
	 * @param ctx the parse tree
	 */
	void enterQuantifier(RTLParser.QuantifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#quantifier}.
	 * @param ctx the parse tree
	 */
	void exitQuantifier(RTLParser.QuantifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#zeroOrOne}.
	 * @param ctx the parse tree
	 */
	void enterZeroOrOne(RTLParser.ZeroOrOneContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#zeroOrOne}.
	 * @param ctx the parse tree
	 */
	void exitZeroOrOne(RTLParser.ZeroOrOneContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#zeroOrMore}.
	 * @param ctx the parse tree
	 */
	void enterZeroOrMore(RTLParser.ZeroOrMoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#zeroOrMore}.
	 * @param ctx the parse tree
	 */
	void exitZeroOrMore(RTLParser.ZeroOrMoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#oneOrMore}.
	 * @param ctx the parse tree
	 */
	void enterOneOrMore(RTLParser.OneOrMoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#oneOrMore}.
	 * @param ctx the parse tree
	 */
	void exitOneOrMore(RTLParser.OneOrMoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#exactly}.
	 * @param ctx the parse tree
	 */
	void enterExactly(RTLParser.ExactlyContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#exactly}.
	 * @param ctx the parse tree
	 */
	void exitExactly(RTLParser.ExactlyContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#row}.
	 * @param ctx the parse tree
	 */
	void enterRow(RTLParser.RowContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#row}.
	 * @param ctx the parse tree
	 */
	void exitRow(RTLParser.RowContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#subrows}.
	 * @param ctx the parse tree
	 */
	void enterSubrows(RTLParser.SubrowsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#subrows}.
	 * @param ctx the parse tree
	 */
	void exitSubrows(RTLParser.SubrowsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#subrow}.
	 * @param ctx the parse tree
	 */
	void enterSubrow(RTLParser.SubrowContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#subrow}.
	 * @param ctx the parse tree
	 */
	void exitSubrow(RTLParser.SubrowContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#cells}.
	 * @param ctx the parse tree
	 */
	void enterCells(RTLParser.CellsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#cells}.
	 * @param ctx the parse tree
	 */
	void exitCells(RTLParser.CellsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#cell}.
	 * @param ctx the parse tree
	 */
	void enterCell(RTLParser.CellContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#cell}.
	 * @param ctx the parse tree
	 */
	void exitCell(RTLParser.CellContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#elements}.
	 * @param ctx the parse tree
	 */
	void enterElements(RTLParser.ElementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#elements}.
	 * @param ctx the parse tree
	 */
	void exitElements(RTLParser.ElementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#element}.
	 * @param ctx the parse tree
	 */
	void enterElement(RTLParser.ElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#element}.
	 * @param ctx the parse tree
	 */
	void exitElement(RTLParser.ElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#elementType}.
	 * @param ctx the parse tree
	 */
	void enterElementType(RTLParser.ElementTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#elementType}.
	 * @param ctx the parse tree
	 */
	void exitElementType(RTLParser.ElementTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#tags}.
	 * @param ctx the parse tree
	 */
	void enterTags(RTLParser.TagsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#tags}.
	 * @param ctx the parse tree
	 */
	void exitTags(RTLParser.TagsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#actions}.
	 * @param ctx the parse tree
	 */
	void enterActions(RTLParser.ActionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#actions}.
	 * @param ctx the parse tree
	 */
	void exitActions(RTLParser.ActionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#action}.
	 * @param ctx the parse tree
	 */
	void enterAction(RTLParser.ActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#action}.
	 * @param ctx the parse tree
	 */
	void exitAction(RTLParser.ActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#actionType}.
	 * @param ctx the parse tree
	 */
	void enterActionType(RTLParser.ActionTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#actionType}.
	 * @param ctx the parse tree
	 */
	void exitActionType(RTLParser.ActionTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#actionBody}.
	 * @param ctx the parse tree
	 */
	void enterActionBody(RTLParser.ActionBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#actionBody}.
	 * @param ctx the parse tree
	 */
	void exitActionBody(RTLParser.ActionBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#struct}.
	 * @param ctx the parse tree
	 */
	void enterStruct(RTLParser.StructContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#struct}.
	 * @param ctx the parse tree
	 */
	void exitStruct(RTLParser.StructContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#startText}.
	 * @param ctx the parse tree
	 */
	void enterStartText(RTLParser.StartTextContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#startText}.
	 * @param ctx the parse tree
	 */
	void exitStartText(RTLParser.StartTextContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#separator}.
	 * @param ctx the parse tree
	 */
	void enterSeparator(RTLParser.SeparatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#separator}.
	 * @param ctx the parse tree
	 */
	void exitSeparator(RTLParser.SeparatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#endText}.
	 * @param ctx the parse tree
	 */
	void enterEndText(RTLParser.EndTextContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#endText}.
	 * @param ctx the parse tree
	 */
	void exitEndText(RTLParser.EndTextContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#choice}.
	 * @param ctx the parse tree
	 */
	void enterChoice(RTLParser.ChoiceContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#choice}.
	 * @param ctx the parse tree
	 */
	void exitChoice(RTLParser.ChoiceContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#choiceBody}.
	 * @param ctx the parse tree
	 */
	void enterChoiceBody(RTLParser.ChoiceBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#choiceBody}.
	 * @param ctx the parse tree
	 */
	void exitChoiceBody(RTLParser.ChoiceBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterCond(RTLParser.CondContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitCond(RTLParser.CondContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#lookup}.
	 * @param ctx the parse tree
	 */
	void enterLookup(RTLParser.LookupContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#lookup}.
	 * @param ctx the parse tree
	 */
	void exitLookup(RTLParser.LookupContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#all}.
	 * @param ctx the parse tree
	 */
	void enterAll(RTLParser.AllContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#all}.
	 * @param ctx the parse tree
	 */
	void exitAll(RTLParser.AllContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#direction}.
	 * @param ctx the parse tree
	 */
	void enterDirection(RTLParser.DirectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#direction}.
	 * @param ctx the parse tree
	 */
	void exitDirection(RTLParser.DirectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#where}.
	 * @param ctx the parse tree
	 */
	void enterWhere(RTLParser.WhereContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#where}.
	 * @param ctx the parse tree
	 */
	void exitWhere(RTLParser.WhereContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#range}.
	 * @param ctx the parse tree
	 */
	void enterRange(RTLParser.RangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#range}.
	 * @param ctx the parse tree
	 */
	void exitRange(RTLParser.RangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#rowRange}.
	 * @param ctx the parse tree
	 */
	void enterRowRange(RTLParser.RowRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#rowRange}.
	 * @param ctx the parse tree
	 */
	void exitRowRange(RTLParser.RowRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#colRange}.
	 * @param ctx the parse tree
	 */
	void enterColRange(RTLParser.ColRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#colRange}.
	 * @param ctx the parse tree
	 */
	void exitColRange(RTLParser.ColRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#rangeBody}.
	 * @param ctx the parse tree
	 */
	void enterRangeBody(RTLParser.RangeBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#rangeBody}.
	 * @param ctx the parse tree
	 */
	void exitRangeBody(RTLParser.RangeBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(RTLParser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(RTLParser.StartContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#end}.
	 * @param ctx the parse tree
	 */
	void enterEnd(RTLParser.EndContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#end}.
	 * @param ctx the parse tree
	 */
	void exitEnd(RTLParser.EndContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#relative}.
	 * @param ctx the parse tree
	 */
	void enterRelative(RTLParser.RelativeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#relative}.
	 * @param ctx the parse tree
	 */
	void exitRelative(RTLParser.RelativeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#elementIndex}.
	 * @param ctx the parse tree
	 */
	void enterElementIndex(RTLParser.ElementIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#elementIndex}.
	 * @param ctx the parse tree
	 */
	void exitElementIndex(RTLParser.ElementIndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code strLiteral}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterStrLiteral(RTLParser.StrLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code strLiteral}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitStrLiteral(RTLParser.StrLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code strExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterStrExpr(RTLParser.StrExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code strExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitStrExpr(RTLParser.StrExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code thisExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterThisExpr(RTLParser.ThisExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code thisExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitThisExpr(RTLParser.ThisExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arithmExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterArithmExpr(RTLParser.ArithmExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arithmExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitArithmExpr(RTLParser.ArithmExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code funcExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterFuncExpr(RTLParser.FuncExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code funcExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitFuncExpr(RTLParser.FuncExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code propExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterPropExpr(RTLParser.PropExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code propExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitPropExpr(RTLParser.PropExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intLiteral}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIntLiteral(RTLParser.IntLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intLiteral}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIntLiteral(RTLParser.IntLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBinaryExpr(RTLParser.BinaryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBinaryExpr(RTLParser.BinaryExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code boolLiteral}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBoolLiteral(RTLParser.BoolLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code boolLiteral}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBoolLiteral(RTLParser.BoolLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParenExpr(RTLParser.ParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParenExpr(RTLParser.ParenExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code hexLiteral}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterHexLiteral(RTLParser.HexLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code hexLiteral}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitHexLiteral(RTLParser.HexLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code notExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNotExpr(RTLParser.NotExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code notExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNotExpr(RTLParser.NotExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code compExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCompExpr(RTLParser.CompExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code compExpr}
	 * labeled alternative in {@link RTLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCompExpr(RTLParser.CompExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#compOp}.
	 * @param ctx the parse tree
	 */
	void enterCompOp(RTLParser.CompOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#compOp}.
	 * @param ctx the parse tree
	 */
	void exitCompOp(RTLParser.CompOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#binaryOp}.
	 * @param ctx the parse tree
	 */
	void enterBinaryOp(RTLParser.BinaryOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#binaryOp}.
	 * @param ctx the parse tree
	 */
	void exitBinaryOp(RTLParser.BinaryOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#func}.
	 * @param ctx the parse tree
	 */
	void enterFunc(RTLParser.FuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#func}.
	 * @param ctx the parse tree
	 */
	void exitFunc(RTLParser.FuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#arg}.
	 * @param ctx the parse tree
	 */
	void enterArg(RTLParser.ArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#arg}.
	 * @param ctx the parse tree
	 */
	void exitArg(RTLParser.ArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#prop}.
	 * @param ctx the parse tree
	 */
	void enterProp(RTLParser.PropContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#prop}.
	 * @param ctx the parse tree
	 */
	void exitProp(RTLParser.PropContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#bool}.
	 * @param ctx the parse tree
	 */
	void enterBool(RTLParser.BoolContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#bool}.
	 * @param ctx the parse tree
	 */
	void exitBool(RTLParser.BoolContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#arithmOp}.
	 * @param ctx the parse tree
	 */
	void enterArithmOp(RTLParser.ArithmOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#arithmOp}.
	 * @param ctx the parse tree
	 */
	void exitArithmOp(RTLParser.ArithmOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link RTLParser#strOp}.
	 * @param ctx the parse tree
	 */
	void enterStrOp(RTLParser.StrOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link RTLParser#strOp}.
	 * @param ctx the parse tree
	 */
	void exitStrOp(RTLParser.StrOpContext ctx);
}