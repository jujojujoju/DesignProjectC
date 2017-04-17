package project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.jgrapht.GraphPath;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

public class Database {
	private UndirectedGraph<Node, DefaultEdge> mainGraph;
	private SimpleWeightedGraph<Author, DefaultWeightedEdge> authorGraph;
	private Set<Author> authorSet;
	private Set<Paper> paperSet;

	public Database(){
		mainGraph = new SimpleGraph<>(DefaultEdge.class);
		authorGraph = new SimpleWeightedGraph<Author, DefaultWeightedEdge> (DefaultWeightedEdge.class);
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

		String paperURL = "paperList4.txt";
		int count = 0;

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

				if(!authorkey.equals("")){
					for(String author: authorStringList){
						Author authorTemp = new Author(author);
						mainGraph.addVertex(authorTemp);
						authorSet.add(authorTemp);
						mainGraph.addEdge(paperTemp, authorTemp);
					}

					List<Author> authorList = new ArrayList<Author>();
					for(String authorString: authorStringList){
						Author authorTemp = new Author(authorString);
						authorGraph.addVertex(authorTemp);
						authorList.add(authorTemp);
					}
				}



//				System.out.println(count++);
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

		if(!authorSet.contains(sourceAuthor)||!authorSet.contains(targetAuthor))
			return sourceSet;

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

	int _length = 6;

	/////둘 사이 관계도 그래프
	public UndirectedGraph<Node, DefaultEdge> getRelationGraph(Author sourceAuthor, Author targetAuthor)
	{
		UndirectedGraph<Node, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
		DijkstraShortestPath<Node, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath(mainGraph);
		ShortestPathAlgorithm.SingleSourcePaths<Node, DefaultEdge> paths =  dijkstraShortestPath.getPaths(sourceAuthor);
		DijkstraShortestPath<Node, DefaultEdge> dijkstraShortestPath2 = new DijkstraShortestPath(mainGraph);
		ShortestPathAlgorithm.SingleSourcePaths<Node, DefaultEdge> paths2 =  dijkstraShortestPath.getPaths(targetAuthor);

		Set<Author> relatedAuthor = new HashSet<Author>();
		Set<GraphPath<Node, DefaultEdge>> graphSet = new HashSet<GraphPath<Node, DefaultEdge>>();
		for(Author author: authorSet){
			double length = paths.getWeight(author);
			double length2 = paths2.getWeight(author);
			if(length+length2<=6) {
				graphSet.add(paths.getPath(author));
				graphSet.add(paths2.getPath(author));
			}
		}
		for(GraphPath<Node, DefaultEdge> path: graphSet){
			for(Node node: path.getVertexList())
				graph.addVertex(node);
			for(DefaultEdge edge: path.getEdgeList())
				graph.addEdge(mainGraph.getEdgeSource(edge), mainGraph.getEdgeTarget(edge));
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

				int size =  getCoauthorSet(sourceAuthor, tempAuthor).size();
				if(size>0) {
					graph.addVertex(tempAuthor);
					DefaultWeightedEdge e1 = graph.addEdge(sourceAuthor, tempAuthor);
					if (e1 != null)
						graph.setEdgeWeight(e1, size);
				}
			}
		}

		return graph;
	}

	public SimpleWeightedGraph<Node, DefaultWeightedEdge> getCoauthorWeightedGraph(Author sourceAuthor)
	{
		SimpleWeightedGraph<Node, DefaultWeightedEdge> graph = new SimpleWeightedGraph<Node, DefaultWeightedEdge>(DefaultWeightedEdge.class);

		HashSet<Author> tempSet = new HashSet<Author>();
		tempSet.addAll(authorSet);
		if(!authorSet.contains(sourceAuthor))
			return graph;
		tempSet.remove(sourceAuthor);

		graph.addVertex(sourceAuthor);
		for(Author tempAuthor: tempSet){
			int size =  getCoauthorSet(sourceAuthor, tempAuthor).size();

			if(size>0) {
				graph.addVertex(tempAuthor);

				DefaultWeightedEdge e1 = graph.addEdge(sourceAuthor, tempAuthor);
				if (e1 != null)
					graph.setEdgeWeight(e1, size);
			}
		}

		return graph;
	}

