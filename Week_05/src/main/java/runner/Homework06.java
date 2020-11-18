package runner;

import entity.School;
import entity.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.JdbcService;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Homework06 {

    private static final Logger logger = LoggerFactory.getLogger(Homework06.class);

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        logger.info("context is {}", context);
        JdbcService jdbcService = (JdbcService)context.getBean("jdbcService");
        logger.info("校园列表: {}" ,jdbcService.getSchoolAll());

        String id = UUID.randomUUID().toString().replace("-","");
        School school = new School();
        school.setId(id);
        school.setTitle("北京大学");
        school.setAddress("北京市朝阳区");
        school.setTel("010-985454500");
        jdbcService.insertSchool(school);

        TimeUnit.SECONDS.sleep(2);
        logger.info("校园列表: {}" ,jdbcService.getSchoolAll());
        TimeUnit.SECONDS.sleep(2);
        school.setAddress("北京市海淀区");
        jdbcService.updateSchool(school);

        TimeUnit.SECONDS.sleep(2);
        logger.info("校园列表: {}" ,jdbcService.getSchoolAll());
        TimeUnit.SECONDS.sleep(2);
        jdbcService.deleteSchool(school.getId());

        TimeUnit.SECONDS.sleep(2);
        logger.info("校园列表: {}" ,jdbcService.getSchoolAll());
    }
}
