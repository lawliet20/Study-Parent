import com.alibaba.druid.support.json.JSONUtils;
import com.wwj.model.Resource;
import com.wwj.model.User;
import com.wwj.service.ResourceService;
import com.wwj.service.UserService;
import com.wwj.utils.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.*;

/**
 * Created by sherry on 2016/10/17.
 */
public class ServiceTest {

    private ApplicationContext ac;

    @Before
    public void init(){
        ac = new ClassPathXmlApplicationContext("spring.xml");
    }

    @Test
    public void testResourceService(){
        ResourceService resourceService = (ResourceService) ac.getBean("resourceService");
        List<Resource> list = resourceService.findAll();
        System.out.println(JsonUtil.toJson(list));
    }

    @Test
    public void testUserService(){
        UserService userService = (UserService) ac.getBean("userService");
        Set<String> roles = new HashSet<String>();
        roles.add("1");
        roles.add("2");
        Set<String> pers = userService.findPermissions(roles);
        Iterator iterator = pers.iterator();
        while(iterator.hasNext()){
            System.out.println("###:"+iterator.next());
        }
    }
}
