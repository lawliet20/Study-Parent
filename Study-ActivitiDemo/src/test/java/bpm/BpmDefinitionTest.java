package bpm;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:holidayRequest.bpmn.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@Transactional
public class BpmDefinitionTest {
	@Resource
	private RepositoryService repositoryService;
	@Resource
	private RuntimeService runtimeService;
	@Resource 
	TaskService taskService;
	
	@Test
	public void testDeploy() throws IOException{
		InputStream is=readXmlFile();
		Assert.assertNotNull(is);
		//发布流程
		Deployment deployment=repositoryService.createDeployment().addInputStream("bpmn20.xml", is).name("holidayRequest").deploy();
		Assert.assertNotNull(deployment);
		System.out.println("deployId:" + deployment.getId());
		//查询流程定义
		ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
		
		Long businessKey=new Double(1000000*Math.random()).longValue();
		//启动流程
		runtimeService.startProcessInstanceById(processDefinition.getId(),businessKey.toString());
		//查询任务实例
		List<Task> taskList=taskService.createTaskQuery().processDefinitionId(processDefinition.getId()).list();
		Assert.assertNotNull(taskList);
		Assert.assertTrue(taskList.size()>0);
		for(Task task:taskList){
			System.out.println("task name is " + task.getName() + " ,task key is " + task.getTaskDefinitionKey());
		}
	}
	
	public InputStream readXmlFile() throws IOException{
		String filePath="holidayRequest.bpmn";
		return Class.class.getClass().getResource("/"+filePath).openStream();
	}

	/**部署流程定义*/
	@Test
	public void deploymentProcessDefinition(){
		Deployment deployment = repositoryService  //用于流程定义和部署相关对象的Service
				.createDeployment()   //创建一个部署对象
				.name("leaveBill部门程序")
				.addClasspathResource("holidayRequest.bpmn") //从ClassPath资源中加载，一次只能加载一个文件
//				.addClasspathResource("diagrams/LeaveBill.png")  //从ClassPath资源中加载，一次只能加载一个文件
				.deploy();

		System.out.println("deployment" + deployment.getId());   //1
		System.out.println("deployment" + deployment.getName());//部门程序

	}

	/**
	 * 执行流程实例
	 */
	@Test
	public void startProcessInstance(){
		String processInstanceKey = "myProcess";
		ProcessInstance pi = runtimeService
				.startProcessInstanceByKey(processInstanceKey);
		System.out.println("流程实例id：" + pi.getId());  //流程实例id  101
		System.out.println("流程定义id：" + pi.getProcessDefinitionId());   //流程定义ID helloworld:1:4
	}

	/**查询我的个人任务*/
	@Test
	public void findPersonalTaskList(){
		//任务办理人
		String assignee = "郭靖";
		List<Task> list = taskService
				.createTaskQuery()//
				.taskAssignee(assignee)//个人任务的查询
				.list();
		if(list!=null && list.size()>0){
			for(Task task:list){
				System.out.println("任务ID："+task.getId());
				System.out.println("任务的办理人："+task.getAssignee());
				System.out.println("任务名称："+task.getName());
				System.out.println("任务的创建时间："+task.getCreateTime());
				System.out.println("流程实例ID："+task.getProcessInstanceId());
				System.out.println("#######################################");
			}
		}
	}

	/**完成任务*/
	@Test
	public void completeTask(){
		//任务ID
		String taskId = "5408";
		taskService.complete(taskId);
		System.out.println("完成任务："+taskId);
	}

	/**将个人任务从一个人分配给另一个人*/
	@Test
	public void setAssignee(){
		//指定任务的办理人
		String userId = "黄蓉";
		//任务ID
		String taskId = "5408";
		taskService.setAssignee(taskId, userId);
	}
}
