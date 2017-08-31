package cn.timebusker.zookeeper.center.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.LogManager;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig.ConfigException;
import org.apache.zookeeper.server.quorum.QuorumPeerMain;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyTest {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Test
    public void test1() throws UnknownHostException, IOException {
        String host = "127.0.0.1";
        int port = 2181;
        String cmd = "kill";
        Socket sock = new Socket(host, port);
        BufferedReader reader = null;
        try {
            OutputStream outstream = sock.getOutputStream();
            outstream.write(cmd.getBytes());
            outstream.flush();
            sock.shutdownOutput();
            reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                LOG.info(line);
            }
        } finally {
            sock.close();
            if (reader != null) {
                reader.close();
            }
        }

    }

    @Test
    public void serverMain() throws ConfigException, IOException {
        QuorumPeerConfig config = new QuorumPeerConfig();
        config.parse("E:\\zookeeper-3.4.9\\conf\\zoo1.cfg");
        String args[] = new String[]{"E:\\zookeeper-3.4.9\\conf\\zoo1.cfg"};
        QuorumPeerMain.main(args);
//		QuorumPeerMain peerMain = new QuorumPeerMain();
//		peerMain.runFromConfig(config);
        LOG.info("");
    }
}
