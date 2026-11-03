-- =====================================================================
-- 4A班级学生成绩管理系统  数据库脚本（MySQL 8.0）
-- 执行方式：mysql -uroot -p < studentscore.sql
--          或在 Navicat / IDEA Database 中整体执行
-- 测试账号：admin / teacher / 2024001    密码均为 123456
-- =====================================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS studentscore DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE studentscore;

DROP TABLE IF EXISTS sys_log;
DROP TABLE IF EXISTS score;
DROP TABLE IF EXISTS course;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS class_info;
DROP TABLE IF EXISTS teacher;
DROP TABLE IF EXISTS sys_user;

-- ----------------------------
-- 教师
-- ----------------------------
CREATE TABLE teacher (
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    teacher_no VARCHAR(20) NOT NULL UNIQUE COMMENT '工号',
    name       VARCHAR(30) NOT NULL COMMENT '姓名',
    title      VARCHAR(20) COMMENT '职称',
    phone      VARCHAR(20)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='教师';

-- ----------------------------
-- 班级
-- ----------------------------
CREATE TABLE class_info (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    class_name      VARCHAR(30) NOT NULL UNIQUE COMMENT '班级名称，如 4A班',
    grade           VARCHAR(20) COMMENT '年级',
    major           VARCHAR(50) COMMENT '专业',
    head_teacher_id BIGINT COMMENT '班主任',
    remark          VARCHAR(200)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='班级';

-- ----------------------------
-- 学生
-- ----------------------------
CREATE TABLE student (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_no  VARCHAR(20) NOT NULL UNIQUE COMMENT '学号',
    name        VARCHAR(30) NOT NULL COMMENT '姓名',
    gender      CHAR(1) DEFAULT '男' COMMENT '性别：男/女',
    birth_date  DATE COMMENT '出生日期',
    phone       VARCHAR(20),
    class_id    BIGINT COMMENT '所属班级',
    status      TINYINT DEFAULT 1 COMMENT '1-在读 0-离校',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_student_class (class_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='学生';

-- ----------------------------
-- 课程
-- ----------------------------
CREATE TABLE course (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_no   VARCHAR(20) NOT NULL UNIQUE COMMENT '课程编号',
    course_name VARCHAR(50) NOT NULL COMMENT '课程名称',
    credit      DECIMAL(3, 1) DEFAULT 3.0 COMMENT '学分',
    teacher_id  BIGINT COMMENT '授课教师',
    semester    VARCHAR(20) COMMENT '开课学期，如 2025-2026-1'
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='课程';

-- ----------------------------
-- 成绩
-- ----------------------------
CREATE TABLE score (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id  BIGINT        NOT NULL,
    course_id   BIGINT        NOT NULL,
    usual_score DECIMAL(5, 1) COMMENT '平时成绩',
    exam_score  DECIMAL(5, 1) COMMENT '期末成绩',
    total_score DECIMAL(5, 1) COMMENT '总评成绩 = 平时*0.3 + 期末*0.7',
    semester    VARCHAR(20)   NOT NULL COMMENT '学期',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_stu_course_sem (student_id, course_id, semester),
    KEY idx_score_course (course_id),
    KEY idx_score_semester (semester)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='成绩';

-- ----------------------------
-- 系统账号
-- ----------------------------
CREATE TABLE sys_user (
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    username  VARCHAR(30) NOT NULL UNIQUE COMMENT '登录账号',
    password  VARCHAR(64) NOT NULL COMMENT 'MD5 摘要',
    role      VARCHAR(10) NOT NULL COMMENT 'ADMIN/TEACHER/STUDENT',
    real_name VARCHAR(30) COMMENT '姓名',
    ref_id    BIGINT COMMENT '关联 teacher.id 或 student.id',
    status    TINYINT DEFAULT 1 COMMENT '1-正常 0-禁用'
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='系统账号';

-- ----------------------------
-- 操作日志
-- ----------------------------
CREATE TABLE sys_log (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id     BIGINT COMMENT '操作人账号ID',
    username    VARCHAR(30) COMMENT '操作人账号',
    module      VARCHAR(30) COMMENT '模块',
    operation   VARCHAR(200) COMMENT '操作内容',
    ip          VARCHAR(50) COMMENT '来源IP',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_log_time (create_time)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='操作日志';

-- =====================================================================
-- 演示数据
-- =====================================================================
INSERT INTO teacher (id, teacher_no, name, title, phone)
VALUES (1, 'T001', '王建国', '副教授', '13800000001'),
       (2, 'T002', '李梅', '讲师', '13800000002'),
       (3, 'T003', '赵强', '助教', '13800000003');

INSERT INTO class_info (id, class_name, grade, major, head_teacher_id, remark)
VALUES (1, '4A班', '2024级', '软件技术', 1, '本系统主要管理班级');

INSERT INTO student (id, student_no, name, gender, birth_date, phone, class_id, status)
VALUES (1, '2024001', '张伟', '男', '2006-03-12', '13900000001', 1, 1),
       (2, '2024002', '王芳', '女', '2006-05-21', '13900000002', 1, 1),
       (3, '2024003', '李强', '男', '2006-01-09', '13900000003', 1, 1),
       (4, '2024004', '刘洋', '男', '2006-07-30', '13900000004', 1, 1),
       (5, '2024005', '陈静', '女', '2006-09-14', '13900000005', 1, 1),
       (6, '2024006', '杨帆', '男', '2006-11-02', '13900000006', 1, 1),
       (7, '2024007', '赵磊', '男', '2006-02-18', '13900000007', 1, 1),
       (8, '2024008', '黄敏', '女', '2006-04-25', '13900000008', 1, 1),
       (9, '2024009', '周杰', '男', '2006-06-08', '13900000009', 1, 1),
       (10, '2024010', '吴倩', '女', '2006-08-16', '13900000010', 1, 1),
       (11, '2024011', '徐涛', '男', '2006-10-05', '13900000011', 1, 1),
       (12, '2024012', '孙丽', '女', '2006-12-23', '13900000012', 1, 1),
       (13, '2024013', '马超', '男', '2006-03-03', '13900000013', 1, 1),
       (14, '2024014', '朱婷', '女', '2006-07-11', '13900000014', 1, 1),
       (15, '2024015', '胡军', '男', '2006-09-29', '13900000015', 1, 1);

INSERT INTO course (id, course_no, course_name, credit, teacher_id, semester)
VALUES (1, 'C001', 'Java程序设计', 4.0, 1, '2025-2026-1'),
       (2, 'C002', '数据库原理与应用', 3.5, 2, '2025-2026-1'),
       (3, 'C003', '数据结构', 4.0, 1, '2025-2026-1'),
       (4, 'C004', 'Web前端开发', 3.0, 3, '2025-2026-1'),
       (5, 'C005', '计算机网络', 3.0, 2, '2025-2026-1');

-- 演示成绩：按学号与课程编号生成有区分度的分数
INSERT INTO score (student_id, course_id, usual_score, exam_score, total_score, semester)
SELECT t.sid,
       t.cid,
       t.usual,
       t.exam,
       ROUND(t.usual * 0.3 + t.exam * 0.7, 1),
       '2025-2026-1'
FROM (SELECT s.id AS sid,
             c.id AS cid,
             ROUND(65 + MOD(s.id * 7 + c.id * 13, 30), 1) AS usual,
             ROUND(50 + MOD(s.id * 11 + c.id * 17, 50), 1) AS exam
      FROM student s
               JOIN course c) t;

-- 账号：密码均为 123456（MD5）
INSERT INTO sys_user (id, username, password, role, real_name, ref_id, status)
VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 'ADMIN', '系统管理员', NULL, 1),
       (2, 'teacher', 'e10adc3949ba59abbe56e057f20f883e', 'TEACHER', '王建国', 1, 1),
       (3, '2024001', 'e10adc3949ba59abbe56e057f20f883e', 'STUDENT', '张伟', 1, 1);

-- 其余两位教师账号（李梅=教师2，赵强=教师3）；已有账号的教师跳过，避免重复
INSERT INTO sys_user (username, password, role, real_name, ref_id, status)
SELECT t.teacher_no, 'e10adc3949ba59abbe56e057f20f883e', 'TEACHER', t.name, t.id, 1
FROM teacher t
WHERE NOT EXISTS (SELECT 1 FROM sys_user u WHERE u.role = 'TEACHER' AND u.ref_id = t.id);

-- 为所有在读学生生成账号（账号=学号，可用「账号管理 → 批量生成学生账号」补齐新学生）
INSERT INTO sys_user (username, password, role, real_name, ref_id, status)
SELECT s.student_no, 'e10adc3949ba59abbe56e057f20f883e', 'STUDENT', s.name, s.id, 1
FROM student s
WHERE NOT EXISTS (SELECT 1 FROM sys_user u WHERE u.username = s.student_no);

SET FOREIGN_KEY_CHECKS = 1;
