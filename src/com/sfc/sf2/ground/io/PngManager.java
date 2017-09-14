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
public class PngManager {
    
    private static final String BASE_FILENAME = "groundXX.png";
    
    public static Ground[] importPng(String basepath){
        System.out.println("com.sfc.sf2.ground.io.PngManager.importPng() - Importing PNG files ...");
        List<Ground> grounds = new ArrayList();
        try{
            for(int i=0;i<256;i++){
                String index = String.format("%02d", i);
                String filePath = basepath + BASE_FILENAME.replace("XX.png", index+".png");
                Tile[] tiles = loadPngFile(filePath);
                if(tiles!=null){
                    if(tiles.length==384){
                       Ground ground = new Ground();
                       ground.setIndex(i);
                       System.arraycopy(tiles, 0, tiles, 0, tiles.length);                   
                       ground.setTiles(tiles);
                       grounds.add(ground);
                       System.out.println("Created Ground " + i + " with " + tiles.length + " tiles.");                       
                    }else{
                        System.out.println("Could not create Ground " + i + " because of wrong length : tiles=" + tiles.length);
                    }
                }
            }
        }catch(Exception e){
             System.err.println("com.sfc.sf2.ground.io.PngManager.importPng() - Error while parsing graphics data : "+e);
        }        
        System.out.println("com.sfc.sf2.ground.io.PngManager.importPng() - PNG files imported.");
        return grounds.toArray(new Ground[grounds.size()]);                
    }
    
    
    
    public static Tile[] loadPngFile(String filepath) throws IOException{
        Tile[] tiles = null;
        try{
            Path path = Paths.get(filepath);
            if(path.toFile().exists()){
                BufferedImage img = ImageIO.read(path.toFile());
                ColorModel cm = img.getColorModel();
                if(!(cm instanceof IndexColorModel)){
                    System.out.println("PNG FORMAT ERROR : COLORS ARE NOT INDEXED AS EXPECTED.");
                }else{
                    IndexColorModel icm = (IndexColorModel) cm;
                    Color[] palette = buildColors(icm);

                    int imageWidth = img.getWidth();
                    int imageHeight = img.getHeight();
                    if(imageWidth%8!=0 || imageHeight%8!=0){
                        System.out.println("PNG FORMAT WARNING : DIMENSIONS ARE NOT MULTIPLES OF 8. (8 pixels per tile)");
                    }else{
                        tiles = new Tile[(imageWidth/8)*(imageHeight/8)];
                        int tileId = 0;
                        for(int screenHalf=0;screenHalf<2;screenHalf++){
                            for(int blockColumn=0;blockColumn<4;blockColumn++){
                                for(int blockLine=0;blockLine<3;blockLine++){
                                    for(int tileColumn=0;tileColumn<4;tileColumn++){
                                        for(int tileLine=0;tileLine<4;tileLine++){
                                            int x = (screenHalf*16+blockColumn*4+tileColumn)*8;
                                            int y = (blockLine*4+tileLine)*8;
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
                }                
            }
        }catch(Exception e){
             System.err.println("com.sfc.sf2.ground.io.PngManager.importPng() - Error while parsing PNG data : "+e);
             throw e;
        }                
        return tiles;                
    }
    
    private static Color[] buildColors(IndexColorModel icm){
        Color[] colors = new Color[16];
        if(icm.getMapSize()>16){
            System.out.println("com.sfc.sf2.ground.io.PngManager.buildColors() - PNG FORMAT HAS MORE THAN 16 INDEXED COLORS : "+icm.getMapSize());
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
    
    
    public static void exportPng(Ground[] grounds, String basepath){
        try {
            //System.out.println("com.sfc.sf2.ground.io.PngManager.exportPng() - Exporting PNG files ...");
            for(Ground ground : grounds){
                String index = String.format("%02d", ground.getIndex());
                String filePath = basepath + System.getProperty("file.separator") + BASE_FILENAME.replace("XX.png", index+".png");
                writePngFile(ground.getTiles(),filePath);                
            }
            //System.out.println("com.sfc.sf2.ground.io.PngManager.exportPng() - PNG files exported.");
        } catch (Exception ex) {
            Logger.getLogger(PngManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                
    }    
    
    public static void writePngFile(Tile[] tiles, String filepath){
        try {
            //System.out.println("com.sfc.sf2.ground.io.PngManager.exportPng() - Exporting PNG file ...");
            BufferedImage image = GroundLayout.buildImage(tiles, 32, true);
            File outputfile = new File(filepath);
            //System.out.println("File path : "+outputfile.getAbsolutePath());
            ImageIO.write(image, "png", outputfile);
            System.out.println("PNG file exported : " + outputfile.getAbsolutePath());
        } catch (Exception ex) {
            Logger.getLogger(PngManager.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
       
    
}
