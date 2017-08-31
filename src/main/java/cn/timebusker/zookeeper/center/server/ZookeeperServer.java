package cn.timebusker.zookeeper.center.server;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.ServerAdminClient;
import org.apache.zookeeper.server.quorum.QuorumPeer;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig.ConfigException;
import org.apache.zookeeper.server.quorum.QuorumPeerMain;

public class ZookeeperServer {


    public static boolean bootServer(final String configPath) {
        if (StringUtils.isBlank(configPath)) {
            return false;
        }
        final CountDownLatch connectedLatch = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    QuorumPeerConfig config = new QuorumPeerConfig();
                    config.parse(configPath);
                    QuorumPeerMain peerMain = new QuorumPeerMain();
                    peerMain.runFromConfig(config);
                } catch (Exception e) {
                    connectedLatch.countDown();
                    e.printStackTrace();
                }
            }
        }).start();
        try {
            boolean b = connectedLatch.await(5000, TimeUnit.MILLISECONDS);
            return !b;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return true;
        }
    }
}
