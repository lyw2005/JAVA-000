package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class JdbcUtil {

    private static final Logger logger = LoggerFactory.getLogger(JdbcUtil.class);

    private String url = null;
    private String username = null;
    private String password = null;

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            logger.error("Get connection error :", e);
        }
        return null;
    }


    public void release(Connection connection, PreparedStatement ps, ResultSet rs) {
        if (null != connection) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("Close connection error :", e);
            }
        }

        if (null != ps) {
            try {
                ps.close();
            } catch (SQLException e) {
                logger.error("Close preparedStatement error :", e);
            }
        }

        if (null != rs) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("Close resultSet error :", e);
            }
        }

    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
