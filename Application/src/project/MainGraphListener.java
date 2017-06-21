package project;

import javafx.scene.control.Alert;
import org.jgrapht.event.GraphEdgeChangeEvent;
import org.jgrapht.event.GraphListener;
import org.jgrapht.event.GraphVertexChangeEvent;
import org.jgrapht.graph.DefaultEdge;

/**
 * Created by Jaehyun on 2017-06-20.
 */
public class MainGraphListener implements GraphListener<Node, DefaultEdge> {

    Controller controller;
    Subscription subscription;

    public void setMainScene(Controller controller) {
        this.controller = controller;
    }
    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    @Override
    public void edgeAdded(GraphEdgeChangeEvent e) {
        if(subscription.checkSubscriptionList((Paper) e.getEdgeSource(), (Author) e.getEdgeTarget()))
            controller.showAlert(e);
    }

    @Override
    public void edgeRemoved(GraphEdgeChangeEvent e) {

    }

    @Override
    public void vertexAdded(GraphVertexChangeEvent e) {

    }

    @Override
    public void vertexRemoved(GraphVertexChangeEvent e) {

    }
}
