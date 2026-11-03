package com.studentscore.struts.action;

import com.github.pagehelper.PageInfo;
import com.opensymphony.xwork2.ActionSupport;
import com.studentscore.common.BusinessException;
import com.studentscore.common.DateUtil;
import com.studentscore.dto.StudentQuery;
import com.studentscore.entity.ClassInfo;
import com.studentscore.entity.Student;
import com.studentscore.service.ClassService;
import com.studentscore.service.StudentService;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Struts2 版学生管理：与 SpringMVC 的 StudentController 共用同一套 Service。
 * 由 struts2-spring-plugin 从 Spring 容器获取（bean id = studentAction）。
 */
@Component("studentAction")
@Scope("prototype")
public class StudentAction extends ActionSupport implements SessionAware {

    private static final long serialVersionUID = 1L;

    private static final String FLASH_MSG = "flashMsg";

    @Autowired
    private StudentService studentService;

    @Autowired
    private ClassService classService;

    /**
     * 列表查询条件（Struts2 由参数注入）。
     * 可能被提交为空串的筛选项统一声明为 String，避免类型转换错误，查询时再手动转类型。
     */
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String keyword;
    private String classId;
    private String gender;
    private String status;

    /** 表单字段 */
    private Long id;
    private String birthDate;

    /** 出参 */
    private PageInfo<Student> pageInfo;
    private List<ClassInfo> classes;
    private Student student = new Student();
    private String msg;
    private String error;

    private Map<String, Object> session;

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    /** /student/list.action */
    public String list() {
        StudentQuery query = new StudentQuery();
        query.setPageNum(pageNum);
        query.setPageSize(pageSize);
        query.setKeyword(trimToNull(keyword));
        query.setClassId(toLong(classId));
        query.setGender(trimToNull(gender));
        query.setStatus(toInteger(status));

        pageInfo = studentService.page(query);
        classes = classService.list();
        msg = takeFlashMsg();
        return SUCCESS;
    }

    /** /student/input.action：新增或编辑表单 */
    public String input() {
        Student target = id == null ? null : studentService.get(id);
        student = target == null ? new Student() : target;
        birthDate = DateUtil.formatDate(student.getBirthDate());
        classes = classService.list();
        return SUCCESS;
    }

    /** /student/save.action */
    public String save() {
        try {
            if (id != null) {
                student.setId(id);
            }
            student.setBirthDate(DateUtil.parseDate(birthDate));
            studentService.saveOrUpdate(student);
        } catch (BusinessException e) {
            error = e.getMessage();
            classes = classService.list();
            return INPUT;
        }
        putFlashMsg("学生信息保存成功");
        return SUCCESS;
    }

    /** /student/delete.action */
    public String delete() {
        studentService.delete(id);
        putFlashMsg("学生已删除");
        return SUCCESS;
    }

    private static String trimToNull(String value) {
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }

    private static Long toLong(String value) {
        String text = trimToNull(value);
        return text == null ? null : Long.valueOf(text);
    }

    private static Integer toInteger(String value) {
        String text = trimToNull(value);
        return text == null ? null : Integer.valueOf(text);
    }

    private void putFlashMsg(String text) {
        if (session != null) {
            session.put(FLASH_MSG, text);
        }
    }

    private String takeFlashMsg() {
        if (session == null) {
            return null;
        }
        Object value = session.remove(FLASH_MSG);
        return value == null ? null : String.valueOf(value);
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public PageInfo<Student> getPageInfo() {
        return pageInfo;
    }

    public List<ClassInfo> getClasses() {
        return classes;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getMsg() {
        return msg;
    }

    public String getError() {
        return error;
    }
}
