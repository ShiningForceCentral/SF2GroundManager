/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.ground;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.palette.Palette;

/**
 *
 * @author wiz
 */
public class Ground {
    
    private int index;
    
    private Tile[] tiles;
    
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }  

    public Tile[] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }
    
    public Palette getPalette() {
        if (tiles == null || tiles.length == 0) {
            return null;
        }
        return tiles[0].getPalette();
    }
}
