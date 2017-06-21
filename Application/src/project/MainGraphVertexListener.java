package project;

import org.jgrapht.event.GraphVertexChangeEvent;
import org.jgrapht.event.VertexSetListener;

/**
 * Created by Jaehyun on 2017-06-20.
 */
public class MainGraphVertexListener implements VertexSetListener<Node> {
    @Override
    public void vertexAdded(GraphVertexChangeEvent<Node> e) {
        System.out.println(e.getVertex().toString());
    }

    @Override
    public void vertexRemoved(GraphVertexChangeEvent<Node> e) {
        System.out.println(e.getVertex().toString());
    }
}
