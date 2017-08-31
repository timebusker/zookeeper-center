package cn.timebusker.zookeeper.center.entity;

import cn.timebusker.zookeeper.center.enums.ServerModelEnum;
import cn.timebusker.zookeeper.center.enums.ServerStatusEnum;

public class ZkServerInfo {

    /**
     * 主键id
     */
    private Integer id;
    /**
     * 主机地址
     */
    private String ip;
    /**
     * 端口
     */
    private int port;
    /**
     * 服务器的运行状态
     */
    private ServerStatusEnum serverStatusEnum;
    /**
     * 服务器的运行模式
     */
    private ServerModelEnum serverModelEnum;
    /**
     * 配置文件地址
     */
    private String configPath;
    /**
     * 描述
     */
    private String desc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ServerStatusEnum getServerStatusEnum() {
        return serverStatusEnum;
    }

    public void setServerStatusEnum(ServerStatusEnum serverStatusEnum) {
        this.serverStatusEnum = serverStatusEnum;
    }

    public ServerModelEnum getServerModelEnum() {
        return serverModelEnum;
    }

    public void setServerModelEnum(ServerModelEnum serverModelEnum) {
        this.serverModelEnum = serverModelEnum;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

}
