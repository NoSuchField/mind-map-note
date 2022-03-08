package com.mindmapnote.core.scheduler;

import com.mindmapnote.core.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 定时移除孤儿节点
 *
 * @author : XinYu
 * @date : 2022-03-08 23:05
 */
public class RemoveOrphanNodeTask {

    @Autowired
    private NodeService nodeService;

    @Scheduled(cron = "0 */10 * * * ?")
    public void cleanup() {
        nodeService.removeOrphanNodes();
    }
}
