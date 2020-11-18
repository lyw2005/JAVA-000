package service.impl;

import entity.School;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.JdbcService;
import util.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcServiceImpl implements JdbcService {

    private static final Logger logger = LoggerFactory.getLogger(JdbcServiceImpl.class);

    @Autowired
    private JdbcUtil jdbcUtil;


    @Override
    public void insertSchool(School school) {
        String sql = "insert into school(id, title, address, tel) values (?,?,?,?)";
        Connection connection = jdbcUtil.getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, school.getId());
            ps.setString(2, school.getTitle());
            ps.setString(3, school.getAddress());
            ps.setString(4, school.getTel());
            ps.execute();
        } catch (SQLException e) {
            logger.error("inert error : ", e);
        } finally {
            jdbcUtil.release(connection, ps, null);
        }
        logger.info("新增成功");
    }

    @Override
    public void updateSchool(School school) {
        String sql = "update school set title = ?, address = ?, tel = ?";
        Connection connection = jdbcUtil.getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, school.getTitle());
            ps.setString(2, school.getAddress());
            ps.setString(3, school.getTel());
            ps.execute();
        } catch (SQLException e) {
            logger.error("inert error : ", e);
        } finally {
            jdbcUtil.release(connection, ps, null);
        }
        logger.info("更新成功");
    }

    @Override
    public void deleteSchool(String id) {
        String sql = "delete from school where id = ?";
        Connection connection = jdbcUtil.getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, id);
            ps.execute();
        } catch (SQLException e) {
            logger.error("inert error : ", e);
        } finally {
            jdbcUtil.release(connection, ps, null);
        }
        logger.info("删除成功");
    }

    @Override
    public List<School> getSchoolAll() {
        String sql = "select * from school";
        Connection connection = jdbcUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<School> schools = null;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            if (null != rs) {
                schools = new ArrayList<>(rs.getRow());
                while (rs.next()) {
                    School school = new School();
                    school.setId(rs.getString(1));
                    school.setTitle(rs.getString(2));
                    school.setAddress(rs.getString(3));
                    school.setTel(rs.getString(4));
                    schools.add(school);
                }
            }
        } catch (SQLException e) {
            logger.error("select school all error : ", e);
        }
        return schools;
    }

    public void setJdbcUtil(JdbcUtil jdbcUtil) {
        this.jdbcUtil = jdbcUtil;
    }
}
