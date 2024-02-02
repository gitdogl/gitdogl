/* 
 *
 * Copyright (C) 1999-2012 IFLYTEK Inc.All Rights Reserved. 
 * 
 * FileName：OADBUtil.java
 * 
 * Description：OA数据库访问工具类
 * 
 * History：
 * Version   Author      Date            Operation 
 * 1.0    jfzhao   2014年11月4日上午10:31:27        Create   
 */
package weaver.interfaces.hbky.oa.meet.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import weaver.conn.ConnStatement;

/**
 * @author wliu 
 *
 * @version 1.0
 * 
 */
public class OADBUtil {
    /**
     * OA数据库链接
     */
    private static Connection oaConnLocal;
    /**
     * OA系统中OA数据库链接
     */
    private static ConnStatement oaConnServer;
    /**
     * OA数据库驱动类名
     */
  private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
   //  private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    /**
     * OA数据库链接字符串
     */
   // private static final String OAURL = "jdbc:sqlserver://10.111.161.202:1433;DatabaseName=ecology18062801";
//  private static final String OAURL = "jdbc:sqlserver://10.111.161.201:1433;DatabaseName=ecology";
  private static final String OAURL = "jdbc:oracle:thin:@//10.1.9.151:1521/orcl";
   //  private static final String OAURL = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=ecology170205";
     
    /**
     * OA数据库用户名
     */
    private static final String OAACCOUNT = "ecologyuser";
    /**
     * OA数据库密码
     */
    private static final String OAPASSWORD = "ecology8#oa";//Aayonyouup!123
    /**
     * 是否本地调试
     */
    private static final boolean ISLOCAL = false;
    /**
     * 查询字符串
     */
    private String sql;
    /**
     * 
     */
    private PreparedStatement st;
    /**
     * 结果集
     */
    private ResultSet rs;

    /**
     * 将游标下移一行
     * @return 是否有下一行
     * @throws SQLException 数据库异常
     */
    public boolean next() throws SQLException {
        if (ISLOCAL) {
            return rs.next();
        } else {
            return oaConnServer.next();
        }
    }

    /**
     * 设置查询语句
     * @param sql 语句
     * @throws SQLException 数据库异常
     */
    public void setStatementSql(String sql) throws SQLException {
        this.sql = sql;
        if (ISLOCAL) {
            try {
                getOAConnLocal();
            } catch (ClassNotFoundException e) {
                throw new SQLException(e.getCause());
            }
        } else {
            getOAConnServer();
        }
    }

