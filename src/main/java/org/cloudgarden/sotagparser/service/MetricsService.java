package org.cloudgarden.sotagparser.service;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import jodd.petite.meta.PetiteBean;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by schernysh on 7/3/17.
 */
@PetiteBean
public class MetricsService {

    private final MetricRegistry metrics;

    public MetricsService() {
        this.metrics = new MetricRegistry();
    }

    public void sample(String meter, long duration, TimeUnit unit) {
        metrics.timer(meter).update(duration, unit);
    }

    public Map<String, Timer> getTimers() {
        return metrics.getTimers();
    }
}
