/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.ground.io;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.layout.DefaultLayout;
import com.sfc.sf2.ground.Ground;
import com.sfc.sf2.ground.layout.GroundLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author wiz
 */
public class GifManager {
    
    public static Ground importGif(String filepath){
        System.out.println("com.sfc.sf2.ground.io.GifManager.importGif() - Importing GIF file ...");
        Ground ground = null;
        try{
                Tile[] tiles = loadGifFile(filepath);
                if(tiles!=null){
                    if(tiles.length==48){
                       ground = new Ground();
                       System.arraycopy(tiles, 0, tiles, 0, tiles.length);                   
                       ground.setTiles(tiles);
                       System.out.println("Created Ground with " + tiles.length + " tiles.");                       
                    }else{
                        System.out.println("Could not create Ground because of wrong length : tiles=" + tiles.length);
                    }
                }
        }catch(Exception e){
             System.err.println("com.sfc.sf2.ground.io.GifManager.importGif() - Error while parsing graphics data : "+e);
        }        
        System.out.println("com.sfc.sf2.ground.io.GifManager.importGif() - GIF file imported.");
        return ground;                
    }
    
    
    
    public static Tile[] loadGifFile(String filepath) throws IOException{
        Tile[] tiles = null;
        try{
            Path path = Paths.get(filepath);
            if(path.toFile().exists()){
                BufferedImage img = ImageIO.read(path.toFile());
                ColorModel cm = img.getColorModel();
                if(!(cm instanceof IndexColorModel)){
                    System.out.println("GIF FORMAT ERROR : COLORS ARE NOT INDEXED AS EXPECTED.");
                }else{
                    IndexColorModel icm = (IndexColorModel) cm;
                    Color[] palette = buildColors(icm);

                    int imageWidth = img.getWidth();
                    int imageHeight = img.getHeight();
                    if(imageWidth%8!=0 || imageHeight%8!=0){
                        System.out.println("GIF FORMAT WARNING : DIMENSIONS ARE NOT MULTIPLES OF 8. (8 pixels per tile)");
                    }else{
                        tiles = new Tile[(imageWidth/8)*(imageHeight/8)];
                        int tileId = 0;
                            for(int blockColumn=0;blockColumn<3;blockColumn++){
                                    for(int tileColumn=0;tileColumn<4;tileColumn++){
                                        for(int tileLine=0;tileLine<4;tileLine++){
                                            int x = (blockColumn*4+tileColumn)*8;
                                            int y = (tileLine)*8;
                                            //System.out.println("Building tile from coordinates "+x+":"+y);
                                            Tile tile = new Tile();
                                            tile.setId(tileId);
                                            tile.setPalette(palette);
                                            for(int j=0;j<8;j++){
                                                for(int i=0;i<8;i++){
                                                    Color color = new Color(img.getRGB(x+i,y+j));
                                                    for(int c=0;c<16;c++){
                                                        if(color.equals(palette[c])){
                                                            tile.setPixel(i, j, c);
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            //System.out.println(tile);
                                            tiles[tileId] = tile;   
                                            tileId++;
                                        }
                                    }
                            }                  

                    }
                }                
            }
        }catch(Exception e){
             System.err.println("com.sfc.sf2.ground.io.GifManager.importGif() - Error while parsing GIF data : "+e);
             throw e;
        }                
        return tiles;                
    }
    
    private static Color[] buildColors(IndexColorModel icm){
        Color[] colors = new Color[16];
        if(icm.getMapSize()>16){
            System.out.println("com.sfc.sf2.ground.io.GifManager.buildColors() - GIF FORMAT HAS MORE THAN 16 INDEXED COLORS : "+icm.getMapSize());
        }
        byte[] reds = new byte[icm.getMapSize()];
        byte[] greens = new byte[icm.getMapSize()];
        byte[] blues = new byte[icm.getMapSize()];
        icm.getReds(reds);
        icm.getGreens(greens);
        icm.getBlues(blues);
        for(int i=0;i<16;i++){
            colors[i] = new Color((int)(reds[i]&0xff),(int)(greens[i]&0xff),(int)(blues[i]&0xff));
        }
        return colors;
    }
    
    
    public static void exportGif(Ground ground, String filepath){
        try {
            //System.out.println("com.sfc.sf2.ground.io.GifManager.exportGif() - Exporting GIF files ...");
            writeGifFile(ground.getTiles(),filepath);    
            //System.out.println("com.sfc.sf2.ground.io.GifManager.exportGif() - GIF files exported.");
        } catch (Exception ex) {
            Logger.getLogger(GifManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                
    }    
    
    public static void writeGifFile(Tile[] tiles, String filepath){
        try {
            //System.out.println("com.sfc.sf2.ground.io.GifManager.exportGif() - Exporting GIF file ...");
            BufferedImage image = GroundLayout.buildImage(tiles, 12, true);
            File outputfile = new File(filepath);
            //System.out.println("File path : "+outputfile.getAbsolutePath());
            ImageIO.write(image, "gif", outputfile);
            System.out.println("GIF file exported : " + outputfile.getAbsolutePath());
        } catch (Exception ex) {
            Logger.getLogger(GifManager.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
       
    
}
