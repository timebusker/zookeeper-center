package cn.timebusker.zookeeper.center.client;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cn.timebusker.zookeeper.center.enums.NodeModelEnum;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import cn.timebusker.zookeeper.center.entity.NodeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZookeeperClient implements Watcher {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    /**
     * 实例
     */
    private ZooKeeper zookeeper;
    /**
     * 服务器地址
     */
    private String connectString;
    /**
     * 超时时间
     */
    private int sessionTimeout = 50000;
    /**
     * 长度为1的计数器 用于处理zk连接是否成功
     */
    private CountDownLatch connectedLatch = new CountDownLatch(1);

    /**
     * 构造方法
     */
    public ZookeeperClient(String connectString, int sessionTimeout) throws Exception {
        this.connectString = connectString;
        this.sessionTimeout = sessionTimeout;
        zookeeper = new ZooKeeper(this.connectString, this.sessionTimeout, this);
        boolean isConnectin = connectedLatch.await(20l, TimeUnit.SECONDS);
        if (isConnectin) {
            LOG.info("connecting zookeeper server success.");
        } else {
            LOG.info("connecting zookeeper server failed.");
        }
    }

    /**
     * 节点是否存在
     */
    public boolean exists(String path, boolean watch) throws KeeperException, InterruptedException {
        Stat stat = zookeeper.exists(path, watch);
        return stat != null;
    }

    public boolean exists(String path) throws KeeperException, InterruptedException {
        return this.exists(path, false);
    }

    /**
     * 获取数据
     */
    public String getData(String path, Watcher watcher, Stat stat) throws KeeperException, InterruptedException {
        byte[] data = zookeeper.getData(path, watcher, stat);
        String dataStr = "";
        if (data != null) {
            dataStr = new String(data);
        }
        return dataStr;
    }

    /**
     * 获取数据
     */
    public NodeData getData(String path, boolean watcher, Stat stat) throws KeeperException, InterruptedException {
        byte[] data = zookeeper.getData(path, watcher, stat);
        String dataStr = "";
        if (data != null) {
            dataStr = new String(data);
        }
        NodeData nodeData = new NodeData();
        nodeData.setData(dataStr);
        if (stat.getEphemeralOwner() != 0) {
            nodeData.setNodeModel(NodeModelEnum.EPHEMERAL);
        } else {
            nodeData.setNodeModel(NodeModelEnum.PERSISTENT);
        }
        return nodeData;
    }

    public boolean setData(String path, String data) throws KeeperException, InterruptedException {
        Stat stat = zookeeper.setData(path, data.getBytes(), -1);
        return stat != null;
    }

    public NodeData getData(String path) throws KeeperException, InterruptedException {
        return this.getData(path, false, new Stat());
    }

    /**
     * 创建节点
     */
    public String create(String path, byte data[],
                         CreateMode createMode) throws KeeperException, InterruptedException {
        String result = zookeeper.create(path, data, Ids.OPEN_ACL_UNSAFE, createMode);
        return result;
    }

    /**
     * 服务连接监听
     */
    @Override
    public void process(WatchedEvent event) {
        if (event == null)
            return;
        //成功连接服务器
        if (KeeperState.SyncConnected == event.getState()) {
            if (EventType.None == event.getType()) {
                LOG.info("zookeeper connect success");
                //成功连接，计数器-1
                connectedLatch.countDown();
            }
        } else if (KeeperState.Disconnected == event.getState()) {
            LOG.info("zookeeper Disconnected");
        } else if (KeeperState.AuthFailed == event.getState()) {
            LOG.info("zookeeper AuthFailed");
        } else if (KeeperState.Expired == event.getState()) {
            LOG.info("zookeeper Expired");
        }

    }

    /**
     * 删除节点
     */
    public void delete(String path) throws KeeperException, InterruptedException {
        zookeeper.delete(path, -1);
    }

    /**
     * 获取子节点列表
     */
    public List<String> getChildren(String path, boolean watch) throws KeeperException, InterruptedException {
        List<String> childrenNodes = zookeeper.getChildren(path, watch);
        return childrenNodes;
    }

    /**
     * 获取子节点列表
     */
    public List<String> getChildren(String path) throws KeeperException, InterruptedException {
        List<String> childrenNodes = zookeeper.getChildren(path, false);
        return childrenNodes;
    }

    public boolean hasChildren(String path) throws KeeperException, InterruptedException {
        List<String> childrenNodes = this.getChildren(path);
        if (childrenNodes == null || childrenNodes.size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * 释放连接
     */
    public void releaseConnection() {
        try {
            if (zookeeper != null) {
                zookeeper.close();
            }
            LOG.info("zookeeper disconnect.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
