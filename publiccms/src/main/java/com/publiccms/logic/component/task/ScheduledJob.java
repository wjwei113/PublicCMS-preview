package com.publiccms.logic.component.task;

import static com.publiccms.common.base.AbstractFreemarkerView.exposeSite;
import static com.publiccms.logic.component.site.SiteComponent.getFullFileName;
import static com.sanluan.common.tools.FreeMarkerUtils.generateStringByFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.publiccms.entities.log.LogTask;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysTask;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.log.LogTaskService;
import com.publiccms.logic.service.sys.SysSiteService;
import com.publiccms.logic.service.sys.SysTaskService;

import freemarker.template.TemplateException;

/**
 * 
 * ScheduledJob 任务计划实现类
 *
 */
public class ScheduledJob extends QuartzJobBean {
    private static String[] ignoreProperties = new String[] { "id", "begintime", "taskId", "siteId" };

    private SysTaskService sysTaskService;
    private LogTaskService logTaskService;
    private SysSiteService siteService;
    private ScheduledTask scheduledTask;
    private TemplateComponent templateComponent;

    /*
     * (non-Javadoc)
     * 
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Integer taskId = (Integer) context.getJobDetail().getJobDataMap().get(ScheduledTask.ID);
        SysTask task = sysTaskService.getEntity(taskId);
        if (null != task) {
            if (ScheduledTask.TASK_STATUS_READY == task.getStatus() && sysTaskService.updateStatusToRunning(task.getId())) {
                LogTask entity = new LogTask(task.getSiteId(), task.getId(), new Date(), false);
                logTaskService.save(entity);
                boolean success = false;
                String result;
                try {
                    success = true;
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("task", task);
                    SysSite site = siteService.getEntity(task.getSiteId());
                    exposeSite(map, site);
                    result = generateStringByFile(getFullFileName(site, task.getFilePath()),
                            templateComponent.getTaskConfiguration(), map);
                } catch (IOException | TemplateException e) {
                    result = e.getMessage();
                }
                entity.setEndtime(new Date());
                entity.setSuccess(success);
                entity.setResult(result);
                logTaskService.update(entity.getId(), entity, ignoreProperties);
                sysTaskService.updateStatus(task.getId(), ScheduledTask.TASK_STATUS_READY);

            }
        } else {
            scheduledTask.delete(taskId);
        }
    }

    public void setSysTaskService(SysTaskService sysTaskService) {
        this.sysTaskService = sysTaskService;
    }

    public void setLogTaskService(LogTaskService logTaskService) {
        this.logTaskService = logTaskService;
    }

    public void setSiteService(SysSiteService siteService) {
        this.siteService = siteService;
    }

    public void setScheduledTask(ScheduledTask scheduledTask) {
        this.scheduledTask = scheduledTask;
    }

    public void setTemplateComponent(TemplateComponent templateComponent) {
        this.templateComponent = templateComponent;
    }

}