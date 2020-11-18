package service;

import entity.School;

import java.util.List;

public interface JdbcService {

    void insertSchool(School school);

    void updateSchool(School school);

    void deleteSchool(String id);

    List<School> getSchoolAll();
}
