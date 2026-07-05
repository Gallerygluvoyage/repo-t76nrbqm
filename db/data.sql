-- =====================================================================
-- 校园兼职信息发布与接单管理系统  数据库脚本
-- 数据库：MySQL 5.7+ / 8.x
-- 使用方式：mysql -u root -p < data.sql
-- =====================================================================

DROP DATABASE IF EXISTS campus_jobs;
CREATE DATABASE campus_jobs DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE campus_jobs;

-- ---------------------------------------------------------------------
-- 用户表
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    username    VARCHAR(50)  NOT NULL COMMENT '登录账号',
    password    VARCHAR(100) NOT NULL COMMENT '登录密码',
    real_name   VARCHAR(50)  DEFAULT NULL COMMENT '真实姓名',
    role        VARCHAR(20)  NOT NULL DEFAULT 'STUDENT' COMMENT '角色：ADMIN/EMPLOYER/STUDENT',
    phone       VARCHAR(20)  DEFAULT NULL COMMENT '联系电话',
    status      VARCHAR(10)  NOT NULL DEFAULT '正常' COMMENT '状态：正常/禁用',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ---------------------------------------------------------------------
-- 兼职信息表
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS job;
CREATE TABLE job (
    id           BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    title        VARCHAR(100)  NOT NULL COMMENT '兼职标题',
    category     VARCHAR(30)   DEFAULT NULL COMMENT '分类',
    company      VARCHAR(100)  DEFAULT NULL COMMENT '发布单位/商家',
    location     VARCHAR(100)  DEFAULT NULL COMMENT '工作地点',
    salary       DECIMAL(10,2) DEFAULT NULL COMMENT '薪资',
    salary_unit  VARCHAR(20)   DEFAULT '元/时' COMMENT '薪资单位',
    headcount    INT           DEFAULT 1 COMMENT '招聘人数',
    contact      VARCHAR(50)   DEFAULT NULL COMMENT '联系方式',
    description  VARCHAR(1000) DEFAULT NULL COMMENT '岗位描述',
    status       VARCHAR(10)   NOT NULL DEFAULT '招聘中' COMMENT '状态：招聘中/已结束',
    publisher_id BIGINT        DEFAULT NULL COMMENT '发布者用户ID',
    create_time  DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='兼职信息表';

-- ---------------------------------------------------------------------
-- 接单/订单表
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS job_order;
CREATE TABLE job_order (
    id              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    job_id          BIGINT       NOT NULL COMMENT '兼职ID',
    applicant_name  VARCHAR(50)  NOT NULL COMMENT '接单人姓名',
    applicant_phone VARCHAR(20)  DEFAULT NULL COMMENT '接单人电话',
    status          VARCHAR(10)  NOT NULL DEFAULT '待处理' COMMENT '状态：待处理/已接受/已完成/已取消',
    remark          VARCHAR(500) DEFAULT NULL COMMENT '备注',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '接单时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接单/订单表';

-- =====================================================================
-- 初始化数据
-- =====================================================================

-- 用户（密码均为明文，便于演示）
INSERT INTO sys_user (username, password, real_name, role, phone, status) VALUES
('admin',    '123456', '系统管理员', 'ADMIN',    '13800000000', '正常'),
('employer1','123456', '阳光家教中心', 'EMPLOYER', '13811112222', '正常'),
('employer2','123456', '校园便利店',   'EMPLOYER', '13833334444', '正常'),
('student1', '123456', '张同学',       'STUDENT',  '13855556666', '正常'),
('student2', '123456', '李同学',       'STUDENT',  '13877778888', '正常'),
('student3', '123456', '王同学',       'STUDENT',  '13899990000', '禁用');

-- 兼职信息
INSERT INTO job (title, category, company, location, salary, salary_unit, headcount, contact, description, status, publisher_id) VALUES
('高数一对一家教',    '家教',   '阳光家教中心', '大学城校区',     80.00, '元/时', 2, '13811112222', '负责为大一学生辅导高等数学，要求耐心细致，有家教经验优先。', '招聘中', 2),
('奶茶店周末店员',    '餐饮',   '校园便利店',   '生活区商业街',   22.00, '元/时', 3, '13833334444', '周末制作奶茶及收银，需形象良好、沟通能力强。',           '招聘中', 3),
('校园快递分拣',      '体力',   '菜鸟驿站',     '东门快递中心',   25.00, '元/时', 5, '13800001111', '负责快递入库分拣与派送提醒，工作时间灵活。',             '招聘中', 2),
('迎新志愿者',        '活动',   '学生会',       '校本部体育馆',   150.00,'元/天', 10,'13800002222', '协助新生报到、行李搬运及信息登记，管午餐。',             '招聘中', 2),
('图书馆整理助理',    '文职',   '校图书馆',     '图书馆三楼',     18.00, '元/时', 2, '13800003333', '整理归还图书、维护阅览秩序，需细心负责。',               '已结束', 3),
('程序设计竞赛助教',  '技术',   '计算机学院',   '实验楼A栋',      60.00, '元/时', 1, '13800004444', '协助老师批改代码作业、答疑，要求熟悉Java或Python。',     '招聘中', 2);

-- 接单/订单
INSERT INTO job_order (job_id, applicant_name, applicant_phone, status, remark) VALUES
(1, '张同学', '13855556666', '已接受', '每周二四晚辅导'),
(2, '李同学', '13877778888', '待处理', '只能周六到岗'),
(3, '王同学', '13899990000', '已完成', '已结算工资'),
(1, '李同学', '13877778888', '待处理', '希望周末辅导'),
(4, '张同学', '13855556666', '已接受', '负责行李搬运组'),
(6, '李同学', '13877778888', '已取消', '时间冲突');
