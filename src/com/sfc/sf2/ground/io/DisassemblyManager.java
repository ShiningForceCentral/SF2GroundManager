/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.ground.io;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.compressed.BasicGraphicsDecoder;
import com.sfc.sf2.graphics.compressed.BasicGraphicsEncoder;
import com.sfc.sf2.ground.Ground;
import com.sfc.sf2.graphics.compressed.StackGraphicsDecoder;
import com.sfc.sf2.graphics.compressed.StackGraphicsEncoder;
import com.sfc.sf2.palette.graphics.PaletteDecoder;
import com.sfc.sf2.palette.graphics.PaletteEncoder;
import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wiz
 */
public class DisassemblyManager {

    private static final String BASE_FILENAME = "groundXX.bin";
    private static final int GROUND_TILE_LENGTH = 48;
    
    public static Ground importDisassembly(String basePalettePath, String palettePath, String graphicsPath){
        System.out.println("com.sfc.sf2.ground.io.DisassemblyManager.importDisassembly() - Importing disassembly ...");
        Ground ground = null;
        try{
            Tile[] tiles = parseGraphics(basePalettePath, palettePath, graphicsPath);
            if(tiles!=null){
                if(tiles.length==GROUND_TILE_LENGTH){
                   ground = new Ground();   
                   ground.setTiles(tiles);
                   System.out.println("Created Ground with " + tiles.length + " tiles.");                       
                }else{
                    System.out.println("Could not create Ground because of wrong length : tiles=" + tiles.length);
                }
            }
        }catch(Exception e){
             System.err.println("com.sfc.sf2.ground.io.PngManager.importPng() - Error while parsing graphics data : "+e);
        }         
                
        System.out.println("com.sfc.sf2.ground.io.DisassemblyManager.importDisassembly() - Disassembly imported.");
        return ground;
    }
    
    public static void exportDisassembly(Ground ground, String palettePath, String graphicsPath){
        System.out.println("com.sfc.sf2.ground.io.DisassemblyManager.exportDisassembly() - Exporting disassembly ...");
        try {
            Tile[] tileset = ground.getTiles();
            StackGraphicsEncoder.produceGraphics(tileset);
            byte[] newGroundFileBytes = StackGraphicsEncoder.getNewGraphicsFileBytes();
            Path graphicsFilePath = Paths.get(graphicsPath);
            Files.write(graphicsFilePath,newGroundFileBytes);
            if(palettePath!=null && !palettePath.isEmpty()){
                Color[] wholePalette = tileset[0].getPalette();
                Color[] groundPalette = new Color[3];
                groundPalette[0] = wholePalette[3];
                groundPalette[1] = wholePalette[4];
                groundPalette[2] = wholePalette[8];
                PaletteEncoder.producePalette(groundPalette);
                byte[] palette = PaletteEncoder.getNewPaletteFileBytes();
                Path paletteFilePath = Paths.get(palettePath);
                Files.write(paletteFilePath,palette);
            }
            System.out.println(newGroundFileBytes.length + " bytes into " + graphicsFilePath);                
        } catch (Exception ex) {
            Logger.getLogger(DisassemblyManager.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            System.out.println(ex);
        }            
        System.out.println("com.sfc.sf2.ground.io.DisassemblyManager.exportDisassembly() - Disassembly exported.");        
    }    
    
    private static Tile[] parseGraphics(String basePalettePath, String palettePath, String graphicsPath){
        Tile[] tiles = null;
        Color[] palette = null;
        try{
            Path basepalettepath = Paths.get(basePalettePath);
            Path palettepath = Paths.get(palettePath);
            Path graphicspath = Paths.get(graphicsPath);
            if(basepalettepath.toFile().exists() && palettepath.toFile().exists() && graphicspath.toFile().exists()){
                byte[] basePaletteData = Files.readAllBytes(basepalettepath);
                byte[] paletteData = Files.readAllBytes(palettepath);
                basePaletteData[6] = paletteData[0];
                basePaletteData[7] = paletteData[1];
                basePaletteData[8] = paletteData[2];
                basePaletteData[9] = paletteData[3];
                basePaletteData[16] = paletteData[4];
                basePaletteData[17] = paletteData[5];
                byte[] graphicsData = Files.readAllBytes(graphicspath);
                if(paletteData.length == 6 && graphicsData.length>2){
                    palette = PaletteDecoder.parsePalette(basePaletteData);
                    palette[0] = new Color(255, 255, 255, 0);
                    tiles = new StackGraphicsDecoder().decodeStackGraphics(graphicsData, palette);
                }else{
                    System.out.println("com.sfc.sf2.ground.io.DisassemblyManager.parseGraphics() - File ignored because of too small length (must be a dummy file) " + graphicsData.length + " : " + graphicsPath);
                }
            }            
        }catch(Exception e){
             System.err.println("com.sfc.sf2.ground.io.DisassemblyManager.parseGraphics() - Error while parsing graphics data : "+e);
             e.printStackTrace();
        } 
        return tiles;
    }   
    
    private static short getNextWord(byte[] data, int cursor){
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(data[cursor+1]);
        bb.put(data[cursor]);
        short s = bb.getShort(0);
        return s;
    }

    
}
