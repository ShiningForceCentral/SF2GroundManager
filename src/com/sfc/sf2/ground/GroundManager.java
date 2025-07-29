/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.ground;

import com.sfc.sf2.graphics.GraphicsManager;
import com.sfc.sf2.ground.io.DisassemblyManager;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.PaletteManager;

/**
 *
 * @author wiz
 */
public class GroundManager {
       
    private final PaletteManager paletteManager = new PaletteManager();
    private final GraphicsManager graphicsManager = new GraphicsManager();
    
    private Palette basePalette;
    private Ground ground;
       
    public void importDisassembly(String basePalettePath, String palettePath, String graphicsPath){
        System.out.println("com.sfc.sf2.ground.GroundManager.importDisassembly() - Importing disassembly ...");
        importBasePalette(basePalettePath);
        ground = DisassemblyManager.importDisassembly(basePalettePath, palettePath, graphicsPath);
        Palette palette = ground.getPalette();
        adjustImportedPalette(basePalette, palette);
        System.out.println("com.sfc.sf2.ground.GroundManager.importDisassembly() - Disassembly imported.");
    }
    
    public void exportPalette(String palettePath){
        System.out.println("com.sfc.sf2.ground.GroundManager.importDisassembly() - Exporting disassembly ...");
        DisassemblyManager.exportPalette(ground.getPalette(), palettePath);
        System.out.println("com.sfc.sf2.ground.GroundManager.importDisassembly() - Disassembly exported.");        
    }
    
    public void exportDisassembly(String graphicsPath){
        System.out.println("com.sfc.sf2.ground.GroundManager.importDisassembly() - Exporting disassembly ...");
        DisassemblyManager.exportDisassembly(ground, graphicsPath);
        System.out.println("com.sfc.sf2.ground.GroundManager.importDisassembly() - Disassembly exported.");        
    }
    
    public void importRom(String basePalettePath, String romFilePath, String paletteOffset, String paletteLength, String graphicsOffset, String graphicsLength){
        System.out.println("com.sfc.sf2.ground.GroundManager.importOriginalRom() - Importing original ROM ...");
        importBasePalette(basePalettePath);
        graphicsManager.importRom(romFilePath, paletteOffset, paletteLength, graphicsOffset, graphicsLength,GraphicsManager.COMPRESSION_BASIC);
        ground = new Ground();
        ground.setTiles(graphicsManager.getTiles());
        Palette palette = ground.getPalette();
        adjustImportedPalette(basePalette, palette);
        System.out.println("com.sfc.sf2.ground.GroundManager.importOriginalRom() - Original ROM imported.");
    }
    
    public void exportRom(String originalRomFilePath, String graphicsOffset){
        System.out.println("com.sfc.sf2.ground.GroundManager.exportOriginalRom() - Exporting original ROM ...");
        graphicsManager.exportRom(originalRomFilePath, graphicsOffset, GraphicsManager.COMPRESSION_BASIC);
        System.out.println("com.sfc.sf2.ground.GroundManager.exportOriginalRom() - Original ROM exported.");        
    }      

    public void importPng(String basePalettePath, String basepath){
        System.out.println("com.sfc.sf2.ground.GroundManager.importPng() - Importing PNG ...");
        importBasePalette(basePalettePath);
        graphicsManager.importPng(basepath);
        ground = new Ground();
        ground.setTiles(graphicsManager.getTiles());
        Palette palette = ground.getPalette();
        adjustImportedPalette(basePalette, palette);
        System.out.println("com.sfc.sf2.ground.GroundManager.importPng() - PNG imported.");
    }
    
    public void exportPng(String filepath){
        System.out.println("com.sfc.sf2.ground.GroundManager.exportPng() - Exporting PNG ...");
        graphicsManager.setTiles(ground.getTiles());
        graphicsManager.exportPng(filepath, 12);
        System.out.println("com.sfc.sf2.ground.GroundManager.exportPng() - PNG exported.");       
    } 

    public void importGif(String basePalettePath, String basepath){
        System.out.println("com.sfc.sf2.ground.GroundManager.importGif() - Importing GIF ...");
        importBasePalette(basePalettePath);
        graphicsManager.importGif(basepath);
        ground = new Ground();
        ground.setTiles(graphicsManager.getTiles());
        Palette palette = ground.getPalette();
        adjustImportedPalette(basePalette, palette);
        System.out.println("com.sfc.sf2.ground.GroundManager.importGif() - GIF imported.");
    }
    
    public void exportGif(String filepath){
        System.out.println("com.sfc.sf2.ground.GroundManager.exportGif() - Exporting GIF ...");
        graphicsManager.setTiles(ground.getTiles());
        graphicsManager.exportGif(filepath, 12);
        System.out.println("com.sfc.sf2.ground.GroundManager.exportGif() - GIF exported.");       
    }
    
    private void importBasePalette(String palettePath) {        
        if (basePalette == null) {
            paletteManager.importDisassembly(palettePath);
            basePalette = paletteManager.getPalette();
        }
    }
    
    private static void adjustImportedPalette(Palette basePalette, Palette importedPalette) {
        for (int i = 0; i < basePalette.getColors().length; i++) {
            if (i != 9 && i != 13 && i != 14)
                importedPalette.getColors()[i] = basePalette.getColors()[i];
        }
    }

    public Ground getGround() {
        return ground;
    }

    public void setGround(Ground ground) {
        this.ground = ground;
    }
}
