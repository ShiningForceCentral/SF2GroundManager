/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.ground;

import com.sfc.sf2.graphics.GraphicsManager;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.ground.io.DisassemblyManager;
import com.sfc.sf2.ground.io.PngManager;
import com.sfc.sf2.palette.PaletteManager;
import java.awt.Color;

/**
 *
 * @author wiz
 */
public class GroundManager {
       
    private PaletteManager paletteManager = new PaletteManager();
    private GraphicsManager graphicsManager = new GraphicsManager();
    private Tile[] tiles;
    private Ground[] grounds;

    public Tile[] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }
       
    public void importDisassembly(String graphicsBasepath){
        System.out.println("com.sfc.sf2.ground.GroundManager.importDisassembly() - Importing disassembly ...");
        grounds = DisassemblyManager.importDisassembly(graphicsBasepath);
        tiles = new Tile[grounds.length*32*12];
        for(int i=0;i<grounds.length;i++){
            System.arraycopy(grounds[i].getTiles(), 0, tiles, i*32*12, 32*12);
        }
        graphicsManager.setTiles(tiles);
        System.out.println("com.sfc.sf2.ground.GroundManager.importDisassembly() - Disassembly imported.");
    }
    
    public void exportDisassembly(String basepath){
        System.out.println("com.sfc.sf2.ground.GroundManager.importDisassembly() - Exporting disassembly ...");
        DisassemblyManager.exportDisassembly(grounds, basepath);
        System.out.println("com.sfc.sf2.ground.GroundManager.importDisassembly() - Disassembly exported.");        
    }   
    
    public void importRom(String romFilePath, String paletteOffset, String paletteLength, String graphicsOffset, String graphicsLength){
        System.out.println("com.sfc.sf2.ground.GroundManager.importOriginalRom() - Importing original ROM ...");
        graphicsManager.importRom(romFilePath, paletteOffset, paletteLength, graphicsOffset, graphicsLength,GraphicsManager.COMPRESSION_BASIC);
        tiles = graphicsManager.getTiles();
        System.out.println("com.sfc.sf2.ground.GroundManager.importOriginalRom() - Original ROM imported.");
    }
    
    public void exportRom(String originalRomFilePath, String graphicsOffset){
        System.out.println("com.sfc.sf2.ground.GroundManager.exportOriginalRom() - Exporting original ROM ...");
        graphicsManager.exportRom(originalRomFilePath, graphicsOffset, GraphicsManager.COMPRESSION_BASIC);
        System.out.println("com.sfc.sf2.ground.GroundManager.exportOriginalRom() - Original ROM exported.");        
    }      
    
    public void importPng(String basepath){
        System.out.println("com.sfc.sf2.ground.GroundManager.importPng() - Importing PNG ...");
        grounds = PngManager.importPng(basepath);
        tiles = new Tile[grounds.length*384];
        for(int i=0;i<grounds.length;i++){
            System.arraycopy(grounds[i].getTiles(), 0, tiles, i*384, 384);
        }
        graphicsManager.setTiles(tiles);
        System.out.println("com.sfc.sf2.ground.GroundManager.importPng() - PNG imported.");
    }
    
    public void exportPng(String basepath){
        System.out.println("com.sfc.sf2.ground.GroundManager.exportPng() - Exporting PNG ...");
        PngManager.exportPng(grounds, basepath);
        System.out.println("com.sfc.sf2.ground.GroundManager.exportPng() - PNG exported.");       
    }
}
