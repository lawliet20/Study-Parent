package com.wwj.xml与java对象互转.test3;

import com.thoughtworks.xstream.XStream;
import com.wwj.xml与java对象互转.test3.entity.Event;
import com.wwj.xml与java对象互转.test3.entity.Rettype;
import com.wwj.xml与java对象互转.test3.entity.Root;
import com.wwj.xml与java对象互转.test3.entity.State;

/**
 * Created by sherry on 2016/11/8.
 */
public class XStreamTest {
    public static void main(String[] args) {
        String xml = "<?xml version=\"1.0\"?>\n" +
                "<root>\n" +
                "    <context>cn.migu.statemachineframework.tsuit.ContextInfo</context>\n" +
                "    <states>\n" +
                "        <state start=\"\\&quot;1\\&quot;\">\n" +
                "            <name>S0</name>\n" +
                "            <events>\n" +
                "                <event>\n" +
                "                    <name>e1</name>\n" +
                "                    <objstate>S1</objstate>\n" +
                "                    <function>cn.migu.statemachineframework.tsuit.S0.onE1</function>\n" +
                "                    <rettype succval=\"\\&quot;OK\\&quot;\">java.lang.String</rettype>\"\n" +
                "                </event>\n" +
                "                <event>\n" +
                "                    <name>e2</name>\n" +
                "                    <objstate>S2</objstate>\n" +
                "                    <function>cn.migu.statemachineframework.tsuit.S0.onE2</function>\n" +
                "                    <rettype succval=\"\\&quot;0\\&quot;\">int</rettype>\n" +
                "                </event>\n" +
                "            </events>\n" +
                "        </state>\n" +
                "        <state>\n" +
                "            <name>S1</name>\n" +
                "            <events>\n" +
                "                <event>\n" +
                "                    <name>e1</name>\n" +
                "                    <objstate>S1</objstate>\n" +
                "                    <function>cn.migu.statemachineframework.tsuit.S1.onE1</function>\n" +
                "                    <rettype>cn.migu.statemachineframework.IRetValue</rettype>\"\n" +
                "                </event>\n" +
                "                <event>\n" +
                "                    <name>e2</name>\n" +
                "                    <objstate>S2</objstate>\n" +
                "                    <function>cn.migu.statemachineframework.tsuit.S1.onE2</function>\n" +
                "                    <rettype>careless</rettype>\n" +
                "                </event>\n" +
                "            </events>\n" +
                "        </state>\n" +
                "        <state>\n" +
                "            <name>S2</name>\n" +
                "            <events></events>\n" +
                "        </state>\n" +
                "    </states>\n" +
                "    <events>\n" +
                "        <event>\n" +
                "            <name>e1</name>\n" +
                "            <arg>cn.migu.statemachineframework.tsuit.E1Arg</arg>\n" +
                "        </event>\n" +
                "        <event>\n" +
                "            <name>e2</name>\n" +
                "            <arg>cn.migu.statemachineframework.tsuit.E2Arg</arg>\n" +
                "        </event>\n" +
                "        <event>\n" +
                "            <name>e3</name>\n" +
                "            <arg>java.lang.Object</arg>\n" +
                "        </event>\n" +
                "    </events>\n" +
                "</root>\n";
        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        xStream.alias("root", Root.class);
        xStream.alias("state", State.class);
        xStream.alias("event", Event.class);
        xStream.alias("rettype", Rettype.class);
        Root root = (Root) xStream.fromXML(xml);
        System.out.println(root);
    }
}
