package controller;


import entity.School;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.JdbcService;

@RestController
@RequestMapping(value = "jdbc", produces = "application/json;charset=UTF-8")
public class JdbcController {

    @Autowired
    private JdbcService jdbcService;

    public void setJdbcService(JdbcService jdbcService) {
        this.jdbcService = jdbcService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> loadAllSchools() {
        return ResponseEntity.ok(jdbcService.getSchoolAll());
    }

    @PostMapping("/")
    public ResponseEntity<?> createSchool(@RequestBody School school) {
        jdbcService.insertSchool(school);
        return ResponseEntity.ok("创建成功");
    }

    @PutMapping("/")
    public ResponseEntity<?> modifySchool(@RequestBody School school) {
        jdbcService.updateSchool(school);
        return ResponseEntity.ok("更新成功");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> modifySchool(@PathVariable String id) {
        jdbcService.deleteSchool(id);
        return ResponseEntity.ok("删除成功");
    }

}
