package com.gc;

import java.awt.*;

public class Cell extends Vertex {
	
	private int width;
	private int height;
	private Color color;
	private boolean isWall;
	private int row;

	public boolean isWall() {
		return isWall;
	}

	public void setWall(boolean wall) {
		isWall = wall;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	private int column;


	
	public Cell(Point position, int width, int height, int row, int column){
		super(position);
		this.width = width;
		this.height = height;	
		this.color = Color.GRAY;
		this.isWall = true;
		this.row = row;
		this.column = column;
	}
	public Cell(Point position, int width, int height, int row, int column, boolean a, Color b){
		super(position);
		this.width = width;
		this.height = height;
		this.color = b;
		this.isWall = a;
		this.row = row;
		this.column = column;
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
	public Color getColor(){
		return this.color;
	}
	
	public void draw(Graphics g){
		g.setColor(color);
		g.fillRect(position.x, position.y, width, height);
		g.setColor(Color.BLACK);
		g.drawRect(position.x, position.y, width, height);
		
	}
}
