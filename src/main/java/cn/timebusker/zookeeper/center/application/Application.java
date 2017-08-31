package cn.timebusker.zookeeper.center.application;

import cn.timebusker.zookeeper.center.systemconfig.SystemConfig;
import cn.timebusker.zookeeper.center.util.ZookeeperUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;

import cn.timebusker.zookeeper.center.client.ZookeeperClient;
import cn.timebusker.zookeeper.center.systemconfig.ZookeeperConfig;
import cn.timebusker.zookeeper.center.viewresolver.JsonViewResolver;

@EnableAutoConfiguration
@Configuration
@ComponentScan("cn.timebusker.zookeeper.center")//指定扫描包
@EnableConfigurationProperties({ZookeeperConfig.class})
@SpringBootApplication//启动器
public class Application {
    /**
     * 上下文对象
     */
    private static ApplicationContext ac;


    public static ApplicationContext getApplicationContext() {
        return ac;
    }


    /**
     * json输出，访问时url以json结尾
     */
    @Bean
    public ViewResolver jsonViewResolver() {
        return new JsonViewResolver();
    }


    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = SpringApplication.run(Application.class);
        ac = ctx;
        SystemConfig.setApplicationContext(ctx);
        ZookeeperConfig zookeeperConfig = SystemConfig.getZookeeperConfig();
        ZookeeperClient zookeeperClient = new ZookeeperClient(zookeeperConfig.getHost(), zookeeperConfig.getTimeout());
        ZookeeperUtil.setZkClient(zookeeperClient);
    }


}
