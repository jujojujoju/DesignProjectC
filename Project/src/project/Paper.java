package project;


public class Paper extends Node{
	private int year;
	private String paperKey;

	public Paper(){
		year = -1;
		paperKey = "";
	}

	public Paper(String paper) {
		// TODO Auto-generated constructor stub
		this.setName(paper);
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
