# 4A 班级学生成绩管理系统（studentscore）

基于 **SSM（Spring + SpringMVC + MyBatis）** 的学生成绩管理系统，同时集成了一套 **Struts2** Web 层用于课程演示，支持三种角色（管理员 / 教师 / 学生）、成绩录入与统计、Excel 导入导出、账号管理与操作日志。

## 技术栈

| 分类 | 选型 |
|---|---|
| 语言 / 构建 | Java 8、Maven 3.6+、WAR 打包 |
| 核心框架 | Spring 5.3.39（IoC/AOP/事务）、SpringMVC、MyBatis 3.5.16 + PageHelper 5.3.3 |
| 第二套 Web 层 | Struts2 2.5.33 + struts2-spring-plugin（仅处理 `*.action`） |
| 数据库 | MySQL 8.0、Druid 1.2.23 连接池 |
| 视图 | JSP + JSTL + Bootstrap 5（已本地化，无 CDN 依赖） |
| 其他 | Apache POI 5.2.5（Excel）、Jackson、Logback |
| 容器 | **Tomcat 9.x**（`javax.servlet` 规范） |

## 环境要求

| 组件 | 版本要求 | 说明 |
|---|---|---|
| JDK | **1.8** | 源码与字节码均为 1.8，高版本 JDK 需显式指定 `-source/-target` |
| Maven | 3.6 及以上 | |
| MySQL | 8.0 | 脚本使用 utf8mb4 |
| Tomcat | **9.x** | **不可用 Tomcat 10/11**，它们使用 `jakarta.servlet`，会启动失败 |

## 部署步骤

### 1. 克隆代码

```bash
git clone https://github.com/<your-name>/studentscore.git
cd studentscore
```

### 2. 初始化数据库

```bash
mysql -uroot -p < src/main/resources/sql/studentscore.sql
```

脚本会自动完成：创建 `studentscore` 库（utf8mb4）→ 建 7 张表（`sys_user` / `teacher` / `student` / `class_info` / `course` / `score` / `sys_log`）→ 灌入演示数据（3 位教师、15 名学生、6 门课程、75 条成绩，以及全部账号）。

> 重复执行会先 `DROP TABLE`，已有数据会被清空。

### 3. 修改数据源

编辑 `src/main/resources/jdbc.properties`：

```properties
jdbc.url=jdbc:mysql://localhost:3306/studentscore?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
jdbc.username=root
jdbc.password=你的MySQL密码
```

### 4. 打包

```bash
mvn clean package -DskipTests
```

产物：`target/studentscore.war`

### 5. 部署到 Tomcat

**方式 A：命令行部署**

1. 把 `target/studentscore.war` 复制到 `%CATALINA_HOME%\webapps\`（Linux 为 `$CATALINA_HOME/webapps/`）
2. 启动 Tomcat：`bin\startup.bat`（Linux：`bin/startup.sh`）

**方式 B：IDEA 本地运行**

1. `Run` → `Edit Configurations` → 新增 **Tomcat Server → Local**，选择 Tomcat 9 安装目录
2. `Deployment` 页签 → `+` → `Artifact` → 选择 `studentscore:war exploded`
3. `Application context` 填 `/studentscore`
4. 启动后访问下方地址

### 6. 访问

```
http://localhost:8080/studentscore/
```

## 默认账号

| 角色 | 账号 | 密码 | 说明 |
|---|---|---|---|
| 管理员 | `admin` | `123456` | 全部菜单，含账号管理、操作日志 |
| 教师 | `teacher` | `123456` | 王建国，仅能看到所授课程 C001 / C003 |
| 教师 | `T002` / `T003` | `123456` | 李梅 / 赵强 |
| 学生 | `2024001` ~ `2024015` | `123456` | 账号即学号，只能查看本人成绩 |

## 功能概览

- **成绩**：按班级 / 课程 / 学期 / 关键字查询（分页），批量录入（总评 = 平时 30% + 期末 70%），Excel 导入 / 导出 / 模板下载
- **统计**：课程维度平均分、及格率、优秀率；学生维度算术平均分、学分加权平均分、班级排名
- **权限**：路由级登录拦截、注解式角色校验（`@RequireRole`），教师只能操作自己所授课程
- **账号**：账号增删改查、重置密码、批量生成学生账号、自主修改密码
- **日志**：登录、成绩保存、Excel 导入、密码重置等关键操作留痕（含 IP 与时间）
- **Struts2 示例**：`/studentscore/struts/index.action`，包含学生管理与成绩查询两个 action

## 常见问题

| 现象 | 原因与处理 |
|---|---|
| 启动后访问 404，日志有 `Access denied for user 'root'@'localhost'` | 数据库密码错误，或未执行 SQL 脚本，修正 `jdbc.properties` 后重启 |
| `Context [/studentscore] 启动失败`，日志提示 `struts.xml` 中 `package` 内容不匹配 | `struts.xml` 的 `package` 子元素必须按 DTD 顺序：`result-types → interceptors → default-interceptor-ref → default-class-ref → global-results → action` |
| 页面无样式（只有文字） | 项目静态资源已本地化到 `src/main/webapp/static/`，无需外网；若仍异常，确认浏览器强制刷新（Ctrl+F5） |
| 中文乱码 | 检查三处：`jdbc.url` 的 `characterEncoding=utf8`、`web.xml` 的 `CharacterEncodingFilter`、`web.xml` 的 `<jsp-config><page-encoding>UTF-8` |
| `java.lang.ClassNotFoundException: javax.servlet.*` | 使用了 Tomcat 10+，请换成 Tomcat 9 |
| IDE 编译报“程序包 org.springframework.beans 不存在” | IDEA 中执行 `Maven` 面板 → `Reload All Maven Projects` |

## 安全说明

- `src/main/resources/jdbc.properties` 中含明文数据库口令，如需公开仓库，请改为环境变量读取或使用不含真实口令的示例文件。
- 演示账号密码均为 `123456`，仅用于教学演示，请勿用于生产环境。

## 目录结构

```
studentscore
├── src/main/java/com/studentscore
│   ├── controller      # SpringMVC 控制器
│   ├── struts          # Struts2 Action / 拦截器
│   ├── service         # 业务层 + impl
│   ├── mapper          # MyBatis 接口
│   ├── entity / vo / dto
│   ├── common          # 工具、异常、常量、Excel 工具
│   └── interceptor     # 登录 / 角色拦截器
├── src/main/resources
│   ├── spring          # applicationContext.xml / spring-mvc.xml
│   ├── mapper          # MyBatis XML
│   ├── sql             # studentscore.sql
│   ├── jdbc.properties
│   └── struts.xml
└── src/main/webapp
    ├── WEB-INF/views   # JSP 页面
    ├── WEB-INF/web.xml
    └── static          # Bootstrap / 图标 / ECharts（本地）
```
