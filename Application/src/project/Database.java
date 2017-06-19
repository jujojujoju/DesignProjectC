package project;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;

import java.util.*;

import org.jgrapht.GraphPath;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.scoring.PageRank;
import org.jgrapht.alg.shortestpath.BellmanFordShortestPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.event.GraphVertexChangeEvent;
import org.jgrapht.event.VertexSetListener;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

public class Database {
	private UndirectedGraph<Node, DefaultEdge> mainGraph;
	private SimpleWeightedGraph<Author, DefaultWeightedEdge> authorGraph;

	private Map<Author, List<Paper>> subscriptMap;
	private Map<Author, Paper> subscriptStack;

	private Map<String, Author> authorMap;
	private Map<String, Paper> paperMap;

	private String dbName = "";
	private long timecheck;

	private int seq = 0;

	//todo xml에서 읽는 기능 추가
	//todo 저자 센터에 놓기
	//todo 구독기능 구현하기 - 80%
	//todo 논문수에 따른 top k 저널별 연도별 기능 추가
	//todo 저자 이름 검색 및 이름 제안 기능 추가 - 100%
	//todo 검색횟수 목록 추가 - 50%

	//todo 공동저자가 제일 많은 저자와 가장 많은 논문을 작성한 저자 컬러링 기능도 구현

	//DB관련 생성자
	public Database(){
		mainGraph = new SimpleGraph<>(DefaultEdge.class);
		authorGraph = new SimpleWeightedGraph<Author, DefaultWeightedEdge> (DefaultWeightedEdge.class);

		authorMap = new HashMap<String, Author>();
		paperMap = new HashMap<String, Paper>();


	}

	public Database(String dbName){
		mainGraph = new SimpleGraph<>(DefaultEdge.class);
		authorGraph = new SimpleWeightedGraph<Author, DefaultWeightedEdge> (DefaultWeightedEdge.class);

		authorMap = new HashMap<String, Author>();
		paperMap = new HashMap<String, Paper>();
		this.dbName = dbName;

		subscriptMap = new HashMap<Author, List<Paper>>();
		subscriptStack = new HashMap<Author, Paper>();
		subscriptMap.put(new Author("Pyung Soo Kim"), new ArrayList<Paper>());
	}

	public UndirectedGraph<Node, DefaultEdge> getMainGraph(){
		return mainGraph;
	}

	public Map<Author, List<Paper>> getSubscriptMap() {
		return subscriptMap;
	}

	public void setSubscriptMap(Map<Author

			, List<Paper>> subscriptMap) {
		this.subscriptMap = subscriptMap;
	}

	public Map<Author, Paper> getSubscriptStack() {
		return subscriptStack;
	}

	public void setSubscriptStack(Map<Author, Paper> subscriptStack) {
		this.subscriptStack = subscriptStack;
	}

	public SimpleWeightedGraph<Author, DefaultWeightedEdge> getAuthorGraph() { return authorGraph; }

	public Set<Author> getAuthorSet(){
		return authorGraph.vertexSet();
	}

	public Collection<Paper> getPaperCollection() { return paperMap.values(); }

	//파일의 수정시간을 다시 고친다.
	public void initModifiedTime() {
		File file = new File(dbName);
		timecheck = file.lastModified();
	}

	//DB파일의 수정시간을 체크한다.
	public boolean checkFile(){
		File file = new File(dbName);
		if(timecheck == file.lastModified()){
			return false;
		}
		else{
			initModifiedTime();
			return true;
		}
	}

