/*
 * Copyright (c) 2017-2019 "Neo4j,"
 * Neo4j Sweden AB [http://neo4j.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.graphalgo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neo4j.graphalgo.compat.MapUtil;
import org.neo4j.graphalgo.linkprediction.LinkPredictionFunc;
import org.neo4j.graphalgo.unionfind.MSColoringProc;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ListProcTest extends ProcTestBase {

    private static final Set<String> PROCEDURES = new HashSet<>(asList(
        "algo.allShortestPaths.stream",
        "algo.articleRank",
        "algo.articleRank.stream",
        "algo.asPath",
        "algo.balancedTriads",
        "algo.balancedTriads.stream",
        "algo.betweenness",
        "algo.betweenness.sampled",
        "algo.betweenness.sampled.stream",
        "algo.betweenness.stream",
        "algo.bfs.stream",
        "algo.closeness",
        "algo.closeness.dangalchev",
        "algo.closeness.dangalchev.stream",
        "algo.closeness.harmonic",
        "algo.closeness.harmonic.stream",
        "algo.closeness.stream",
        "algo.degree",
        "algo.degree.stream",
        "algo.dfs.stream",
        "algo.eigenvector",
        "algo.eigenvector.stream",
        "algo.infoMap",
        "algo.infoMap.stream",
        "algo.kShortestPaths",
        "algo.kShortestPaths.stream",
        "algo.labs.ml.ann",
        "algo.labs.ml.ann.stream",
        "algo.mst",
        "algo.randomWalk.stream",
        "algo.scc",
        "algo.scc.forwardBackward.stream",
        "algo.scc.iterative",
        "algo.scc.iterative.stream",
        "algo.scc.multistep",
        "algo.scc.multistep.stream",
        "algo.scc.recursive.tarjan",
        "algo.scc.recursive.tunedTarjan",
        "algo.scc.recursive.tunedTarjan.stream",
        "algo.scc.stream",
        "algo.shortestPath",
        "algo.shortestPath.astar.stream",
        "algo.shortestPath.deltaStepping",
        "algo.shortestPath.deltaStepping.stream",
        "algo.shortestPath.stream",
        "algo.shortestPaths",
        "algo.shortestPaths.stream",
        "algo.similarity.cosine",
        "algo.similarity.cosine.stream",
        "algo.similarity.euclidean",
        "algo.similarity.euclidean.stream",
        "algo.similarity.jaccard",
        "algo.similarity.jaccard.stream",
        "algo.similarity.overlap",
        "algo.similarity.overlap.stream",
        "algo.similarity.pearson",
        "algo.similarity.pearson.stream",
        "algo.spanningTree",
        "algo.spanningTree.kmax",
        "algo.spanningTree.kmin",
        "algo.spanningTree.maximum",
        "algo.spanningTree.minimum",
        "algo.triangle.stream",
        "algo.triangleCount",
        "algo.triangleCount.forkJoin",
        "algo.triangleCount.forkJoin.stream",
        "algo.triangleCount.stream",
        "algo.unionFind.mscoloring",
        "algo.unionFind.mscoloring.stream"
    ));

    private static final Set<String> FUNCTIONS = new HashSet<>(asList(
        "algo.linkprediction.adamicAdar",
        "algo.linkprediction.resourceAllocation",
        "algo.linkprediction.commonNeighbors",
        "algo.linkprediction.preferentialAttachment",
        "algo.linkprediction.totalNeighbors",
        "algo.linkprediction.sameCommunity"
    ));

    private static final Set<String> SPANNING_TREE = new HashSet<>(asList(
        "algo.spanningTree",
        "algo.spanningTree.kmax",
        "algo.spanningTree.kmin",
        "algo.spanningTree.maximum",
        "algo.spanningTree.minimum"
    ));

    private static final Set<String> ALL = Stream.concat(PROCEDURES.stream(), FUNCTIONS.stream()).collect(Collectors.toSet());

    @BeforeEach
    void setUp() throws Exception {
        db = TestDatabaseCreator.createTestDatabase();
        registerProcedures(
            ListProc.class,
            MSColoringProc.class,
            AllShortestPathsProc.class,
            ApproxNearestNeighborsProc.class,
            ArticleRankProc.class,
            BalancedTriadsProc.class,
            BetweennessCentralityProc.class,
            ClosenessCentralityProc.class,
            CosineProc.class,
            DangalchevCentralityProc.class,
            DegreeCentralityProc.class,
            EigenvectorCentralityProc.class,
            EuclideanProc.class,
            HarmonicCentralityProc.class,
            InfoMapProc.class,
            IsFiniteFunc.class,
            JaccardProc.class,
            KShortestPathsProc.class,
            KSpanningTreeProc.class,
            NodeWalkerProc.class,
            OneHotEncodingFunc.class,
            OverlapProc.class,
            PearsonProc.class,
            PrimProc.class,
            ShortestPathDeltaSteppingProc.class,
            ShortestPathProc.class,
            ShortestPathsProc.class,
            SimilaritiesFunc.class,
            SimilarityProc.class,
            StronglyConnectedComponentsProc.class,
            TraverseProc.class,
            TriangleProc.class,
            UtilityProc.class
        );
        registerFunctions(
            LinkPredictionFunc.class
        );
    }

    @AfterEach
    void tearDown() {
        db.shutdown();
    }

    @Test
    void shouldListAllThingsExceptTheListProcedure() {
        assertEquals(ALL, listProcs(null));
    }

    @Test
    void listFilteredResult() {
        assertEquals(SPANNING_TREE, listProcs("spanningTree"));
        assertEquals(singleton("algo.mst"), listProcs("algo.mst"));
        assertEquals(emptySet(), listProcs("foo"));
    }

    @Test
    void listFunctions() {
        Set<String> actual = listProcs("adamicAdar");
        actual.addAll(listProcs("linkprediction"));
        assertEquals(FUNCTIONS, actual);
    }

    @Test
    void listEmpty() {
        String query = "CALL gds.list()";
        assertEquals(
            ALL,
            runQuery(query)
                .<String>columnAs("name")
                .stream()
                .collect(Collectors.toSet())
        );
    }

    private Set<String> listProcs(Object name) {
        String query = "CALL gds.list($name)";
        return runQuery(query, MapUtil.map("name", name))
            .<String>columnAs("name")
            .stream()
            .collect(Collectors.toSet());
    }
}