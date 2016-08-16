package com.spring.study.utils.others;

import java.lang.reflect.Field;

import javax.swing.JLabel;

public class ClassUtils {

	public static String queryFieldsProperty(Object f) {
		StringBuilder sb = new StringBuilder("{");
		// ��ȡf�����Ӧ���е�����������
		Field[] fields = f.getClass().getDeclaredFields();
		for (int i = 0, len = fields.length; i < len; i++) {
			// ����ÿ�����ԣ���ȡ������
			String varName = fields[i].getName();
			try {
				// ��ȡԭ���ķ��ʿ���Ȩ��
				boolean accessFlag = fields[i].isAccessible();
				// �޸ķ��ʿ���Ȩ��
				fields[i].setAccessible(true);
				// ��ȡ�ڶ���f������fields[i]��Ӧ�Ķ����еı���
				Object o = fields[i].get(f);
				if(null!=o)
				{
					sb.append(varName).append("=").append(o.toString()).append(",");
				}
				else
				{
					sb.append(varName).append("=null").append(",");
				}
				//System.out.println("����Ķ����а�һ�����µı�����" + varName + " = " + o);
				// �ָ����ʿ���Ȩ��
				fields[i].setAccessible(accessFlag);
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
			} catch (IllegalAccessException ex) {
				ex.printStackTrace();
			}
		}
		if(sb.length()>1) sb.setCharAt(sb.length()-1, '}');
		return sb.toString();
	}

	public static void setAllComponentsName(Object f) {
		// ��ȡf�����Ӧ���е�����������
		Field[] fields = f.getClass().getDeclaredFields();
		for (int i = 0, len = fields.length; i < len; i++) {
			// ����ÿ�����ԣ���ȡ������
			String varName = fields[i].getName();
			try {
				// ��ȡԭ���ķ��ʿ���Ȩ��
				boolean accessFlag = fields[i].isAccessible();
				// �޸ķ��ʿ���Ȩ��
				fields[i].setAccessible(true);
				// ��ȡ�ڶ���f������fields[i]��Ӧ�Ķ����еı���
				Object o = fields[i].get(f);
				System.out.println("����Ķ����а�һ�����µı�����" + varName + " = " + o);
				// �ָ����ʿ���Ȩ��
				fields[i].setAccessible(accessFlag);
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
			} catch (IllegalAccessException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// ���Դ���������ȡһ��JLabel����������е�������������Ա���
		setAllComponentsName(new JLabel("����"));
	}

}
