package org.cloudgarden.sotagparser.service;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import jodd.json.JsonParser;
import jodd.log.Logger;
import jodd.log.LoggerFactory;
import jodd.petite.meta.PetiteBean;
import org.cloudgarden.sotagparser.model.Question;
import org.cloudgarden.sotagparser.model.Wrapper;

import static java.lang.String.format;

/**
 * Created by sergey on 27.05.17.
 */
@PetiteBean
public class StackoverflowService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String rootUrl;
    private String advancedSearchEnpoint;
    private String siteQueryParam;
    private String filterQueryParam;

    @SuppressWarnings("unchecked")
    public Wrapper<Question> search(String query, int page, int pageSize) {
        logger.debug(format("Performing a search request with the following parameters: [q=%s], [page=%d], [pageSize=%d]", query, page, pageSize));

        final HttpResponse response = HttpRequest
                .get(rootUrl)
                .path(advancedSearchEnpoint)
                .query("site", siteQueryParam)
                .query("filter", filterQueryParam)
                .query("pagesize", pageSize)
                .query("page", page)
                .query("q", query)
                .send()
                .unzip();
        final String body = response.bodyText();

        logger.debug(format("Search response: headers=[%s]; body=[%s]", response.headers().toString().replaceAll("\\n", ","), body));

        return JsonParser.create()
                .map("items.values", Question.class)
                .parse(body, Wrapper.class);
    }
}