	//시간마다 파일을 읽어오도록 한다.
	public boolean readFile(){

		String paperURL = dbName;
		int index = 0;
		try {
			BufferedReader in = new BufferedReader(new FileReader(paperURL));
			String s;

			while ((s = in.readLine()) != null) {
				String[] result = s.split("\\|\\|");	//페이퍼 리스트를 분할

				String paperName = result[0];	//페이퍼 이름
				String authorkey = result[1];	//저자 목록
				String year = result[2];		//연도


				String[] paperStringList = paperName.split("\\/");	//논문 이름을 분할
				String[] authorStringList = authorkey.split("\\&\\&");	//연도 목록을 분할

				Paper paperTemp = new Paper();	//새로운 페이퍼 생성
				paperTemp.setPaperKey(paperName);
				paperTemp.setCategory(paperStringList[0]);
				paperTemp.setJournal(paperStringList[1]);
				paperTemp.setName(paperStringList[2]);
				paperTemp.setYear(Integer.parseInt(year));

				if(checkSubscriptionList(paperTemp, authorStringList)) {
					System.out.println("구독 테스트");
				}
				paperTemp = addPaper(paperTemp);

				if(!authorkey.equals("")){
					List<Author> list = new ArrayList<Author>();

					for(String authorName: authorStringList){
						Author authorTemp = new Author(authorName);		//저자 생성
						authorTemp = addAuthor(authorTemp);
						mainGraph.addEdge(paperTemp, authorTemp);	//저자와 해당 페이퍼를 잇는 edge추가
						list.add(authorTemp);
					}

					Set<Set<Author>> resultSet = new HashSet<Set<Author>> ();
					Combination<Author> combination = new Combination<Author>();
					resultSet = combination.getCombinationsFor (list, 2);


					//여기서 주변 사람들 그래프 재 설정
					for(Set<Author> connectedSet: resultSet) {
						Author[] arr = new Author[2];
						connectedSet.toArray(arr);

						DefaultWeightedEdge e1 = authorGraph.addEdge(arr[0], arr[1]);
						if (e1 != null) // edge 새로 생성된 경우
							authorGraph.setEdgeWeight(e1, 1);
						else {			 // edge 이미 만들어 진 경우
							DefaultWeightedEdge e2 = authorGraph.getEdge(arr[0], arr[1]);
							double weight = authorGraph.getEdgeWeight(e2);
							// 많이 같이 쓸 수록 가깝게 표현해야 한다.
							weight = 1 / weight;
							weight += 1;
							weight = 1 / weight;
							// 역수를 반영한다.
							authorGraph.setEdgeWeight(e2, weight);
						}

					}


				}
				System.out.println(++index);
			}
			in.close();


			//테스팅 부분임

			getRecommandPaperMap(new Author("Shuichi Itoh"));
			//getRecommandPaperMap(new Author("AAA"));

			//연도별 테스트 중
			Params params = new Params();
			params.year = 2008;

			for(Map.Entry<Author, Integer> entry: getAuthorMapByCont(params).entrySet()) {
				System.out.println(entry.toString());
			}


			return true;
		} catch (IOException e) {
			System.err.println(e);
			return false;
		}

	}



	public Paper addPaper(Paper paper) {
		if(paperMap.get(paper.getName()) != null) {
			return paperMap.get(paper.getName());
		}
		else {
			paperMap.put(paper.getName(), paper);
			mainGraph.addVertex(paper);
			return paper;
		}
	}

	public Author addAuthor(Author author) {
		if(authorGraph.vertexSet().contains(author))
			return authorMap.get(author.getName());
		else {
			mainGraph.addVertex(author);
			authorGraph.addVertex(author);
			authorMap.put(author.getName(), author);
			return author;
		}
	}

	//sourceAuthor와 targetAuthor 사이의 페이퍼를 반환. 즉 같이 쓴 페이퍼를 set 형태로 모두 반환한다.
	public Set<Node> getCoauthorSet(Author sourceAuthor, Author targetAuthor){
		Set<Node> sourceSet = new HashSet<Node>();

		//만약 파라미터가 존재하지 않는 인원이 들어온 경우 아무도 없는 set을 반환한다.
		if(!authorGraph.vertexSet().contains(sourceAuthor)||!authorGraph.vertexSet().contains(targetAuthor))
			return sourceSet;

		//시작하는 저자와 이어진 페이퍼를 모두 찾는다.
		for (DefaultEdge connectedEdge: mainGraph.edgesOf(sourceAuthor)){
			sourceSet.add(mainGraph.getEdgeSource(connectedEdge));
		}

		Set<Node> targetSet = new HashSet<Node>();

		//목표점인 저자와 이어진 페이퍼를 모두 찾는다.
		for (DefaultEdge connectedEdge: mainGraph.edgesOf(targetAuthor)){
			targetSet.add(mainGraph.getEdgeSource(connectedEdge));
		}

		//두 set의 교집합을 만든다.
		Set<Node> intersection = new HashSet<Node>(sourceSet);
		intersection.retainAll(targetSet);

		//교집합 반환
		return intersection;
	}

	//두 저자 사이의 공동 제작 논문을 그래프로 보여준다.
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

