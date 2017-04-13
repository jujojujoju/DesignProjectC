package project;

import java.util.List;

public class Paper extends Node{
	private List<Author> authorList;
	private int year;
	private String paperKey;

	public List<Author> getAuthorList() {
		return authorList;
	}
	public void setAuthorList(List<Author> authorList) {
		this.authorList = authorList;
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
}
