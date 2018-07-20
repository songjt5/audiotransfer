package com.cmos.audiotransfer.resourcemanager.controller;

import com.cmos.audiotransfer.resourcemanager.manager.ResourceCapacityManager;
import com.cmos.audiotransfer.resourcemanager.manager.ResourceInfoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * Created by hejie
 * Date: 18-7-19
 * Time: 上午11:23
 * Description:
 */
@RestController public class ResouceManagementController {

    @Autowired ResourceCapacityManager resourceManager;
    @Autowired ResourceInfoManager resourceInfoManager;

/*重置*/
    @RequestMapping(value = "/resetAllResource", method = RequestMethod.GET)
    public Set<String> resetResource(@RequestParam(name = "resourceCode") String resourceCode) {
        return resourceManager
            .resetResourceEntities(resourceInfoManager.resetResources(resourceCode));
    }
}
