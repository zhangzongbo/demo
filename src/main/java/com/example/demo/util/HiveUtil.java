package com.example.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.*;

/**
 * @author zhangzongbo
 * @date 19-9-17 下午3:02
 */

@Component
@Slf4j
public class HiveUtil {

    private static String driverName = "org.apache.hive.jdbc.HiveDriver";

    public static void main(String[] args) throws SQLException {
        try {
            Class.forName(driverName);
        }catch (ClassNotFoundException e){
            log.error(e.getMessage());

        }

        Connection con = DriverManager.getConnection("jdbc:hive2://l-bigdata-server48.bgd.prod.aws.dm:2181,l-bigdata-server49.bgd.prod.aws.dm:2181,l-bigdata-server50.bgd.prod.aws.dm:2181,l-bigdata-server51.bgd.prod.aws.dm:2181,l-bigdata-server52.bgd.prod.aws.dm:2181/;serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=hiveserver2");
        Statement stmt = con.createStatement();

        ResultSet res = stmt.executeQuery("show databases");
        if (res.next()){
            log.info(res.getString(1));
        }

    }
}
