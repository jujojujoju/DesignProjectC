package project;

import sample.Cell;
import sample.Graph;
import sample.LabelCell;
import sample.Layout;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * Created by joju on 2017. 6. 10..
 */
public class CenterLayout extends Layout{

    private Graph graph;
    private HashSet<Author> centerAuthorSet;
    private Random rnd = new Random();

    public CenterLayout(Graph graph, HashSet<Author> centerAuthorSet) {

        this.graph = graph;
        this.centerAuthorSet = centerAuthorSet;

    }

    public void execute() {

        double x = 0;
        double y = 0;
        List<Cell> cells = graph.getModel().getAllCells();

        for (Cell cell : cells) {
            x = rnd.nextDouble() * 2000;
            y = rnd.nextDouble() * 1000;

            for (Author author : centerAuthorSet) {

                if (author.getName().equals(cell.getCellId()))
                {
                    x = rnd.nextDouble() * 400+800;
                    y = rnd.nextDouble() * 200+400;
                    break;
                }
            }
            cell.relocate(x, y);
        }

    }
}
