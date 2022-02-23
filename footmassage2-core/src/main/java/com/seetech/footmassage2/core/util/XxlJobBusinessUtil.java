package com.seetech.footmassage2.core.util;


import com.alibaba.nacos.common.utils.StringUtils;
import com.seetech.footmassage2.core.configration.exceptionConfig.MyException;
import com.xxl.job.core.context.XxlJobContext;
import com.xxl.job.exposeds.dto.req.XxlJobAddReq;
import com.xxl.job.exposeds.dto.res.JobParamRes;
import com.xxl.job.exposeds.xxljobrpcs.XxlJobRpcService;


/**
 *  常用定时任务封装
 */
public class XxlJobBusinessUtil {

    private static XxlJobRpcService xxlJobRpcService;

    public XxlJobBusinessUtil(XxlJobRpcService xxlJobRpcService) {
        XxlJobBusinessUtil.xxlJobRpcService = xxlJobRpcService;
    }


    /**
     * 创建以主键id关联的定时任务
     *
     * @param jobGroup
     * @param desc
     * @param cron
     * @param id
     */
    public static void expireTimeJobAdd(Integer jobGroup, String desc, String cron, String executorHandler, String id, String tenantId, int tryCount) {
        XxlJobAddReq xxlJobAddReq = new XxlJobAddReq();
        xxlJobAddReq.setJobGroup(jobGroup);
        xxlJobAddReq.setJobDesc(desc);
        xxlJobAddReq.setScheduleConf(cron);
        //执行的处理器
        xxlJobAddReq.setExecutorHandler(executorHandler);
        //关联的业务id
        xxlJobAddReq.setRelateId(id);
        //额外信息
        xxlJobAddReq.setExtraRelateInfo(tenantId);
        //失败重试次数
        xxlJobAddReq.setExecutorFailRetryCount(tryCount);
        try {
            Integer jobId = xxlJobRpcService.jobAdd(xxlJobAddReq);

            //启动
            xxlJobRpcService.jobStart(jobId);
        } catch (Exception e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 获取当前定时任务参数
     * @return
     */
    public static JobParamRes getJobParam() {
        XxlJobContext xxlJobContext = XxlJobContext.getXxlJobContext();
        if (xxlJobContext == null) {
            return null;
        }
        String jobParam = xxlJobContext.getJobParam();
        JobParamRes jobParamRes = null;
        if (!StringUtils.isBlank(jobParam)) {
            jobParamRes = JsonUtils.toObject(jobParam, JobParamRes.class);
        }
        return jobParamRes;
    }

}
