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
import com.sfc.sf2.ground.io.GifManager;
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
    private Ground ground;
       
    public void importDisassembly(String basePalettePath, String palettePath, String graphicsPath){
        System.out.println("com.sfc.sf2.ground.GroundManager.importDisassembly() - Importing disassembly ...");
        ground = DisassemblyManager.importDisassembly(basePalettePath, palettePath, graphicsPath);
        tiles = ground.getTiles();
        graphicsManager.setTiles(tiles);
        System.out.println("com.sfc.sf2.ground.GroundManager.importDisassembly() - Disassembly imported.");
    }
    
    public void exportDisassembly(String palettePath, String graphicsPath){
        System.out.println("com.sfc.sf2.ground.GroundManager.importDisassembly() - Exporting disassembly ...");
        DisassemblyManager.exportDisassembly(ground, palettePath, graphicsPath);
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
        ground = PngManager.importPng(basepath);
        tiles = ground.getTiles();
        graphicsManager.setTiles(tiles);
        System.out.println("com.sfc.sf2.ground.GroundManager.importPng() - PNG imported.");
    }
    
    public void exportPng(String filepath){
        System.out.println("com.sfc.sf2.ground.GroundManager.exportPng() - Exporting PNG ...");
        PngManager.exportPng(ground, filepath);
        System.out.println("com.sfc.sf2.ground.GroundManager.exportPng() - PNG exported.");       
    } 

    public void importGif(String basepath){
        System.out.println("com.sfc.sf2.ground.GroundManager.importGif() - Importing GIF ...");
        ground = GifManager.importGif(basepath);
        tiles = ground.getTiles();
        graphicsManager.setTiles(tiles);
        System.out.println("com.sfc.sf2.ground.GroundManager.importGif() - GIF imported.");
    }
    
    public void exportGif(String filepath){
        System.out.println("com.sfc.sf2.ground.GroundManager.exportGif() - Exporting GIF ...");
        GifManager.exportGif(ground, filepath);
        System.out.println("com.sfc.sf2.ground.GroundManager.exportGif() - GIF exported.");       
    }
    
    public Tile[] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }

    public Ground getGround() {
        return ground;
    }

    public void setGround(Ground ground) {
        this.ground = ground;
    }

}