	public SimpleWeightedGraph<Node, DefaultWeightedEdge> getCoauthorWeightedGraph(Set<Author> sourceAuthorSet)
	{
		SimpleWeightedGraph<Node, DefaultWeightedEdge> graph = new SimpleWeightedGraph<Node, DefaultWeightedEdge>(DefaultWeightedEdge.class);

		HashSet<Author> tempSet = new HashSet<Author>();

		for(Author sourceAuthor: sourceAuthorSet){
			tempSet.addAll(authorSet);
			tempSet.remove(sourceAuthor);

			graph.addVertex(sourceAuthor);
			for(Author tempAuthor: tempSet){
				int size =  getCoauthorSet(sourceAuthor, tempAuthor).size();
				if(size>0) {
					graph.addVertex(tempAuthor);
					DefaultWeightedEdge e1 = graph.addEdge(sourceAuthor, tempAuthor);
					if (e1 != null)
						graph.setEdgeWeight(e1, size);
				}
			}
		}

		return graph;
	}


	//////전체 노드에서 탑케이
	public Map<Author, Integer> getAuthorMapByCont(int count){
		Map<Author, Integer> contributeList = new HashMap<Author, Integer>();

		for(Author author:authorSet){
			int contributeNum = mainGraph.degreeOf(author);
			contributeList.put(author, contributeNum);
		}

		ValueComparator<Author> bvc =  new ValueComparator<Author>(contributeList);
		TreeMap<Author,Integer> sorted_map = new TreeMap<Author,Integer>(bvc);
		sorted_map.putAll(contributeList);

		TreeMap<Author,Integer> result = new TreeMap<Author,Integer>(bvc);

		if(count>=sorted_map.size()){
			return sorted_map;
		}
		else{
			Iterator it = sorted_map.entrySet().iterator();
			int min = -1;
			int index = 0;
			while(it.hasNext()){
				Map.Entry<Author, Integer> entry = (Map.Entry<Author, Integer>)it.next();
				if (index >= count && min > entry.getValue()) //개수가 count 개 이상이고 최소 값이 count 등의 value 보다 작으면 탈출시킨다.
					break;

				result.put(entry.getKey(), entry.getValue());
				index++;
				min = entry.getValue();
			}
			return result;
		}

	}

	///저자 기준으로 탑테이
	public Map<Author, Integer> getAuthorMapByCont(Author sourceAuthor, int count){
		Map<Author, Integer> contributeList = new HashMap<Author, Integer>();

		for(Author author:authorSet){
			if(getCoauthorSet(sourceAuthor,author).size() > 0) {
				int contributeNum = mainGraph.degreeOf(author);
				contributeList.put(author, contributeNum);
			}
		}

		ValueComparator<Author> bvc =  new ValueComparator<Author>(contributeList);
		TreeMap<Author,Integer> sorted_map = new TreeMap<Author,Integer>(bvc);
		sorted_map.putAll(contributeList);

		TreeMap<Author,Integer> result = new TreeMap<Author,Integer>(bvc);

		if(count>=sorted_map.size()){
			return sorted_map;
		}
		else{
			Iterator it = sorted_map.entrySet().iterator();
			int min = -1;
			int index = 0;
			while(it.hasNext()){
				Map.Entry<Author, Integer> entry = (Map.Entry<Author, Integer>)it.next();
				if (index >= count && min > entry.getValue()) //개수가 count 개 이상이고 최소 값이 count 등의 value 보다 작으면 탈출시킨다.
					break;


				result.put(entry.getKey(), entry.getValue());
				index++;
				min = entry.getValue();
			}
			return result;
		}
	}
}