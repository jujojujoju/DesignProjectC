package project;

public class Author extends Node{

	public Author(){
	}

	public Author(String name){
		this.setName(name);
	}

	@Override
	public String toString() {
		return "Author {getName()=" + getName() + "}";
	}

}
