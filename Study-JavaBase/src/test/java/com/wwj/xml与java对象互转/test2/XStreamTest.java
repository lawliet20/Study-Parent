package com.wwj.xml与java对象互转.test2;

import com.thoughtworks.xstream.XStream;
import com.wwj.xml与java对象互转.test2.entity.Birthday;
import com.wwj.xml与java对象互转.test2.entity.ListBean;
import com.wwj.xml与java对象互转.test2.entity.Student;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sherry on 2016/11/8.
 */
public class XStreamTest {

    private XStream xstream = null;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;

    private Student bean = null;

    /**
     * <b>function:</b>初始化资源准备
     *
     * @author hoojo
     * @createDate Nov 27, 2010 12:16:28 PM
     */
    @Before
    public void init() {
        try {
            xstream = new XStream();
            //xstream = new XStream(new DomDriver()); // 需要xpp3 jar
        } catch (Exception e) {
            e.printStackTrace();
        }
        bean = new Student();
        bean.setAddress("china");
        bean.setEmail("jack@email.com");
        bean.setId(1);
        bean.setName("jack");
        Birthday day = new Birthday();
        day.setBirthday("2010-11-22");
        bean.setBirthday(day);
    }

    /**
     * <b>function:</b>释放对象资源
     *
     * @author hoojo
     * @createDate Nov 27, 2010 12:16:38 PM
     */
    @After
    public void destory() {
        xstream = null;
        bean = null;
        try {
            if (out != null) {
                out.flush();
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.gc();
    }

    public final void fail(String string) {
        System.out.println(string);
    }

    public final void failRed(String string) {
        System.err.println(string);
    }

    /**
     * <b>function:</b>Java对象转换成XML字符串
     *
     * @author hoojo
     * @createDate Nov 27, 2010 12:19:01 PM
     */
    @Test
    public void writeBean2XML() {
        try {
            fail("------------Bean->XML------------");
            fail(xstream.toXML(bean));
            fail("重命名后的XML");
            //类重命名
            //xstream.alias("account", Student.class);
            //xstream.alias("生日", Birthday.class);
            //xstream.aliasField("生日", Student.class, "birthday");
            //xstream.aliasField("生日", Birthday.class, "birthday");
            //fail(xstream.toXML(bean));
            //属性重命名
            xstream.aliasField("邮件", Student.class, "email");
            //包重命名
            xstream.aliasPackage("hoo", "com.hoo.entity");
            fail(xstream.toXML(bean));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <b>function:</b>将Java的List集合转换成XML对象
     *
     * @author hoojo
     * @createDate Nov 27, 2010 12:20:07 PM
     */
    @Test
    public void writeList2XML() {
        try {
            //修改元素名称
            xstream.alias("beans", ListBean.class);
            xstream.alias("student", Student.class);
            fail("----------List-->XML----------");
            ListBean listBean = new ListBean();
            listBean.setName("this is a List Collection");

            List<Object> list = new ArrayList<Object>();
            list.add(bean);
            list.add(bean);//引用bean
            //list.add(listBean);//引用listBean，父元素

            bean = new Student();
            bean.setAddress("china");
            bean.setEmail("tom@125.com");
            bean.setId(2);
            bean.setName("tom");
            Birthday day = new Birthday("2010-11-22");
            bean.setBirthday(day);

            list.add(bean);
            listBean.setList(list);

            //将ListBean中的集合设置空元素，即不显示集合元素标签
            //xstream.addImplicitCollection(ListBean.class, "list");

            //设置reference模型
            xstream.setMode(XStream.NO_REFERENCES);//不引用
            //xstream.setMode(XStream.ID_REFERENCES);//id引用
            //xstream.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);//绝对路径引用

            //将name设置为父类（Student）的元素的属性
            xstream.useAttributeFor(Student.class, "name");
            xstream.useAttributeFor(Birthday.class, "birthday");
            //修改属性的name
            xstream.aliasAttribute("姓名", "name");
            xstream.aliasField("生日", Birthday.class, "birthday");

            fail(xstream.toXML(listBean));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <b>function:</b>用InputStream将XML文档转换成java对象
     * 需要额外的jar xpp3-main.jar
     *
     * @author hoojo
     * @createDate Nov 27, 2010 1:14:52 PM
     */
    @Test
    public void readXML4InputStream() {
        try {
            String s = "<beans>\n" +
                    "  <name>this is a List Collection</name>\n" +
                    "  <list>\n" +
                    "    <student>\n" +
                    "      <id>1</id>\n" +
                    "      <name>jack</name>\n" +
                    "      <email>jack@email.com</email>\n" +
                    "      <address>china</address>\n" +
                    "      <birthday>\n" +
                    "        <strBirthday>2010-11-22</strBirthday>\n" +
                    "      </birthday>\n" +
                    "    </student>\n" +
                    "    <student>\n" +
                    "      <id>1</id>\n" +
                    "      <name>jack</name>\n" +
                    "      <email>jack@email.com</email>\n" +
                    "      <address>china</address>\n" +
                    "      <birthday>\n" +
                    "        <strBirthday>2010-11-22</strBirthday>\n" +
                    "      </birthday>\n" +
                    "    </student>\n" +
                    "    <student>\n" +
                    "      <id>2</id>\n" +
                    "      <name>tom</name>\n" +
                    "      <email>tom@125.com</email>\n" +
                    "      <address>china</address>\n" +
                    "      <birthday>\n" +
                    "        <strBirthday>2010-11-22</strBirthday>\n" +
                    "      </birthday>\n" +
                    "    </student>\n" +
                    "  </list>\n" +
                    "</beans>";
            failRed("---------ObjectInputStream## XML --> javaObject---------");
            StringReader reader = new StringReader(s);
            in = xstream.createObjectInputStream(reader);
            ListBean listBean = (ListBean) in.readObject();
//            Student stu = (Student) in.readObject();
//            Birthday b = (Birthday) in.readObject();
//            byte i = in.readByte();
//            boolean bo = in.readBoolean();
//            float f = in.readFloat();
//            String str = in.readUTF();
//            System.out.println(stu);
//            System.out.println(b);
//            System.out.println(i);
//            System.out.println(bo);
//            System.out.println(f);
//            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
