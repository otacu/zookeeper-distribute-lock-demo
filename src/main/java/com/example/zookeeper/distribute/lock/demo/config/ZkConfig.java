package com.example.zookeeper.distribute.lock.demo.config;

import com.example.zookeeper.distribute.lock.demo.client.ZookeeperClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 自动化配置核心数据库的连接配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix="zk")
@PropertySource("classpath:zookeeper.properties")
public class ZkConfig {

    String host;
    String lockPath;

    /**
     * 这是最快的数据库连接池
     * @return
     */
    @Bean
    public ZookeeperClient zookeeperClient(){
        return new ZookeeperClient(this.host,this.lockPath);
    }

}