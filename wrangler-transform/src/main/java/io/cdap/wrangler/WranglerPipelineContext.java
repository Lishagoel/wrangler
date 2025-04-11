package io.cdap.wrangler;

import io.cdap.cdap.etl.api.Lookup;
import io.cdap.cdap.etl.api.StageMetrics;
import io.cdap.cdap.etl.api.TransformContext;
import io.cdap.wrangler.api.ExecutorContext;
import io.cdap.wrangler.api.TransientStore;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class {@link WranglerPipelineContext} is a runtime context that is provided for each
 * {@link Executor} execution.
 */
class WranglerPipelineContext implements ExecutorContext {
    private final Environment environment;
    private final TransformContext context;
    private final TransientStore store;
    private final StageMetrics metrics;
    private final String name;
    private final Map<String, String> properties;

    /**
     * Constructor for WranglerPipelineContext.
     *
     * @param environment The execution environment
     * @param context The transform context
     * @param store The transient store for data
     */
    WranglerPipelineContext(Environment environment,
                            TransformContext context,
                            TransientStore store) {
        this.environment = environment;
        this.context = context;
        this.store = store;
        this.metrics = context.getMetrics();
        this.name = context.getStageName();

        this.properties = new HashMap<>();
        if (context.getArguments() != null) {
            System.out.println("Type of context.getArguments(): " + context.getArguments().getClass().getName());
            if (context.getArguments() instanceof Configuration) {
                Configuration conf = (Configuration) context.getArguments();
                for (Entry<String, String> entry : conf.iterator()) {
                    this.properties.put(entry.getKey(), entry.getValue());
                }
            } else if (context.getArguments() instanceof Map) {
                Map<String, String> argsMap = (Map<String, String>) context.getArguments();
                this.properties.putAll(argsMap);
            } else {
                System.err.println("Unexpected type for context.getArguments(): " + context.getArguments().getClass().getName());
            }
        }
    }

    @Override
    public String getNamespace() {
        return context.getNamespace();
    }

    @Override
    public Environment getEnvironment() {
        return environment;
    }

    @Override
    public StageMetrics getMetrics() {
        return metrics;
    }

    @Override
    public String getContextName() {
        return name;
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public URL getService(String applicationId, String serviceId) {
        return context.getServiceURL(applicationId, serviceId);
    }

    @Override
    public TransientStore getTransientStore() {
        return store;
    }

    @Override
    public <T> Lookup<T> provide(String s, Map<String, String> map) {
        return context.provide(s, map);
    }

    @Override
    public boolean isLast() {
        return false;
    }
}
