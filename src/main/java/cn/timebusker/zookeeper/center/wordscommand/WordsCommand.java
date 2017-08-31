package cn.timebusker.zookeeper.center.wordscommand;

import java.io.IOException;

import cn.timebusker.zookeeper.center.entity.ResultStat;
import cn.timebusker.zookeeper.center.systemconst.CommandConstEnum;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.client.FourLetterWordMain;

import cn.timebusker.zookeeper.center.entity.ResultRuok;
import cn.timebusker.zookeeper.center.entity.ZkServerInfo;

public class WordsCommand {

    private static String[] getResultArray(ZkServerInfo serverInfo, CommandConstEnum commandConstEnum) {
        String[] resultArray = null;
        try {
            String cmdResult = FourLetterWordMain.send4LetterWord(serverInfo.getIp(), serverInfo.getPort(), commandConstEnum.getVal());
            if (StringUtils.isNotBlank(cmdResult)) {
                resultArray = cmdResult.split("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultArray;
    }

    /**
     * 四字命令stat
     */
    public static ResultStat stat(ZkServerInfo serverInfo) {
        String[] resultArray = getResultArray(serverInfo, CommandConstEnum.STAT);
        if (resultArray != null) {
            ResultStat resultStat = new ResultStat();
            for (String rs : resultArray) {
                if (rs.indexOf("Zookeeper version:") != -1) {
                    resultStat.setZookeeperVersion(rs.replace("Zookeeper version:", "").trim());
                } else if (rs.indexOf("Mode:") != -1) {
                    resultStat.setMode(StringUtils.deleteWhitespace(rs.replace("Mode:", "")));
                }
            }
            return resultStat;
        }
        return null;
    }

    /**
     * 四字命令ruok
     */
    public static ResultRuok ruok(ZkServerInfo serverInfo) {
        String[] resultArray = getResultArray(serverInfo, CommandConstEnum.RUOK);
        if (resultArray != null) {
            ResultRuok resultRuok = new ResultRuok();
            for (String rs : resultArray) {
                if (rs.indexOf("imok") != -1) {
                    resultRuok.setImok(rs.trim());
                }
            }
            return resultRuok;
        }
        return null;
    }

    /**
     * 四字命令conf
     */
    public static String conf(ZkServerInfo serverInfo) throws IOException {
        String cmdResult = FourLetterWordMain.send4LetterWord(serverInfo.getIp(), serverInfo.getPort(), CommandConstEnum.CONF.getVal());
        return cmdResult;
    }

}
