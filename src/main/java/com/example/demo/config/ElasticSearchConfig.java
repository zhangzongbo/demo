//package com.example.demo.config;
//
//import org.apache.commons.lang3.StringUtils;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
//import org.elasticsearch.transport.client.PreBuiltTransportClient;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by wanggang1 on 2017/4/5.
// */
//@Configuration
//public class ElasticSearchConfig {
//
//    private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchConfig.class);
//
//    @Value("${elasticsearch.cluster.name}")
//    private String clusterName;
//
//    @Value("${elasticsearch.cluster.host}")
//    private String hosts;
//
//    @Bean
//    public TransportClient transportClient() {
//        TransportClient client = null;
//        Settings settings = Settings.builder().put("cluster.name", clusterName)
//                .put("search.default_search_timeout", 2, TimeUnit.SECONDS).build();
//        try {
//            String[] hostsArray = StringUtils.split(hosts, ";");
//            client = new PreBuiltTransportClient(settings);
//            for (String host : hostsArray) {
//                String[] hostS = host.split(":");
//                client.addTransportAddress(
//                        new InetSocketTransportAddress(
//                                InetAddress.getByName(hostS[0]), Integer.parseInt(hostS[1])));
//            }
//        } catch (UnknownHostException e) {
//            LOG.error("ElasticSearch 注册失败:{}", e);
//        }
//        return client;
//    }
//}
