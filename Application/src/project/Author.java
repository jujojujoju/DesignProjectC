package project;

public class Author extends Node{

	int numOfSearch;

	public Author(){
		numOfSearch = 0;
	}

	public int getNumOfSearch() {
		return numOfSearch;
	}

	public void setNumOfSearch(int numOfSearch) {
		this.numOfSearch = numOfSearch;
	}

	public void addNumOfSearch() {
		this.numOfSearch++;
	}

	public Author(String name){
		this.setName(name);
	}

	@Override
	public String toString() {
		return "Author {getName()=" + getName() + "}";
	}

}
