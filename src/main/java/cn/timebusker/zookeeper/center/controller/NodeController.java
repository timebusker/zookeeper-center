package cn.timebusker.zookeeper.center.controller;

import java.util.ArrayList;
import java.util.List;

import cn.timebusker.zookeeper.center.client.ZookeeperClient;
import cn.timebusker.zookeeper.center.entity.JsonResult;
import cn.timebusker.zookeeper.center.entity.TreeNode;
import cn.timebusker.zookeeper.center.entity.ZkNode;
import cn.timebusker.zookeeper.center.util.ZookeeperUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import cn.timebusker.zookeeper.center.entity.NodeData;

@RestController
@RequestMapping("/node")
public class NodeController {
    /**
     * 节点管理首页
     */
    @RequestMapping("/home")
    public ModelAndView list() throws Exception {
        ModelAndView view = new ModelAndView();
        ZookeeperClient zkClient = ZookeeperUtil.getZkClient();
        view = new ModelAndView();
        List<String> rootNode = zkClient.getChildren(ZookeeperUtil.SEPARATOR);
        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
        for (String root : rootNode) {
            TreeNode treeNode = new TreeNode();
            treeNode.setId(ZookeeperUtil.SEPARATOR + root);
            treeNode.setName(root);
            treeNode.setParent(zkClient.hasChildren(treeNode.getId()));
            treeNode.setPId("/");
            treeNodes.add(treeNode);
        }
        TreeNode treeNode = new TreeNode();
        treeNode.setId("/");
        treeNode.setName("zkRoot");
        treeNode.setParent(treeNodes.size() > 0 ? true : false);
        treeNode.setOpen(true);
        treeNodes.add(treeNode);
        String treeNodeJson = new ObjectMapper().writeValueAsString(treeNodes);
        view.addObject("rootNodeJson", treeNodeJson);
        view.setViewName("node/list");
        return view;
    }

    /**
     * 节点树异步加载子节点
     */
    @RequestMapping("/ajaxGetChildren")
    public List<TreeNode> ajaxGetChildrenNode(@RequestParam String id) throws Exception {
        ZookeeperClient zkClient = ZookeeperUtil.getZkClient();
        List<String> childrenNode = zkClient.getChildren(id, false);
        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
        for (String node : childrenNode) {
            TreeNode treeNode = new TreeNode();
            treeNode.setId(id + ZookeeperUtil.SEPARATOR + node);
            treeNode.setName(node);
            treeNode.setParent(zkClient.hasChildren(treeNode.getId()));
            treeNodes.add(treeNode);
        }
        return treeNodes;
    }

    /**
     * 添加子节点
     */
    @RequestMapping("/ajaxAddChildren")
    public JsonResult ajaxAddChildrenNode(ZkNode zkNode) throws Exception {
        ZookeeperClient zkClient = ZookeeperUtil.getZkClient();
        JsonResult jsonResult = new JsonResult(false, "");
        if (zkClient.exists(zkNode.getpId())) {
            String fullId = zkNode.getpId() + ZookeeperUtil.SEPARATOR + zkNode.getId();
            if ("/".equals(zkNode.getpId())) {
                fullId = zkNode.getpId() + zkNode.getId();
            }
            zkNode.setFullId(fullId);
            if (zkClient.exists(zkNode.getFullId())) {
                jsonResult.setMessage("节点" + zkNode.getFullId() + "已存在");
            } else {
                byte[] data = null;
                if (StringUtils.isNotBlank(zkNode.getData())) {
                    data = zkNode.getData().getBytes();
                }
                String newNodeId = zkClient.create(zkNode.getFullId(), data, CreateMode.PERSISTENT);
                jsonResult.setSuccess(true);
                jsonResult.setMessage(newNodeId);
            }
        } else {
            jsonResult.setMessage("节点" + zkNode.getpId() + "不存在");
        }

        return jsonResult;
    }

