package com.spring.study.utils.others;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class JdbcUtils {
    public static CallableStatement close(CallableStatement cstmt) {
        try {
            if (cstmt != null)
                cstmt.close();
        } catch (Exception ex) {
        }

        return null;
    }

    /**
     * JdbcUtils constructor comment.
     */
    public JdbcUtils() {
        super();
    }

    public static PreparedStatement close(PreparedStatement pstmt) {
        try {
            if (pstmt != null)
                pstmt.close();
        } catch (Exception ex) {
        }

        return null;
    }

    public static ResultSet close(ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
        } catch (Exception ex) {
        }

        return null;
    }

    public static Statement close(Statement stmt) {
        try {
            if (stmt != null)
                stmt.close();
        } catch (Exception ex) {
        }

        return null;
    }
//public static JdbcPool getJdbcPool1(javax.servlet.ServletContext servletContext)
//{
//	SystemGate sysGate = SystemGate.getSystemGate(servletContext);
//	ServiceMgr scmg = sysGate.getServiceMgr();
//	return (JdbcPool)scmg.getService("JdbcPool1");
//}
//public static JdbcPool getSMSJdbcPool(javax.servlet.ServletContext servletContext)
//{
//	SystemGate sysGate = SystemGate.getSystemGate(servletContext);
//	ServiceMgr scmg = sysGate.getServiceMgr();
//	return (JdbcPool)scmg.getService("SMSJdbcPool");
//}
}