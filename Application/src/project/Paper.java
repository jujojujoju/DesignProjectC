package project;


import java.util.ArrayList;
import java.util.List;

public class Paper extends Node{
	private int year;
	private String paperKey;

	public Paper(){
		year = -1;
		paperKey = "";
	}

	public Paper(String paper) {
		this.setName(paper);
	}


	public Paper(String name, String year) {
		this.setName(name);
		this.setYear(Integer.parseInt(year));
	}

	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getPaperKey() {
		return paperKey;
	}
	public void setPaperKey(String paperKey) {
		this.paperKey = paperKey;
	}

	@Override
	public String toString() {
		return "Paper {year=" + year + ", paperKey=" + paperKey + ", getName()=" + getName() + "}";
	}

}
