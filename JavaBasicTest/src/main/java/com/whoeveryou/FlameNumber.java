package com.whoeveryou;

public class FlameNumber {

	private int x1;
	private int x2;
	
	public FlameNumber(int x1,int x2){
		this.x1=x1;
		this.x2=x2;
	}
	public int getX1() {
		return x1;
	}
	public void setX1(int x1) {
		this.x1 = x1;
	}
	public int getX2() {
		return x2;
	}
	public void setX2(int x2) {
		this.x2 = x2;
	}
	public int addUp(){
		return x1+x2;
	}
	
}