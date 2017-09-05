package activiti5_demo;

import com.xiaoleilu.hutool.json.JSONUtil;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:config/spring-activiti.xml"})
public class WebserviceTask2Test {
    private Logger logger = LoggerFactory.getLogger(WebserviceTask2Test.class);

    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private ManagementService managementService;

    @Test
    public void deployFlow() {
        repositoryService.createDeployment()
                .addClasspathResource("activiti.test/web-service-test2.bpmn20.xml")
                .deploy();
        logger.info("Number of process definitions: {}", repositoryService.createProcessDefinitionQuery().count());
        logger.info("流程信息：{}", JSONUtil.toJsonStr(repositoryService.createProcessDefinitionQuery().active()));

    }

    @Test
    public void queryAndCompleteTaskTest() {
        // Fetch all tasks for the management group
        List<Task> tasks = taskService.createTaskQuery().list();
        for (Task task : tasks) {
            logger.info("Task name :{},task Id : {}", task.getName(), task.getId());
            String taskId = task.getId();
            taskService.complete(taskId);
        }
    }

    @Test
    public void startAndCompleteTaskTest() {
        // 初始化参数
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("nameVar", "mpc_test");
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(
                "service1", vars);
        // 完成第一个任务
        Task task = taskService.createTaskQuery().singleResult();
        taskService.complete(task.getId());
        // 输出调用Web Service后的参数
        String add = (String) runtimeService.getVariable(pi.getId(), "addVar");
        System.out.println("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
        System.out.println();
        System.out.println();
        System.out.println("mpc_test现在居住的地点是—————————————————————————>" + add);
        System.out.println();
        System.out.println();
        System.out.println("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");

    }

}
