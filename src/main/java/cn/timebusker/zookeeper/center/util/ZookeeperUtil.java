package cn.timebusker.zookeeper.center.util;

import cn.timebusker.zookeeper.center.client.ZookeeperClient;

public class ZookeeperUtil {
    /**
     * 节点分隔符
     */
    public static final String SEPARATOR = "/";

    private static ZookeeperClient zkClient = null;

    public static ZookeeperClient getZkClient() {
        return zkClient;
    }

    public static void setZkClient(ZookeeperClient zkClient) {
        ZookeeperUtil.zkClient = zkClient;
    }

}
