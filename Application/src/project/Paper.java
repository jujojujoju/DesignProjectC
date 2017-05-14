package project;


import java.util.ArrayList;
import java.util.List;

public class Paper extends Node{
	private int year;
	private String paperKey;
	private String category;
	private String journal;


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
	public String getCategory() { return category; }
	public void setCategory(String category) { this.category = category; }
	public String getJournal() { return journal; }
	public void setJournal(String journal) { this.journal = journal; }

	@Override
	public String toString() {
		return "Paper {year=" + year + ", paperKey=" + paperKey + ", category=" + category +", name=" + getName() + "}";
	}

}