    /**
     * 通过节点的全路径获取节点信息
     */
    @RequestMapping("/ajaxGetNodeByFullId")
    public ZkNode ajaxGetNodeByFullId(@RequestParam String fullId) throws Exception {
        ZookeeperClient zkClient = ZookeeperUtil.getZkClient();
        ZkNode zkNode = new ZkNode();
        if (zkClient.exists(fullId)) {
            zkNode.setId(fullId.substring(fullId.lastIndexOf('/') + 1));
            zkNode.setFullId(fullId);
            NodeData nodeData = zkClient.getData(fullId);
            zkNode.setData(nodeData.getData());
            zkNode.setNodeModel(nodeData.getNodeModel());
        }
        return zkNode;
    }

    /**
     * 修改节点数据
     */
    @RequestMapping("/ajaxEditNodeById")
    public JsonResult ajaxEditNodeById(ZkNode zkNode) throws Exception {
        ZookeeperClient zkClient = ZookeeperUtil.getZkClient();
        JsonResult jsonResult = new JsonResult(true, "");
        if (zkClient.exists(zkNode.getFullId())) {
            jsonResult.setSuccess(zkClient.setData(zkNode.getFullId(), zkNode.getData()));
        } else {
            jsonResult.setSuccess(false);
            jsonResult.setMessage("该节点找不到了");
        }
        return jsonResult;
    }

    /**
     * 删除节点
     */
    @RequestMapping("/ajaxDelNodeByFullId")
    public JsonResult ajaxDelNodeByFullId(@RequestParam String fullId) throws Exception {
        ZookeeperClient zkClient = ZookeeperUtil.getZkClient();
        JsonResult jsonResult = new JsonResult(false, "");
        if (!zkClient.exists(fullId)) {
            jsonResult.setMessage("节点" + fullId + "找不到了");
            return jsonResult;
        }
        if (zkClient.hasChildren(fullId)) {
            jsonResult.setMessage("节点" + fullId + "还有子节点，不能删除");
            return jsonResult;
        }
        zkClient.delete(fullId);
        jsonResult.setSuccess(true);
        return jsonResult;
    }

    /**
     * 获取子节点列表
     */
    @RequestMapping("/childrenList")
    public ModelAndView getChildrenNode(@RequestParam String id) throws Exception {
        ZookeeperClient zkClient = ZookeeperUtil.getZkClient();
        List<String> childrenNode = zkClient.getChildren(id, false);
        List<ZkNode> childrenList = new ArrayList<ZkNode>();
        if (childrenNode != null) {
            for (String node : childrenNode) {
                ZkNode zkNode = new ZkNode();
                zkNode.setId(node);
                zkNode.setpId(id);
                String fullPath = "";
                if ("/".equals(id)) {
                    fullPath = id + node;
                } else {
                    fullPath = id + ZookeeperUtil.SEPARATOR + node;
                }
                zkNode.setFullId(fullPath);
                NodeData nodeData = zkClient.getData(zkNode.getFullId());
                zkNode.setData(nodeData.getData());
                zkNode.setNodeModel(nodeData.getNodeModel());
                childrenList.add(zkNode);
            }
        }
        ModelAndView view = new ModelAndView();
        view.addObject("childrenList", childrenList);
        view.setViewName("node/childrenList");
        return view;
    }

    @RequestMapping("/toAdd")
    public ModelAndView toAdd() throws Exception {
        ModelAndView view = new ModelAndView();
        view.setViewName("node/add");
        return view;
    }

    /**
     * 通过节点的全路径获取节点信息
     */
    @RequestMapping("/toEdit")
    public ModelAndView toEdit(@RequestParam String fullId) throws Exception {
        ZookeeperClient zkClient = ZookeeperUtil.getZkClient();
        ZkNode zkNode = new ZkNode();
        if (zkClient.exists(fullId)) {
            zkNode.setId(fullId.substring(fullId.lastIndexOf('/') + 1));
            zkNode.setFullId(fullId);
            NodeData nodeData = zkClient.getData(fullId);
            zkNode.setData(nodeData.getData());
            zkNode.setNodeModel(nodeData.getNodeModel());
        }
        ModelAndView view = new ModelAndView();
        view.addObject("zkNode", zkNode);
        view.setViewName("node/edit");
        return view;
    }


}