	/////둘 사이 관계도 그래프
	public UndirectedGraph<Node, DefaultEdge> getRelationGraph(Author sourceAuthor, Author targetAuthor)
	{
		// 그래프 생성
		UndirectedGraph<Node, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

		// 먼저 최단거리 경로를 찾는다.
		BellmanFordShortestPath<Node, DefaultWeightedEdge> shortestPathAlg = new BellmanFordShortestPath(authorGraph);
		GraphPath<Node, DefaultWeightedEdge> path =  shortestPathAlg.getPath(sourceAuthor, targetAuthor);

		if(path != null) {
			List<Node> authors = path.getVertexList();
			for(Node author: authors) {
				graph.addVertex(author);
				Set<DefaultEdge> set = mainGraph.edgesOf(author);
				for(DefaultEdge edge: set) {
					graph.addVertex(mainGraph.getEdgeSource(edge));
					graph.addVertex(mainGraph.getEdgeTarget(edge));
					graph.addEdge(mainGraph.getEdgeSource(edge), mainGraph.getEdgeTarget(edge));
				}
			}
		}

		System.out.println(graph);
		// 그래프를 출력한다.
		return graph;
	}


	public SimpleWeightedGraph<Node, DefaultWeightedEdge> getCoauthorWeightedGraph(Author sourceAuthor)
	{
		SimpleWeightedGraph<Node, DefaultWeightedEdge> graph = new SimpleWeightedGraph<Node, DefaultWeightedEdge>(DefaultWeightedEdge.class);

		HashSet<Author> tempSet = new HashSet<Author>();
		tempSet.addAll(authorGraph.vertexSet());
		if(!authorGraph.vertexSet().contains(sourceAuthor))
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
			tempSet.addAll(authorGraph.vertexSet());
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

	private Map<Author, Integer> sortMap(Map<Author, Integer> integerMap, int count) {

		//ValueComparator는 value기준 내림차순 정렬해주는 인터페이스
		ValueComparator<Author> bvc =  new ValueComparator<Author>(integerMap);

		//treemap에서 bvc로 정렬
		TreeMap<Author,Integer> sorted_map = new TreeMap<Author,Integer>(bvc);
		sorted_map.putAll(integerMap);

		//결과는 다른 map인 result에서 처리한다.
		TreeMap<Author,Integer> result = new TreeMap<Author,Integer>(bvc);

		//count보다 map의 원소개수가 이하인 경우
		if(count>=sorted_map.size()){
			return sorted_map;		//그대로 반환한다.
		}
		else{
			Iterator it = sorted_map.entrySet().iterator();	//이터레이터 설정
			int min = -1;	//최소값 비교대상
			int index = 0;		//개수
			//map에서 계속 출력
			while(it.hasNext()){
				Map.Entry<Author, Integer> entry = (Map.Entry<Author, Integer>)it.next();	//값을 뽑아낸다.
				if (index >= count && min > entry.getValue()) //개수가 count 개 이상이고 최소 값이 count 등의 value 보다 작으면 탈출시킨다.
					break;

				result.put(entry.getKey(), entry.getValue());
				index++;
				min = entry.getValue();
			}
			return result;
		}
	}


	//////전체 노드에서 탑케이
	public Map<Author, Integer> getAuthorMapByCont(int count){
		Map<Author, Integer> contributeList = new HashMap<Author, Integer>();

		for(Author author:authorGraph.vertexSet()){
			int contributeNum = mainGraph.degreeOf(author);
			contributeList.put(author, contributeNum);
		}

		return sortMap(contributeList, count);
	}

	///저자 기준으로 탑테이
	public Map<Author, Integer> getAuthorMapByCont(Author sourceAuthor, int count){
		Map<Author, Integer> contributeList = new HashMap<Author, Integer>();

		//각 저자가 만든 페이퍼 수를 value로 해서 map 만들기.
		for(Author author:authorGraph.vertexSet()){
			if(getCoauthorSet(sourceAuthor,author).size() > 0) {
				int contributeNum = mainGraph.degreeOf(author);
				contributeList.put(author, contributeNum);
			}
		}

		return sortMap(contributeList, count);
	}

	public Map<Author, Integer> getAuthorMapByCont(Params params) {
		//todo 논문 검색 경우 제목으로 검색 추가
		Map<Author, Integer> result = new HashMap<> ();

		if(params.year != -1) {
			for(Author author:authorGraph.vertexSet()) {
				int contributeNum = 0;
				for(DefaultEdge edge: mainGraph.edgesOf(author)) {
					Paper paper = (Paper) mainGraph.getEdgeSource(edge);
					if(paper.getYear() == params.year)
						contributeNum++;

				}
				result.put(author, contributeNum);
			}
		}


		return sortMap(result, 10);
	}


	public Map<Node, Double> getRecommandPaperMap(Author sourceAuthor) {
		Map<Node, Double> resultPaperList = new HashMap<Node, Double>();
		Map<Node, Double> resultPaperList2 = new HashMap<Node, Double>();

		PageRank rankAlg = new PageRank(mainGraph, 0.85);
		resultPaperList = rankAlg.getScores();

		PageRank rankAlg2 = new PageRank(authorGraph, 0.85);
		resultPaperList2 = rankAlg2.getScores();



		//ValueComparator는 value기준 내림차순 정렬해주는 인터페이스
		ValueComparatorByDouble<Node> bvc =  new ValueComparatorByDouble<Node>(resultPaperList);
		ValueComparatorByDouble<Node> bvc2 =  new ValueComparatorByDouble<Node>(resultPaperList2);

		//treemap에서 bvc로 정렬
		TreeMap<Node,Double> sorted_map = new TreeMap<Node,Double>(bvc);
		sorted_map.putAll(resultPaperList);
		TreeMap<Node,Double> sorted_map2 = new TreeMap<Node,Double>(bvc2);
		sorted_map2.putAll(resultPaperList2);

		//결과는 다른 map에서
		TreeMap<Node,Double> result = new TreeMap<Node,Double>(bvc);

		int count = 0;
		//count보다 map의 원소개수가 이하인 경우
		Iterator it = sorted_map.entrySet().iterator();	//이터레이터 설정
		Map.Entry<Node, Double> entry;

		//paper 50개를 추린다.
		while(it.hasNext() && count < 50) {
			entry =  (Map.Entry<Node, Double>)it.next();
			if(entry.getKey() instanceof Paper) {
				result.put(entry.getKey(), entry.getValue());
				count++;
			}
		}



		Map<Node, Double> aroundNodeMap = new HashMap<Node, Double>();

		DijkstraShortestPath<Node, DefaultEdge> shortestPathAlg = new DijkstraShortestPath(mainGraph);
		ShortestPathAlgorithm.SingleSourcePaths<Node, DefaultEdge> paths =  shortestPathAlg.getPaths(sourceAuthor);
		//경로가 긴 순서대로 나열한다.
		//fixme  안 이어진 부분은 어떻게 처리해야 될까??

		for(Node node: mainGraph.vertexSet()) {
			if(Double.isFinite(paths.getWeight(node)))
				aroundNodeMap.put(node, paths.getWeight(node));
		}

		//ValueComparator는 value기준 내림차순 정렬해주는 인터페이스
		ValueComparatorByDouble<Node> bvc3 =  new ValueComparatorByDouble<Node>(aroundNodeMap);

		//treemap에서 bvc로 정렬
		TreeMap<Node,Double> sorted_map3 = new TreeMap<Node,Double>(bvc3);
		sorted_map3.putAll(aroundNodeMap);

		System.out.println(sorted_map3);



		return resultPaperList;
	}

	public boolean checkSubscriptionList(Paper paper, String[] authorStringList) {
		for(String author: authorStringList) {
			if(subscriptMap.containsKey(new Author(author))) {
				if(subscriptMap.get(new Author(author)).contains(paper)) {
					//해당 구독이 이미 체크가 된 경우
					return false;
				}
				else {
					subscriptMap.get(new Author(author)).add(paper);
					subscriptStack.put(new Author(author), paper);
					return true;
				}
			}
		}
		return false;
	}

	public List<String> getSearchAuthorList(String keyword){
		List<String> authorArrayList = new ArrayList<>();

		for(Author author: getAuthorSet()) {
			if(author.getName().contains(keyword)) {
				authorArrayList.add(author.getName());
			}
		}
		return authorArrayList;
	}

	public List<Author> getSearchAuthorObjList(String keyword){
		List<Author> authorArrayList = new ArrayList<>();

		for(Author author: getAuthorSet()) {
			if(author.getName().contains(keyword)) {
				authorArrayList.add(author);
			}
		}
		return authorArrayList;
	}

	public Map<Author, Integer> getSearchAuthorTopkList(int count){
		Map<Author, Integer> authorIntegerTreeMap = new TreeMap<>();

		//ValueComparator는 value기준 내림차순 정렬해주는 인터페이스
		ValueComparator<Author> bvc =  new ValueComparator<>(authorIntegerTreeMap);

		for(Author author:authorGraph.vertexSet()){
			authorIntegerTreeMap.put(author, author.getNumOfSearch());
		}

		Map<Author, Integer> sortedMap = new TreeMap<>(bvc);

		for(Author author: sortMap(authorIntegerTreeMap, count).keySet()) {
			if(author.numOfSearch>0)
				sortedMap.put(author, author.getNumOfSearch());
		}
		return sortedMap;
	}
}