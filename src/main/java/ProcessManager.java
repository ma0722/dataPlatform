import org.activiti.engine.*;
import org.activiti.engine.repository.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.HashMap;

public class ProcessManager {

    private static ProcessEngine processEngine;
    private static RuntimeService runtimeService;
    private static RepositoryService repositoryService;
    private static Logger logger;
    static {
        processEngine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
        repositoryService = processEngine.getRepositoryService();
        runtimeService = processEngine.getRuntimeService();
        logger = Logger.getLogger(ProcessManager.class);
        System.out.println("processEngine" + processEngine);
    }

    public void deploymentProcessDefinition(String name, String filePath) {
        Deployment deployment = processEngine.getRepositoryService()
                .createDeployment()
                .name(name)
                .addClasspathResource(filePath)
                .deploy();
        logger.info(new Date().toString() + "部署ID:" + deployment.getId());
        logger.info(new Date().toString() + "部署名称：" + deployment.getName());
    }

    public void startProcessInstance(String processDefinitionKey, HashMap<String, Object> variables) {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);
        logger.info(new Date().toString() + "流程实例ID:" + pi.getId());
        logger.info(new Date().toString() + "流程定义ID:" + pi.getProcessDefinitionId());
        logger.info("运行实例数 " + runtimeService.createProcessInstanceQuery().count());
    }

    public void suspendProcess(String key) {
        repositoryService.suspendProcessDefinitionByKey(key);
    }

    public void activateProcess(String key) {
        repositoryService.activateProcessDefinitionByKey(key);
    }

}
