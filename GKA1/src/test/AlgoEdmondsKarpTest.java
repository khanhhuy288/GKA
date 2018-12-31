package test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import gka1.AlgoEdmondsKarp;
import gka1.GkaGraph;
import gka1.GkaUtils;

public class AlgoEdmondsKarpTest {

	@Test
    public void graph03Test() throws UnsupportedEncodingException, FileNotFoundException, IOException {

        GkaGraph graph = GkaUtils.read("graph03.gka");

        
        int maxflow = AlgoEdmondsKarp.solve(graph, "Hamburg", "Kiel");

        int maxFlow = 276;

        assertEquals(maxFlow, maxflow);

    }


    @Test
    public void graph04Test1() throws UnsupportedEncodingException, FileNotFoundException, IOException {

        GkaGraph graph = GkaUtils.read("graph04.gka");

        int maxflow = AlgoEdmondsKarp.solve(graph, "v1", "v8");

        int maxFlow = 20;

        assertEquals(maxFlow, maxflow);

    }

    @Test
    public void graph04Test2() throws UnsupportedEncodingException, FileNotFoundException, IOException {

        GkaGraph graph = GkaUtils.read("graph04.gka");
        
        int maxflow = AlgoEdmondsKarp.solve(graph, "v1", "s");

        int maxFlow = 23;

        assertEquals(maxFlow, maxflow);

    }

}
