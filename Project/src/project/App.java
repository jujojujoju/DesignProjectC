package project;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;

import org.jgrapht.*;
import org.jgrapht.graph.*;

public class App {
	public static Database db;

	public static void main(String args[]) {
		//UndirectedGraph<String, DefaultEdge> stringGraph = createStringGraph();

        // note undirected edges are printed as: {<v1>,<v2>}
        //System.out.println(stringGraph.toString());

        // create a graph based on URL objects
        //DirectedGraph<URL, DefaultEdge> hrefGraph = createHrefGraph();

        // note directed edges are printed as: (<v1>,<v2>)
        //System.out.println(hrefGraph.toString());





        String coauthor = "coauthorList.txt";
		String paper = "paperList.txt";

		UndirectedGraph<String, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);

		try {
			BufferedReader in = new BufferedReader(new FileReader(paper));
			String s;

			while ((s = in.readLine()) != null) {
				//System.out.println(s);
				String[] result = s.split("\\|\\|");

				String paperkey = result[0];
				String authorkey = result[1];
				String year = result[2];

				String[] authorList = authorkey.split("\\&\\&");

				g.addVertex(paperkey);
				for (String author : authorList){
					g.addVertex(author);
					g.addEdge(paperkey, author);
				}

			}
			in.close();
		} catch (IOException e) {
			System.err.println(e); // 에러가 있다면 메시지 출력
			System.exit(1);
		}

		g.
		 System.out.println(g.toString());
		 //System.out.println(g.vertexSet().);



	}

	/**
     * Creates a toy directed graph based on URL objects that represents link structure.
     *
     * @return a graph based on URL objects.
     */
    private static DirectedGraph<URL, DefaultEdge> createHrefGraph()
    {
        DirectedGraph<URL, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);

        try {
            URL amazon = new URL("http://www.amazon.com");
            URL yahoo = new URL("http://www.yahoo.com");
            URL ebay = new URL("http://www.ebay.com");

            // add the vertices
            g.addVertex(amazon);
            g.addVertex(yahoo);
            g.addVertex(ebay);

            // add edges to create linking structure
            g.addEdge(yahoo, amazon);
            g.addEdge(yahoo, ebay);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return g;
    }

    /**
     * Create a toy graph based on String objects.
     *
     * @return a graph based on String objects.
     */
    private static UndirectedGraph<String, DefaultEdge> createStringGraph()
    {
        UndirectedGraph<String, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);

        String v1 = "v1";
        String v2 = "v2";
        String v3 = "v3";
        String v4 = "v4";

        // add the vertices
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);

        // add edges to create a circuit
        g.addEdge(v1, v2);
        g.addEdge(v2, v3);
        g.addEdge(v3, v4);
        g.addEdge(v4, v1);

        return g;
    }
}
