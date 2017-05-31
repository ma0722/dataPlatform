import org.activiti.engine.*;
import org.activiti.engine.repository.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.*;

import java.util.List;

public class Main {

    private static ProcessEngine processEngine;
    static {
        processEngine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
        System.out.println("processEngine" + processEngine);
    }

    public void deploymentProcessDefinition() {
        Deployment deployment = processEngine.getRepositoryService()// 与流程定义和部署对象相关的service
                .createDeployment()// 创建一个部署对象
                .name("helloworld入门程序")// 添加部署的名称
                .addClasspathResource("bpmn/helloworld.bpmn")// classpath的资源中加载，一次只能加载一个文件
//                .addClasspathResource("diagrams/helloworld.png")// classpath的资源中加载，一次只能加载一个文件
                .deploy();// 完成部署
        System.out.println("部署ID:" + deployment.getId());
        System.out.println("部署名称：" + deployment.getName());
    }

    public void startProcessInstance() {
        String processDefinitionKey = "HelloWorld";         // 流程定义的key
        ProcessInstance pi = processEngine.getRuntimeService()// 于正在执行的流程实例和执行对象相关的Service
                .startProcessInstanceByKey(processDefinitionKey);// 使用流程定义的key启动流程实例，key对应hellworld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动
        System.out.println("流程实例ID:" + pi.getId());// 流程实例ID 101
        System.out.println("流程定义ID:" + pi.getProcessDefinitionId()); // 流程定义ID HelloWorld:1:4
    }

    public void findMyPersonTask() {
        String assignee = "张三";
        List<Task> list = processEngine.getTaskService()// 与正在执行的认为管理相关的Service
                .createTaskQuery()// 创建任务查询对象
                .taskAssignee(assignee)// 指定个人认为查询，指定办理人
                .list();

        if (list != null && list.size() > 0) {
            for (Task task:list) {
                System.out.println("任务ID:"+task.getId());
                System.out.println("任务名称:"+task.getName());
                System.out.println("任务的创建时间"+task);
                System.out.println("任务的办理人:"+task.getAssignee());
                System.out.println("流程实例ID:"+task.getProcessInstanceId());
                System.out.println("执行对象ID:"+task.getExecutionId());
                System.out.println("流程定义ID:"+task.getProcessDefinitionId());
                System.out.println("#################################");
            }
        }
    }

    public void completeMyPersonTask(){
        //任务Id
        String taskId="104";
        processEngine.getTaskService()//与正在执行的认为管理相关的Service
                .complete(taskId);
        System.out.println("完成任务:任务ID:"+taskId);

    }

    public static void main(String[] args) {
        Main main = new Main();
        main.deploymentProcessDefinition();
        main.startProcessInstance();
        main.findMyPersonTask();
//        main.completeMyPersonTask();
    }

}
