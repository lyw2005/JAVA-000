package runner;

import entity.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Homework02 {

    private static final Logger logger = LoggerFactory.getLogger(Homework02.class);

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        logger.info("context is {}", context);
        Student student = (Student) context.getBean("student");
        logger.info("student is {}, name is {}, age is {}", student, student.getName(), student.getAge());

    }
}