    /**
     * @description 
     * @author jfzhao
     * @create 2014年11月5日下午4:37:55
     * @version 1.0
     * @throws ClassNotFoundException 驱动加载异常
     * @throws SQLException 数据库异常
     */
    public void getOAConnLocal() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        oaConnLocal = DriverManager.getConnection(OAURL, OAACCOUNT, OAPASSWORD);
        st = oaConnLocal.prepareStatement(sql);
    }

    /**
     * OA数据库服务器链接
     * @throws SQLException 数据库异常
     */
    public void getOAConnServer() throws SQLException {
        oaConnServer = new ConnStatement();
        oaConnServer.setStatementSql(sql);
    }

    /**
     * 设置查询参数
     * @param sequenceid 参数序号
     * @param value 参数值
     * @throws Exception 操作异常
     */
    public void setString(int sequenceid, String value) throws Exception {
        if (ISLOCAL) {
            st.setString(sequenceid, value);
        } else {
            oaConnServer.setString(sequenceid, value);
        }
    }

    /**
     * 设置查询参数
     * @param sequenceid 参数序号
     * @param value 参数值
     * @throws SQLException 数据库异常
     */
    public void setBigDecimal(int sequenceid, BigDecimal value)
        throws SQLException {
        if (ISLOCAL) {
            st.setBigDecimal(sequenceid, value);
        } else {
            oaConnServer.setBigDecimal(sequenceid, value);
        }
    }

    /**
     * 设置查询参数
     * @param sequenceid 参数序号
     * @param value 参数值
     * @throws SQLException 数据库异常
     */
    public void setDate(int sequenceid, Date value) throws SQLException {
        if (ISLOCAL) {
            st.setDate(sequenceid, value);
        } else {
            oaConnServer.setDate(sequenceid, value);
        }
    }

    /**
     * 设置查询参数
     * @param sequenceid 参数序号
     * @param value 参数值
     * @throws SQLException 数据库异常
     */
    public void setInt(int sequenceid, int value) throws SQLException {
        if (ISLOCAL) {
            st.setInt(sequenceid, value);
        } else {
            oaConnServer.setInt(sequenceid, value);
        }
    }

    /**
     * 关闭并释放链接
     */
    public void close() {
        if (ISLOCAL) {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (oaConnLocal != null) {
                try {
                    oaConnLocal.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            oaConnServer.close();
        }
    }

    /**
     * 查询数据库
     * @throws SQLException 数据库异常
     */
    public void executeQuery() throws SQLException {
        if (ISLOCAL) {
            rs = st.executeQuery();
        } else {
            oaConnServer.executeQuery();
        }
    }

    /**
     * @description 
     * @author jfzhao
     * @create 2014年11月5日下午4:42:59
     * @version 1.0
     * @return 影响行数
     * @throws SQLException 数据库异常
     */
    public int executeUpdate() throws SQLException {
        if (ISLOCAL) {
            return st.executeUpdate();
        } else {
            return oaConnServer.executeUpdate();
        }
    }

    /**
     * 获取查询结果
     * @param columnLabel 查询字段名
     * @return 查询结果
     * @throws SQLException 数据库异常
     */
    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        if (ISLOCAL) {
            return rs.getBigDecimal(columnLabel);
        } else {
            return oaConnServer.getBigDecimal(columnLabel);
        }
    }

    /**
     * 获取查询结果
     * @param columnIndex 查询序号
     * @return 查询结果
     * @throws SQLException 数据库异常
     */
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        if (ISLOCAL) {
            return rs.getBigDecimal(columnIndex);
        } else {
            return oaConnServer.getBigDecimal(columnIndex);
        }
    }

    /**
     * 获取查询结果
     * @param columnLabel 查询字段名
     * @return 查询结果
     * @throws Exception 异常
     */
    public String getString(String columnLabel) throws Exception {
        if (ISLOCAL) {
            return rs.getString(columnLabel);
        } else {
            return oaConnServer.getString(columnLabel);
        }
    }

    /**
     * 获取查询结果
     * @param columnIndex 查询序号
     * @return 查询结果
     * @throws Exception 异常
     */
    public String getString(int columnIndex) throws Exception {
        if (ISLOCAL) {
            return rs.getString(columnIndex);
        } else {
            return oaConnServer.getString(columnIndex);
        }
    }
    
   

    /**
     * 获取查询结果
     * @param columnLabel 查询字段名
     * @return 查询结果
     * @throws SQLException 数据库异常
     */
    public Date getDate(String columnLabel) throws SQLException {
        if (ISLOCAL) {
            return rs.getDate(columnLabel);
        } else {
            return oaConnServer.getDate(columnLabel);
        }
    }

    /**
     * 获取查询结果
     * @param columnIndex 查询字段名
     * @return 查询结果
     * @throws SQLException 数据库异常
     */
    public Date getDate(int columnIndex) throws SQLException {
        if (ISLOCAL) {
            return rs.getDate(columnIndex);
        } else {
            return oaConnServer.getDate(columnIndex);
        }
    }

    /**
     * 获取查询结果
     * @param columnLabel 查询字段名
     * @return 查询结果
     * @throws SQLException 数据库异常
     */
    public int getInt(String columnLabel) throws SQLException {
        if (ISLOCAL) {
            return rs.getInt(columnLabel);
        } else {
            return oaConnServer.getInt(columnLabel);
        }
    }

    /**
     * 获取查询结果
     * @param columnIndex 查询序号
     * @return 查询结果
     * @throws SQLException 数据库异常
     */
    public int getInt(int columnIndex) throws SQLException {
        if (ISLOCAL) {
            return rs.getInt(columnIndex);
        } else {
            return oaConnServer.getInt(columnIndex);
        }
    }
}
