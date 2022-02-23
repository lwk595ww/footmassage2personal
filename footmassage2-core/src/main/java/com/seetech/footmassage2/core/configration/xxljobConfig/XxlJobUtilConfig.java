package com.seetech.footmassage2.core.configration.xxljobConfig;

import com.seetech.footmassage2.core.util.XxlJobBusinessUtil;
import com.xxl.job.exposeds.xxljobrpcs.XxlJobRpcService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author lwk
 * @Date 2021/7/30 16:51
 * @Version 1.0
 * @Description
 */
@Component
public class XxlJobUtilConfig {
    @DubboReference(check = false)
    private XxlJobRpcService xxlJobRpcService;

    @PostConstruct
    public void XxlJobUtilBean() {
        XxlJobBusinessUtil xxlJobBusinessUtil = new XxlJobBusinessUtil(xxlJobRpcService);
    }

}
