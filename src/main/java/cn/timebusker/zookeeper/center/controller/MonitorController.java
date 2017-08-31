package cn.timebusker.zookeeper.center.controller;

import java.util.List;

import javax.annotation.Resource;

import cn.timebusker.zookeeper.center.entity.JsonResult;
import cn.timebusker.zookeeper.center.entity.ZkServerInfo;
import cn.timebusker.zookeeper.center.service.ZkServerInfoService;
import cn.timebusker.zookeeper.center.wordscommand.WordsCommand;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@EnableAutoConfiguration
@RestController
@RequestMapping("/monitor")
public class MonitorController {

    @Resource
    private ZkServerInfoService service;

    @RequestMapping("/home")
    public ModelAndView mechineList() throws Exception {
        ModelAndView view = new ModelAndView();
        List<ZkServerInfo> serverInfos = service.getZkServerInfoByPage(1, 10);
        view.addObject("serverInfos", serverInfos);
        view.setViewName("server/list");
        return view;
    }

    @RequestMapping("/toAdd")
    public ModelAndView toAdd() throws Exception {
        ModelAndView view = new ModelAndView();
        view.setViewName("server/add");
        return view;
    }

    @RequestMapping("/toWordsCommand/{serverId}")
    public ModelAndView toWordsCommand(@PathVariable("serverId") int serverId) throws Exception {
        ModelAndView view = new ModelAndView();
        ZkServerInfo serverInfo = service.getZkServerInfoById(serverId);
        String conf = WordsCommand.conf(serverInfo);
        view.addObject("conf", StringUtils.replace(conf, "\n", "<br>"));
        view.setViewName("server/wordsCommand");
        return view;
    }

    /**
     * 添加zk服务器信息
     */
    @RequestMapping("/ajaxAddServerInfo")
    public JsonResult ajaxAddServerInfo(ZkServerInfo zkServerInfo) throws Exception {
        JsonResult jsonResult = new JsonResult(false, "");
        int i = service.addZkServerInfo(zkServerInfo);
        if (i > 0) {
            jsonResult.setSuccess(true);
        }
        return jsonResult;
    }

    /**
     * 通过节点的全路径获取节点信息
     */
    @RequestMapping("/toEdit/{id}")
    public ModelAndView toEdit(@PathVariable int id) throws Exception {
        ZkServerInfo zkServerInfo = service.getZkServerInfoById(id);
        ModelAndView view = new ModelAndView();
        view.addObject("zkServerInfo", zkServerInfo);
        view.setViewName("server/edit");
        return view;
    }

    @RequestMapping("/ajaxEditServerInfo")
    public JsonResult ajaxEditServerInfo(ZkServerInfo zkServerInfo) throws Exception {
        JsonResult jsonResult = new JsonResult(false, "");
        if (zkServerInfo.getId() != null && zkServerInfo.getId() > 0) {
            int i = service.updZkServerInfo(zkServerInfo);
            if (i > 0) {
                jsonResult.setSuccess(true);
            } else {
                jsonResult.setMessage("未知错误");
            }
        } else {
            jsonResult.setMessage("主键参数缺失");
        }
        return jsonResult;
    }

    /**
     * 删除一个zk服务器信息
     */
    @RequestMapping("/ajaxDelServerInfo/{id}")
    public JsonResult ajaxDelServerInfo(@PathVariable("id") int id) throws Exception {
        JsonResult jsonResult = new JsonResult(false, "");
        int i = service.delZkServerInfoById(id);
        if (i > 0) {
            jsonResult.setSuccess(true);
        }
        return jsonResult;
    }

    /**
     * 启动一台服务器
     */
    @RequestMapping("/ajaxBootServerInfo/{id}")
    public JsonResult ajaxBootServerInfo(@PathVariable("id") int id) throws Exception {
        JsonResult jsonResult = new JsonResult(false, "");
        boolean b = service.startServer(id);
        jsonResult.setSuccess(b);
        return jsonResult;
    }

    /**
     * 全部启动
     */
    @RequestMapping("/ajaxBootServerInfoAll")
    public JsonResult ajaxBootServerInfoAll() throws Exception {
        JsonResult jsonResult = new JsonResult(false, "");
        boolean b = service.startAllServer();
        jsonResult.setSuccess(b);
        return jsonResult;
    }
}
