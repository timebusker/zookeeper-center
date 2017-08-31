package cn.timebusker.zookeeper.center.service;

import java.util.List;

import cn.timebusker.zookeeper.center.entity.ZkServerInfo;

public interface ZkServerInfoService {

    public List<ZkServerInfo> getZkServerInfoByPage(int pageIndex, int pageSize) throws Exception;

    /**
     * 启动单个zk服务器
     */
    public boolean startServer(int id) throws Exception;

    /**
     * 添加一个zk服务器信息
     */
    public int addZkServerInfo(ZkServerInfo zkServerInfo) throws Exception;

    /**
     * 通过主键id删除一个zk服务器信息
     */
    public int delZkServerInfoById(int id) throws Exception;

    /**
     * 更新一个zk服务器信息
     */
    public int updZkServerInfo(ZkServerInfo zkServerInfo) throws Exception;

    /**
     * 通过主键id获取一个zk服务器信息
     */
    public ZkServerInfo getZkServerInfoById(int id) throws Exception;

    /**
     * 启动全部zk服务器
     */
    public boolean startAllServer() throws Exception;


}
