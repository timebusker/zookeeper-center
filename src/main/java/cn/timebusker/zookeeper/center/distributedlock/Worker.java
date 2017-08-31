package cn.timebusker.zookeeper.center.distributedlock;

public interface Worker {

    public boolean doWork() throws Exception;

}
