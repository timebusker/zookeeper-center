package cn.timebusker.zookeeper.center.dao;

import java.util.List;

import cn.timebusker.zookeeper.center.entity.ZkServerInfo;

public interface ZkServerInfoDao {

    public List<ZkServerInfo> getZkServerInfoByPage(int pageIndex, int pageSize) throws Exception;

    /**
     * 通过id获取单个zk服务的信息
     */
    public ZkServerInfo getZkServerInfoById(int id) throws Exception;

    /**
     * 添加一个zk服务器信息
     */
    public int addZkServerInfo(ZkServerInfo zkServerInfo) throws Exception;

    /**
     * 通过id删除一个zk服务器信息
     */
    public int delZkServerInfoById(int id) throws Exception;

    /**
     * 更新一个zk服务器信息
     */
    public int updZkServerInfo(ZkServerInfo zkServerInfo) throws Exception;
}
