/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.ground.layout;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.ground.Ground;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author TiMMy
 */
public class GroundLayout extends JPanel {
    
    private static final int TILES_PER_ROW = 12;
    
    private Ground ground;
    
    private BufferedImage currentImage;
    private boolean redraw = true;
    
    private int displaySize = 1;
    private boolean showGrid = true;
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(buildImage(), 0, 0, this);       
    }
    
    public BufferedImage buildImage() {
        if (redraw) {
            if (ground == null || ground.getTiles() == null) {
                currentImage = null;
            } else {
                currentImage = buildImage(ground);
                currentImage = resize(currentImage);
                setSize(currentImage.getWidth(), currentImage.getHeight());
                if (showGrid) { drawGrid(currentImage); }
            }
        }
        return currentImage;
    }
    
    public static BufferedImage buildImage(Ground ground) {
        Tile[] tiles = ground.getTiles();
        int imageHeight = (tiles.length/TILES_PER_ROW);
        if (tiles.length % TILES_PER_ROW != 0) {
            imageHeight++;
        }
        int imageWidth = TILES_PER_ROW;
        BufferedImage image = new BufferedImage(imageWidth*8, imageHeight*8, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                if (x+y*TILES_PER_ROW >= tiles.length)
                    break;
                graphics.drawImage(tiles[x+y*TILES_PER_ROW].getIndexedColorImage(), x*8, y*8, null);
            }
        }
        graphics.dispose();
        return image;
    }
    
    private void drawGrid(BufferedImage image) {
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.BLACK);
        int x = 0;
        int y = 0;
        while (x < image.getWidth()) {
            graphics.drawLine(x, 0, x, image.getHeight());
            x += 8*displaySize;
        }
        graphics.drawLine(x-1, 0, x-1, image.getHeight());
        while (y < image.getHeight()) {
            graphics.drawLine(0, y, image.getWidth(), y);
            y += 8*displaySize;
        }
        graphics.drawLine(0, y-1, image.getWidth(), y-1);
        graphics.dispose();
    }
    
    public void resize(int size){
        this.displaySize = size;
        currentImage = resize(currentImage);
    }
    
    private BufferedImage resize(BufferedImage image) {
        if (displaySize == 1)
            return image;
        BufferedImage newImage = new BufferedImage(image.getWidth()*displaySize, image.getHeight()*displaySize, BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.getGraphics();
        g.drawImage(image, 0, 0, image.getWidth()*displaySize, image.getHeight()*displaySize, null);
        g.dispose();
        return newImage;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getHeight());
    }
    
    public Ground getGround() {
        return ground;
    }

    public void setGround(Ground ground) {
        this.ground = ground;
        redraw = true;
    }

    public int getDisplaySize() {
        return displaySize;
    }

    public void setDisplaySize(int displaySize) {
        this.displaySize = displaySize;
        redraw = true;
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        redraw = true;
    }
}
