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
public class WebserviceTaskTest {
    private Logger logger = LoggerFactory.getLogger(WebserviceTaskTest.class);

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
                .addClasspathResource("activiti.test/web-service-test.bpmn20.xml")
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
        }
    }

    @Test
    public void startAndCompleteTaskTest() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("input1", 2);
        map.put("input2", 3);
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("process1", map);
        String flowInstanceId = pi.getId();
//        String flowInstanceId = "90006";
//        logger.info("流程实例id ：{}", flowInstanceId);
        List<Task> tasks = taskService.createTaskQuery().list();
        for (Task task : tasks) {
            logger.info("Task name :{},task Id : {}", task.getName(), task.getId());
            taskService.complete(task.getId());
        }
//
//        taskService.claim(flowInstanceId, "yuyong");
//        taskService.complete(flowInstanceId);
//        int output = (Integer) runtimeService.getVariable("90001", "output3");
//        System.out.println(output);
    }

}
