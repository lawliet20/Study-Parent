package activiti5_demo;

import com.xiaoleilu.hutool.json.JSONUtil;
import org.activiti.engine.*;
import org.activiti.engine.impl.persistence.entity.VariableInstanceEntity;
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
public class ActivitiTest {
    private Logger logger = LoggerFactory.getLogger(ActivitiTest.class);

    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private ManagementService managementService;

    @Test
    public void showAllServiceTest() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        TaskService taskService = processEngine.getTaskService();
        ManagementService managementService = processEngine.getManagementService();
        IdentityService identityService = processEngine.getIdentityService();
        HistoryService historyService = processEngine.getHistoryService();
        FormService formService = processEngine.getFormService();
    }

    @Test
    public void deployFlowTest() {
        repositoryService.createDeployment()
                .addClasspathResource("activiti.test/VacationRequest.bpmn20.xml")
                .deploy();
        logger.info("Number of process definitions: {}", repositoryService.createProcessDefinitionQuery().count());
    }

    @Test
    public void starFlowInstanceTest() {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("employeeName", "Kermit");
        variables.put("numberOfDays", new Integer(4));
        variables.put("vacationMotivation", "I'm really tired!");
//        variables.put("startDate", DateUtil.format(DateUtil.date(),"dd-MM-yyyy hh:mm"));

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("vacationRequest", variables);

        // Verify that we started a new process instance
        logger.info("Number of process instances:{} ", runtimeService.createProcessInstanceQuery().count());
    }

    @Test
    public void queryAndCompleteTaskTest() {
        // Fetch all tasks for the management group
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
        //查询这个任务候选组中的某一个成员任务
        //List<Task> tasks = taskService.createTaskQuery().taskCandidateUser("kk").list();
        for (Task task : tasks) {
            logger.info("Task available: " + task.getName());
        }
        //complete this task
        Task task = tasks.get(0);

        Map<String, Object> taskVariables = new HashMap<String, Object>();
        taskVariables.put("vacationApproved", "false");
        taskVariables.put("managerMotivation", "We have a tight deadline!");
        taskService.complete(task.getId(), taskVariables);
    }

    @Test
    public void queryTest() {
        //api查询(查询用户Kermit认领的任务)
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee("Kermit")
//                .processVariableValueEquals("orderId", "0815")
                .orderByDueDate().asc()
                .list();
        logger.info("task assignee: {}", JSONUtil.toJsonStr(tasks));
        //原生sql查询
        String tableName = managementService.getTableName(Task.class);
        logger.info("table name is :{},{}",managementService.getTableName(Task.class),
                managementService.getTableName(VariableInstanceEntity.class));
        tasks = taskService.createNativeTaskQuery()
                .sql("SELECT count(*) FROM " + managementService.getTableName(Task.class) + " T WHERE T.NAME_ = #{taskName}")
                .parameter("taskName", "gonzoTask")
                .list();

        long count = taskService.createNativeTaskQuery()
                .sql("SELECT count(*) FROM " + managementService.getTableName(Task.class) + " T1, "
                        + managementService.getTableName(VariableInstanceEntity.class) + " V1 WHERE V1.TASK_ID_ = T1.ID_")
                .count();
        logger.info("tasks is :{}",JSONUtil.toJsonStr(tasks));
        logger.info("count is {}:",count);
    }

    /*
     *我们可以挂起一个流程定义。当挂起流程定时时， 就不能创建新流程了（会抛出一个异常）
     */
    @Test
    public void hangUpFlowTest() {
        //挂起
        try {
            repositoryService.suspendProcessDefinitionByKey("vacationRequest");
            runtimeService.startProcessInstanceByKey("vacationRequest");
        } catch (ActivitiException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void activitiFlowTest() {
        //激活
        repositoryService.activateProcessDefinitionByKey("vacationRequest");
    }


}
