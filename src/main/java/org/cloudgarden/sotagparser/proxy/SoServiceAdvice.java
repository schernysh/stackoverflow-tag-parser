package org.cloudgarden.sotagparser.proxy;

import com.google.common.base.Stopwatch;
import jodd.proxetta.ProxyAdvice;
import jodd.proxetta.ProxyTarget;

import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

/**
 * Created by schernysh on 7/3/17.
 */
public class SoServiceAdvice implements ProxyAdvice {

    @Override
    public Object execute() throws Exception {
        final Stopwatch stopwatch = Stopwatch.createStarted();
        final Object result = ProxyTarget.invoke();
        stopwatch.stop();

        SoServiceAdviceSupport.metrics.sample(format("%s.%s", ProxyTarget.targetClass().getSimpleName(), ProxyTarget.targetMethodName()),
                stopwatch.elapsed().toMillis(), TimeUnit.MILLISECONDS);

        return result;
    }
}
