package org.cloudgarden.sotagparser.service;

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

    private int pageSize;
    private int maxPageNum;
    private int topNumber;

    @PetiteInject
    private StackoverflowService stackoverflowService;

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
                .forEach(System.out::println);
    }
}
