package com.example.demo.util;

import com.example.demo.entity.Page;
import com.example.demo.entity.Result;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;


/**
 * @author zhangzongbo
 */
@Component
public class ElasticSearchUtil {

    public static final String INDEX_READERS = "curriculum_readers";
    public static final String MAPPINGS_PICTUREBOOK = "picturebook";
    public static final String MAPPINGS_TAG = "tag";
    private static Logger LOG = LoggerFactory.getLogger(ElasticSearchUtil.class);
    private static ElasticSearchUtil util;

    @Autowired
    private TransportClient client;

    public static IndexResponse index(String index, String type, Object obj) {
        LOG.info("插入ES数据[index={},type={},obj={}]", index, type, JsonUtil.toJSONString(obj));
        IndexRequestBuilder requestBuilder =
                util.client
                        .prepareIndex(index, type)
                        .setId(JsonUtil.getJsonNode(JsonUtil.toJSONString(obj)).path("id").asText())
                        .setSource(JsonUtil.toJSONString(obj));
        return requestBuilder.get();
    }

    public static BulkResponse bulkIndex(String index, String type, List objs) {
        LOG.info("批量插入ES数据[index={},type={},objs={}]", index, type, JsonUtil.toJSONString(objs));
        BulkRequestBuilder bulkRequestBuilder = util.client.prepareBulk();
        for (Object obj : objs) {
            bulkRequestBuilder.add(
                    util.client
                            .prepareIndex(index, type)
                            .setId(JsonUtil.getJsonNode(JsonUtil.toJSONString(obj)).path("id").asText())
                            .setSource(JsonUtil.toJSONString(obj)));
        }
        return bulkRequestBuilder.get();
    }

    public static SearchResponse search(String index, String type, QueryBuilder query, Page page) {
        if (page == null) {
            page = new Page();
        }
        SearchRequestBuilder builder =
                util.client
                        .prepareSearch(index)
                        .setTypes(type)
                        .setQuery(query)
                        .setFrom(page.getStart())
                        .setSize(page.getLimit());
        String sort = page.getSort();
        String order = page.getOrder();
        if (StringUtils.isNotEmpty(sort) && StringUtils.isNotEmpty(order)){
            builder.addSort(sort, SortOrder.fromString(order));
        }
        return builder.execute().actionGet();
    }

    public static SearchResponse searchWithFieldWithoutSort(
            String index,
            String type,
            QueryBuilder query,
            Page page,
            String[] include,
            String[] exclude) {
        if (page == null) {
            page = new Page();
        }
        SearchRequestBuilder builder =
                util.client
                        .prepareSearch(index)
                        .setTypes(type)
                        .setQuery(query)
                        .setFrom(page.getStart())
                        .setSize(page.getLimit());
        builder.setFetchSource(include, exclude);
        return builder.execute().actionGet();
    }

    public static SearchResponse searchWithFieldWithSort(
            String index,
            String type,
            QueryBuilder query,
            Page page,
            String[] include,
            String[] exclude) {
        if (page == null) {
            page = new Page();
        }
        SearchRequestBuilder builder =
                util.client
                        .prepareSearch(index)
                        .setTypes(type)
                        .setQuery(query)
                        .setFrom(page.getStart())
                        .setSize(page.getLimit());
        String sort = page.getSort();
        String order = page.getOrder();
        if (StringUtils.isNotEmpty(sort) && StringUtils.isNotEmpty(order)){
            builder.addSort(sort, SortOrder.fromString(order));
            if(!Objects.equals(Page.DEFAULT_SORT, sort)) {
                builder.addSort(Page.DEFAULT_SORT, SortOrder.ASC);
            }
        }
        builder.setFetchSource(include, exclude);
        return builder.execute().actionGet();
    }

    public static <T> Result<T> getResultFromResponse(SearchResponse response, Class<T> clazz) {
        if (response == null || clazz == null) {
            return null;
        }
        Result<T> result = new Result<>();
        List<T> list = new ArrayList();
        SearchHit[] hits = response.getHits().getHits();
        for (SearchHit hit : hits) {
            try {
                Map<String, Object> source = hit.getSource();
                if (source != null || !source.isEmpty()) {
                    T t = JsonUtil.parseObject(JsonUtil.toJSONString(source), clazz);
                    if (t != null) list.add(t);
                }
            } catch (Exception e) {
                LOG.error("ES获取数据错误：{}", e);
            }
        }
        result.setData(list);
        result.setTotal(response.getHits().getTotalHits());
        return result;
    }

    public static HashSet<Object> getValueSetFromResponse(SearchResponse response, String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        HashSet<Object> result = new HashSet<>();
        SearchHit[] hits = response.getHits().getHits();
        for (SearchHit hit : hits) {
            Map<String, Object> source = hit.getSource();
            if (source != null || !source.isEmpty()) {
                result.add(source.get(key));
            }
        }
        return result;
    }

    public static UpdateResponse update(String index, String type, String jsonString)
            throws ExecutionException, InterruptedException, IOException {
        LOG.info("更新ES数据[index={},type={},obj={}]", index, type, jsonString);
        JsonNode jsonNode = JsonUtil.getJsonNode(jsonString);
        JsonNode id = jsonNode.path("id");
        if (id != null && StringUtils.isEmpty(id.asText())) {
            return null;
        }
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(index).type(type);
        updateRequest.id(id.toString()).doc(jsonString);
        return util.client.update(updateRequest).get();
    }

    public static void delete(String index, String type, Integer id) {
        util.client.prepareDelete(index, type, id.toString()).execute().actionGet();
    }

    @PostConstruct
    public void init() {
        util = this;
        util.client = this.client;
    }
}
