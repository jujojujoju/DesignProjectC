package project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class Database {
	private UndirectedGraph<Node, DefaultEdge> mainGraph;
	private Set<Author> authorSet;
	private Set<Paper> paperSet;

	public Database(){
		mainGraph = new SimpleGraph<>(DefaultEdge.class);
		authorSet = new HashSet<Author>();
		paperSet = new HashSet<Paper>();
	}

	public UndirectedGraph<Node, DefaultEdge> getMainGraph(){
		return mainGraph;
	}

	public Set<Author> getAuthorSet(){
		return authorSet;
	}

	public Set<Paper> getPaperSet(){
		return paperSet;
	}

	public boolean readFile(){
		//String paperURL = "dblp-paper.txt";

        //String coauthorURL = "coauthorList.txt";
		String paperURL = "paperList2.txt";

		try {
			BufferedReader in = new BufferedReader(new FileReader(paperURL));
			String s;

			while ((s = in.readLine()) != null) {
				//System.out.println(s);
				String[] result = s.split("\\|\\|");

				String paperkey = result[0];
				String authorkey = result[1];
				String year = result[2];

				String[] authorStringList = authorkey.split("\\&\\&");

				Paper paperTemp = new Paper(paperkey);
				paperTemp.setYear(Integer.parseInt(year));
				mainGraph.addVertex(paperTemp);
				paperSet.add(paperTemp);

				for(String author: authorStringList){
					Author authorTemp = new Author(author);
					mainGraph.addVertex(authorTemp);
					authorSet.add(authorTemp);
					mainGraph.addEdge(paperTemp, authorTemp);
				}
			}
			in.close();
			return true;
		} catch (IOException e) {
			System.err.println(e); // 에러가 있다면 메시지 출력
			return false;
		}

	}

	//같이 쓴 논문
	public Set<Node> getCoauthorSet(Author sourceAuthor, Author targetAuthor){
    	Set<Node> sourceSet = new HashSet<Node>();

		for (DefaultEdge connectedEdge: mainGraph.edgesOf(sourceAuthor)){
			sourceSet.add(mainGraph.getEdgeSource(connectedEdge));
		}

		Set<Node> targetSet = new HashSet<Node>();

		for (DefaultEdge connectedEdge: mainGraph.edgesOf(targetAuthor)){
			targetSet.add(mainGraph.getEdgeSource(connectedEdge));
		}

		Set<Node> intersection = new HashSet<Node>(sourceSet);
		intersection.retainAll(targetSet);

		return intersection;
	}


	public UndirectedGraph<Node, DefaultEdge> getCoauthorGraph(Author sourceAuthor, Author targetAuthor)
    {
    	Set<Node> sourceSet = new HashSet<Node>();

		for (DefaultEdge connectedEdge: mainGraph.edgesOf(sourceAuthor)){
			sourceSet.add(mainGraph.getEdgeSource(connectedEdge));
		}

		Set<Node> targetSet = new HashSet<Node>();

		for (DefaultEdge connectedEdge: mainGraph.edgesOf(targetAuthor)){
			targetSet.add(mainGraph.getEdgeSource(connectedEdge));
		}

		Set<Node> intersection = new HashSet<Node>(sourceSet);
		intersection.retainAll(targetSet);

		return null;
    }

	public Map<Author, Integer> getAuthorMapByCont(){
		int max = 0;
		int count = 0;
		Map<Author, Integer> contributeList = new HashMap<Author, Integer>();

		for(Author author:authorSet){
			contributeList.put(author, mainGraph.degreeOf(author));
		}

		ValueComparator<Author> bvc =  new ValueComparator<Author>(contributeList);
		TreeMap<Author,Integer> sorted_map = new TreeMap<Author,Integer>(bvc);
		sorted_map.putAll(contributeList);

		return sorted_map;
	}
}
