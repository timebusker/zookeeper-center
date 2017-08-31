package cn.timebusker.zookeeper.center.systemconfig;

import org.springframework.context.ApplicationContext;

public class SystemConfig {


    private static ApplicationContext ctx;

    public static ApplicationContext getApplicationContext() {
        return ctx;
    }

    public static void setApplicationContext(ApplicationContext ctx) {
        SystemConfig.ctx = ctx;
    }

    /**
     * 获取zk配置
     */
    public static ZookeeperConfig getZookeeperConfig() {
        return (ZookeeperConfig) ctx.getBean("zookeeperConfig");
    }
}
