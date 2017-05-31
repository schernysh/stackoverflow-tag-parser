package org.cloudgarden.sotagparser.service;

import jodd.http.HttpRequest;
import jodd.json.JsonParser;
import jodd.petite.meta.PetiteBean;
import org.cloudgarden.sotagparser.model.Question;
import org.cloudgarden.sotagparser.model.Wrapper;

/**
 * Created by sergey on 27.05.17.
 */
@PetiteBean
public class StackoverflowService {

    private String rootUrl;
    private String advancedSearchEnpoint;
    private String siteQueryParam;
    private String filterQueryParam;

    @SuppressWarnings("unchecked")
    public Wrapper<Question> search(String query, int page, int pageSize) {
        return JsonParser.create()
                .map("items.values", Question.class)
                .parse(
                        HttpRequest
                                .get(rootUrl)
                                .path(advancedSearchEnpoint)
                                .query("site", siteQueryParam)
                                .query("filter", filterQueryParam)
                                .query("pagesize", pageSize)
                                .query("page", page)
                                .query("q", query)
                                .send()
                                .unzip()
                                .bodyText(),
                        Wrapper.class);
    }
}
