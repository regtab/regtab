package com.regtab.core.model;

/**
 * The Range record represents four positions of a source cell:
 * rt -- top row; rb -- bottom row; cl -- column left; cr -- column right
 */
public record CellPos(int rt, int rb, int cl, int cr) {
}
