package graph.algo.viz.circlefit;

import java.util.function.Consumer;
import java.util.function.ToDoubleFunction;

import graph.Edge;
import graph.algo.viz.Representation;

public class Circlefit {
	
	public static void layoutCircle(Representation rep) {
		for (int i=0; i<rep.getGraph().vertexCount(); i++) {
			double[] coord = new double[2];
			coord[0] = Math.cos(rep.getChunk()*rep.getOrdering().getData()[i]);
			coord[1] = Math.sin(rep.getChunk()*rep.getOrdering().getData()[i]);
			rep.setLayout(i, coord);
		}
	}
	
	
	public static Consumer<Representation> mutate = rep -> {
		rep.getOrdering().mutate();
	};
	
	
	public static ToDoubleFunction<Representation> fit_EdgeLengthSum = rep -> {
		double result = 0;
		double dx = 0;
		double dy = 0;
		for (Edge e : rep.getGraph().getEdgeList()) {
			dx = rep.getLayout(e.getVertex(0), 0) - rep.getLayout(e.getVertex(1), 0);
			dy = rep.getLayout(e.getVertex(0), 1) - rep.getLayout(e.getVertex(1), 1);
			result = result + Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2));
		}
		return result;
	};

	public static ToDoubleFunction<Representation> fit_EdgeCrossings = rep -> {
		double result = 0;
		
		for (int i=0; i<rep.getGraph().edgeCount(); i++) {
			for (int j=i+1; j<rep.getGraph().edgeCount(); j++) {

				Edge a = rep.getGraph().getEdgeList().get(i); // edge a
				int a0 = a.getVertex(0); //vertex 0 of edge a
				int a1 = a.getVertex(1); //vertex 1 of edge a

				//minimal and maximal position on the circular ordering
				int a_min =  Math.min(rep.getOrdering().getData()[a0], rep.getOrdering().getData()[a1]);
				int a_max =  Math.max(rep.getOrdering().getData()[a0], rep.getOrdering().getData()[a1]);

				Edge b = rep.getGraph().getEdgeList().get(j);	
				int b0 = b.getVertex(0);
				int b1 = b.getVertex(1);
				int b_min = Math.min(rep.getOrdering().getData()[b0], rep.getOrdering().getData()[b1]);
				int b_max = Math.max(rep.getOrdering().getData()[b0], rep.getOrdering().getData()[b1]);
				
				if (a_min < b_min ) {
					if (b_min < a_max)
						result++;
				} else {
					if (a_min < b_max)
						result++;
				}
			}
		}
//		System.out.println("Intersections: " + result);
		
		return result;
	};
}