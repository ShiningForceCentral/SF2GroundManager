/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.ground.layout;

import com.sfc.sf2.graphics.Tile;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import javax.swing.JPanel;

/**
 *
 * @author wiz
 */
public class GroundLayout extends JPanel {
    
    private static final int DEFAULT_TILES_PER_ROW = 32;
    
    private int tilesPerRow = DEFAULT_TILES_PER_ROW;
    private Tile[] tiles;
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);   
        g.drawImage(buildImage(), 0, 0, this);       
    }
    
    public BufferedImage buildImage(){
        BufferedImage image = buildImage(this.tiles,this.tilesPerRow, false);
        setSize(image.getWidth(), image.getHeight());
        return image;
    }
    
    public static BufferedImage buildImage(Tile[] tiles, int tilesPerRow, boolean pngExport){
        int imageHeight = (tiles.length/tilesPerRow)*8;
        if(tiles.length%tilesPerRow!=0){
            imageHeight+=8;
        }
        BufferedImage image;
        if(pngExport){
            IndexColorModel icm = buildIndexColorModel(tiles[0].getPalette());
            image = new BufferedImage(tilesPerRow*8, imageHeight , BufferedImage.TYPE_BYTE_BINARY, icm);
        } else{
            image = new BufferedImage(tilesPerRow*8, imageHeight , BufferedImage.TYPE_INT_RGB);
        }
        Graphics graphics = image.getGraphics();
        for(int groundIndex=0;groundIndex<(tiles.length/(12*32));groundIndex++){         
            /*
                1  5  9 13 49 53                  193 197
                2  6 10 14 50  .                  194   .
                3  7 11 15 51  .                  195   .
                4  8 12 16 52  .                  196   .
               17 21 25 29  
               18 22 26 30
               19 23 27 31
               20 24 28 32
               33 37 41 45                  . 189                    . 381
               34 38 42 46                  . 190                    . 382
               35 39 43 47                  . 191                    . 383
               36 40 44 48                188 192                  380 384
            */
            
            /* Loop on grounds, then 2 screen halves, then 4 block columns, then 3 block lines, then 4 tile columns, then 4 tile lines */
            
            for(int screenHalf=0;screenHalf<2;screenHalf++){
                for(int blockColumn=0;blockColumn<4;blockColumn++){
                    for(int blockLine=0;blockLine<3;blockLine++){
                        for(int tileColumn=0;tileColumn<4;tileColumn++){
                            for(int tileLine=0;tileLine<4;tileLine++){
                                graphics.drawImage(tiles[(groundIndex*12*32)+(screenHalf*16*12)+(blockColumn*12*4)+(blockLine*4*4)+(tileColumn*4)+tileLine].getImage(), (screenHalf*16+blockColumn*4+tileColumn)*8, (groundIndex*12+blockLine*4+tileLine)*8, null);
                            }
                        }
                    }
                }
            }
        }
        return image;
    }
    
    private static IndexColorModel buildIndexColorModel(Color[] colors){
        byte[] reds = new byte[16];
        byte[] greens = new byte[16];
        byte[] blues = new byte[16];
        byte[] alphas = new byte[16];
        reds[0] = (byte)0xFF;
        greens[0] = (byte)0xFF;
        blues[0] = (byte)0xFF;
        alphas[0] = 0;
        for(int i=0;i<16;i++){
            reds[i] = (byte)colors[i].getRed();
            greens[i] = (byte)colors[i].getGreen();
            blues[i] = (byte)colors[i].getBlue();
            alphas[i] = (byte)0xFF;
        }
        IndexColorModel icm = new IndexColorModel(4,16,reds,greens,blues,alphas);
        return icm;
    }     
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getHeight());
    }
    
        public Tile[] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }
    
    public int getTilesPerRow() {
        return tilesPerRow;
    }

    public void setTilesPerRow(int tilesPerRow) {
        this.tilesPerRow = tilesPerRow;
    }
    
}
