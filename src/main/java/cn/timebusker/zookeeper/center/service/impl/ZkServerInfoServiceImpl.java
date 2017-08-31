package cn.timebusker.zookeeper.center.service.impl;

import java.util.List;

import javax.annotation.Resource;

import cn.timebusker.zookeeper.center.dao.ZkServerInfoDao;
import cn.timebusker.zookeeper.center.entity.ResultRuok;
import cn.timebusker.zookeeper.center.entity.ResultStat;
import cn.timebusker.zookeeper.center.entity.ZkServerInfo;
import cn.timebusker.zookeeper.center.enums.ServerModelEnum;
import cn.timebusker.zookeeper.center.enums.ServerStatusEnum;
import cn.timebusker.zookeeper.center.server.ZookeeperServer;
import cn.timebusker.zookeeper.center.service.ZkServerInfoService;
import cn.timebusker.zookeeper.center.wordscommand.WordsCommand;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ZkServerInfoServiceImpl implements ZkServerInfoService {

    @Resource
    private ZkServerInfoDao zkServerInfoDao;

    @Override
    public List<ZkServerInfo> getZkServerInfoByPage(int pageIndex, int pageSize)
            throws Exception {
        List<ZkServerInfo> infos = zkServerInfoDao.getZkServerInfoByPage(pageIndex, pageSize);
        for (ZkServerInfo info : infos) {
            ResultStat resultStat = WordsCommand.stat(info);
            if (resultStat != null) {
                ResultRuok ruok = WordsCommand.ruok(info);
                if (ruok == null) {
                    info.setServerStatusEnum(ServerStatusEnum.OFFLINE);
                    continue;
                }
                if (StringUtils.isNotBlank(ruok.getImok())) {
                    info.setServerStatusEnum(ServerStatusEnum.ONLINE);
                    info.setServerModelEnum(ServerModelEnum.getInstance(resultStat.getMode()));
                } else {
                    info.setServerStatusEnum(ServerStatusEnum.EXCEPTIOM);
                }
            } else {
                info.setServerStatusEnum(ServerStatusEnum.OFFLINE);
            }
        }
        return infos;
    }

    @Override
    public boolean startServer(int id) throws Exception {
        ZkServerInfo zkServerInfo = zkServerInfoDao.getZkServerInfoById(id);
        if (zkServerInfo == null) {
            return false;
        }
        return ZookeeperServer.bootServer(zkServerInfo.getConfigPath());
    }

    @Override
    public int addZkServerInfo(ZkServerInfo zkServerInfo) throws Exception {

        return zkServerInfoDao.addZkServerInfo(zkServerInfo);
    }

    @Override
    public int delZkServerInfoById(int id) throws Exception {
        return zkServerInfoDao.delZkServerInfoById(id);
    }

    @Override
    public int updZkServerInfo(ZkServerInfo zkServerInfo) throws Exception {

        return zkServerInfoDao.updZkServerInfo(zkServerInfo);
    }

    @Override
    public ZkServerInfo getZkServerInfoById(int id) throws Exception {

        return zkServerInfoDao.getZkServerInfoById(id);
    }

    @Override
    public boolean startAllServer() throws Exception {
        List<ZkServerInfo> infos = zkServerInfoDao.getZkServerInfoByPage(1, 1);
        for (ZkServerInfo serverInfo : infos) {
            ZookeeperServer.bootServer(serverInfo.getConfigPath());
        }
        return true;
    }

}
