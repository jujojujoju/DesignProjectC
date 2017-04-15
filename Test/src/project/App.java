package project;


import java.net.*;


import org.jgrapht.*;
import org.jgrapht.graph.*;

public class App {
	public static Database db;

	public static void appstart(String args[]) {
		db = new Database();
		db.readFile();
		//System.out.println(db.getMainGraph().toString());
		//System.out.println(db.getAuthorSet().toString());
		//System.out.println(db.getPaperSet().toString());
		//System.out.println(db.getCoauthorSet(new Author("AAA"), new Author("BBB")).toString());
		System.out.println(db.getAuthorMapByCont().toString());

	}

	/**
     * Creates a toy directed graph based on URL objects that represents link structure.
     *
     * @return a graph based on URL objects.
     */
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
