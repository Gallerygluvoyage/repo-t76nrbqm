# 校园兼职信息发布与接单管理系统

基于 **Spring Boot + MyBatis + MySQL** 后端与 **Vue 3 + Vite + Element Plus** 前端的前后端分离项目。面向校园场景，提供兼职信息发布、浏览搜索、在线接单、订单管理与后台用户管理等功能，UI 采用低饱和单调（灰白 + 石板灰蓝）配色。

## 一、技术栈

| 层次 | 技术 |
| --- | --- |
| 前端 | Vue 3、Vite 5、Element Plus 2、Vue Router、Pinia、Axios |
| 后端 | Spring Boot 3.2.5、MyBatis 3、Spring Web |
| 数据库 | MySQL 5.7+/8.x |
| 构建 | Maven 3.9、Node 20 |

后端分为清晰的三层：**控制器层（controller）→ 业务层（service/impl）→ 数据访问层（mapper + XML）**。

## 二、目录结构

```
campus-jobs/
├── backend/                  # 后端 Spring Boot 工程
│   ├── src/main/java/com/campus/jobs/
│   │   ├── controller/       # 控制器层（REST 接口）
│   │   ├── service/impl/     # 业务层
│   │   ├── mapper/           # 数据访问层接口
│   │   ├── entity/           # 实体类
│   │   ├── common/           # 通用返回/异常/分页
│   │   └── config/           # 跨域等配置
│   ├── src/main/resources/
│   │   ├── mapper/           # MyBatis XML 映射
│   │   └── application.yml   # 配置文件
│   └── pom.xml
├── frontend/                 # 前端 Vue3 工程
│   ├── src/
│   │   ├── views/            # 页面（登录/注册/概览/兼职/订单/用户）
│   │   ├── layout/           # 主框架布局
│   │   ├── router/           # 路由
│   │   ├── api/              # axios 封装
│   │   └── styles/           # 低饱和单调主题
│   └── package.json
├── db/
│   └── data.sql              # 建库建表 + 示例数据
└── README.md
```

## 三、功能模块

- **注册 / 登录**：支持学生、雇主注册，管理员内置；登录后按角色进入系统。
- **首页概览**：统计兼职总数、招聘中岗位、订单总数、待处理订单，并展示最新兼职。
- **兼职信息管理**（新增/修改/删除/查询）：按标题/单位/地点关键字 + 分类 + 状态多条件检索，分页展示。
- **接单订单管理**（新增/修改/删除/查询）：选择岗位下单，维护订单状态（待处理/已接受/已完成/已取消）。
- **用户管理**（新增/修改/删除/查询，仅管理员）：管理各角色账号与启用/禁用状态。

## 四、角色权限

| 角色 | 说明 |
| --- | --- |
| ADMIN 管理员 | 全部功能，含用户管理 |
| EMPLOYER 雇主 | 发布/管理兼职、处理订单 |
| STUDENT 学生 | 浏览搜索兼职、接单、查看订单 |

内置账号（密码均为 `123456`）：`admin` / `employer1` / `employer2` / `student1` / `student2`。

## 五、运行步骤

### 1. 初始化数据库

```bash
mysql -u root -p < db/data.sql
```

脚本会创建 `campus_jobs` 数据库、三张表并写入示例数据。

### 2. 启动后端

修改 `backend/src/main/resources/application.yml` 中的数据库账号密码后：

```bash
cd backend
mvn spring-boot:run
# 或打包运行
mvn -DskipTests package
java -jar target/campus-jobs.jar
```

后端默认端口 **8080**。

### 3. 启动前端

```bash
cd frontend
npm install
npm run dev
```

浏览器访问 **http://localhost:5173** ，前端通过 Vite 代理 `/api` 转发到后端 8080。

## 六、主要接口

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/auth/login` | 登录 |
| POST | `/api/auth/register` | 注册 |
| GET/POST/PUT/DELETE | `/api/jobs` `/api/jobs/{id}` | 兼职 CRUD + 搜索 |
| GET/POST/PUT/DELETE | `/api/orders` `/api/orders/{id}` | 订单 CRUD + 搜索 |
| GET/POST/PUT/DELETE | `/api/users` `/api/users/{id}` | 用户 CRUD + 搜索 |

统一返回结构：`{ "code": 200, "msg": "操作成功", "data": ... }`。
