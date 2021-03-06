package com.thinkaurelius.titan.tinkerpop.rexster;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.rexster.RexsterApplicationGraph;
import com.tinkerpop.rexster.server.AbstractMapRexsterApplication;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom RexsterApplication implementation that auto-allows all extensions in the classpath.
 *
 * Should replace this with the DefaultRexsterApplication class in Rexster 2.4.0.  That class will basically do
 * what this one does to help those doing embedding to auto-configure extensions.
 *
 * @author Stephen Mallette (http://stephen.genoprime.com)
 */
public class TitanRexsterApplication extends AbstractMapRexsterApplication {

    private static final Logger logger = Logger.getLogger(TitanRexsterApplication.class);

    /**
     * Constructs the TitanRexsterApplication.
     *
     * @param graphName the name the graph will have in various Rexster contexts.
     * @param graph a graph instance.
     */
    public TitanRexsterApplication(final String graphName, final Graph graph, final List<HierarchicalConfiguration> extensionConfigurations) {
        final RexsterApplicationGraph rag = new RexsterApplicationGraph(graphName, graph);

        allowAllExtensions(rag);
        configureExtensions(extensionConfigurations, rag);

        this.graphs.put(graphName, rag);
        logger.info(String.format("Graph [%s] loaded", rag.getGraph()));
    }

    private static void configureExtensions(final List<HierarchicalConfiguration> extensionConfigurations, final RexsterApplicationGraph rag) {
        if (extensionConfigurations != null) {
            rag.loadExtensionsConfigurations(extensionConfigurations);
        }
    }

    private static void allowAllExtensions(final RexsterApplicationGraph rag) {
        final List<String> allowableNamespaces = new ArrayList<String>() {{
            add("*:*");
        }};

        rag.loadAllowableExtensions(allowableNamespaces);
    }
}
