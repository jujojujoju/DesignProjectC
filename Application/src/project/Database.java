package project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

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
		String paperURL = "paperList3.txt";

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
			System.err.println(e);
			return false;
		}

	}


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
		UndirectedGraph<Node, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
		Set<Node> sourceSet = new HashSet<Node>();

		graph.addVertex(sourceAuthor);
		graph.addVertex(targetAuthor);

		for (DefaultEdge connectedEdge: mainGraph.edgesOf(sourceAuthor)){
			sourceSet.add(mainGraph.getEdgeSource(connectedEdge));
		}
		for(Node temp:sourceSet){
			if(mainGraph.getEdge(temp, targetAuthor)!=null){
				graph.addVertex(temp);
				graph.addEdge(temp, targetAuthor);
				graph.addEdge(temp, sourceAuthor);
			}
		}

		return graph;
    }

	public SimpleWeightedGraph<Node, DefaultWeightedEdge> getCoauthorWeightedGraph()
	{
		SimpleWeightedGraph<Node, DefaultWeightedEdge> graph = new SimpleWeightedGraph<Node, DefaultWeightedEdge>(DefaultWeightedEdge.class);

		for(Author sourceAuthor: authorSet){
			HashSet<Author> tempSet = new HashSet<Author>();
			tempSet.addAll(authorSet);
			tempSet.remove(sourceAuthor);

			for(Author tempAuthor: tempSet){
				graph.addVertex(sourceAuthor);
				graph.addVertex(tempAuthor);
				int size =  getCoauthorSet(sourceAuthor, tempAuthor).size();
				DefaultWeightedEdge e1 = graph.addEdge(sourceAuthor, tempAuthor);
				if(e1 != null)
					graph.setEdgeWeight(e1, size+1);
			}
		}

		return graph;
	}

	public Map<Author, Integer> getAuthorMapByCont(int count){
		int max = 0;
		int row = 0;
		Map<Author, Integer> contributeList = new HashMap<Author, Integer>();

		for(Author author:authorSet){
			int contributeNum = mainGraph.degreeOf(author);
			if(row < count){
				if(contributeNum>max)
					max = contributeNum;
				contributeList.put(author, contributeNum);
				row++;
			}
			else{
				if(contributeNum>max){

				}
			}

		}

		ValueComparator<Author> bvc =  new ValueComparator<Author>(contributeList);
		TreeMap<Author,Integer> sorted_map = new TreeMap<Author,Integer>(bvc);
		sorted_map.putAll(contributeList);

		return sorted_map;
	}
}
