package org.cloudgarden.sotagparser.service;

import jodd.http.HttpRequest;
import jodd.json.JsonParser;
import org.cloudgarden.sotagparser.model.Question;
import org.cloudgarden.sotagparser.model.Wrapper;

/**
 * Created by sergey on 27.05.17.
 */
public class StackoverflowService {

    @SuppressWarnings("unchecked")
    public Wrapper<Question> search(String query, int page, int pageSize) {
        return JsonParser.create()
                .map("items.values", Question.class)
                .parse(
                        HttpRequest
                                .get("https://api.stackexchange.com")
                                .path("/2.2/search/advanced")
                                .query("site", "stackoverflow")
                                .query("filter", "!RsF46KGDD")
                                .query("pagesize", pageSize)
                                .query("page", page)
                                .query("q", query)
                                .send()
                                .unzip()
                                .bodyText(),
                        Wrapper.class);
    }
}
