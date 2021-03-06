package org.cloudgarden.sotagparser.service;

import jodd.log.Logger;
import jodd.log.LoggerFactory;
import jodd.petite.meta.PetiteBean;
import jodd.petite.meta.PetiteInject;
import one.util.streamex.IntStreamEx;
import org.cloudgarden.sotagparser.model.Wrapper;

import java.util.Map;

import static java.lang.String.format;
import static java.util.Comparator.reverseOrder;

/**
 * Created by sergey on 01.06.17.
 */
@PetiteBean
public class ParserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private int pageSize;
    private int maxPageNum;
    private int topNumber;

    @PetiteInject
    private StackoverflowService stackoverflowService;

    @PetiteInject
    private MetricsService metrics;

    public void parseAndPrint(String query) {
        IntStreamEx.rangeClosed(1, maxPageNum)
                .mapToObj(page -> stackoverflowService.search(query, page, pageSize))
                .takeWhile(Wrapper::isHas_more)
                .flatMap(wrapper -> wrapper.getItems().stream())
                .flatMap(question -> question.getTags().stream())
                .sorted()
                .runLengths()
                .sorted(Map.Entry.comparingByValue(reverseOrder()))
                .limit(topNumber)
                .map(e -> format("%s x %d", e.getKey(), e.getValue()))
                .forEach(logger::info);

        logger.debug("HTTP requests statistics:");
        logger.debug(format("%-39s%-9s%-8s", "request", "count", "avg time (ms)"));
        metrics.getTimers().entrySet().stream()
                .map(t -> format("%-39s%-9d%-8.2f", t.getKey(), t.getValue().getCount(), t.getValue().getSnapshot().getMean() / 1_000_000))
                .forEach(logger::debug);
    }
}
