-- 软件工程专业相关课程（至少 5 门）
INSERT INTO courses (course_code, standard_name, teacher, location, created_at, updated_at) VALUES
('COURSE-SE', '软件工程导论', '赵教授', '教学楼 A205', NOW(), NOW()),
('COURSE-OOP', '面向对象程序设计', '陈教授', '实验楼 B101', NOW(), NOW()),
('COURSE-DSA', '数据结构与算法', '周教授', '教学楼 A301', NOW(), NOW()),
('COURSE-ST', '软件测试技术', '吴教授', '实验楼 C201', NOW(), NOW()),
('COURSE-SPM', '软件项目管理', '郑教授', '教学楼 B105', NOW(), NOW()),
('COURSE-SA', '软件体系结构', '孙教授', '教学楼 A401', NOW(), NOW());

INSERT INTO course_aliases (course_id, alias_name) VALUES
(1, '软工导论'), (1, '软件工程'),
(2, '面向对象'), (2, 'OOP'),
(3, '数据结构'), (3, 'DSA'),
(4, '软件测试'), (4, '软测'),
(5, '项目管理'), (5, 'PM'),
(6, '体系结构'), (6, '软件架构');

-- 软件工程专业相关考试（至少 3 场）
INSERT INTO exams (exam_code, standard_name, exam_time) VALUES
('EXAM-SE-FINAL', '软件工程导论期末考试', '2026-06-18 14:00:00'),
('EXAM-OOP-FINAL', '面向对象程序设计期末考试', '2026-06-20 09:00:00'),
('EXAM-ST-FINAL', '软件测试技术期末考试', '2026-06-22 14:00:00'),
('EXAM-SPM-FINAL', '软件项目管理期末考试', '2026-06-24 09:00:00');

INSERT INTO exam_aliases (exam_id, alias_name) VALUES
(1, '软工期末'), (1, '软件工程导论期末'),
(2, 'OOP期末'), (2, '面向对象期末'),
(3, '软测期末'), (3, '软件测试期末'),
(4, '项目管理期末');

INSERT INTO time_slots (relation_type, relation_code, day_of_week, start_time, end_time) VALUES
('COURSE', 'COURSE-SE', 1, '08:00:00', '09:40:00'),
('COURSE', 'COURSE-SE', 3, '10:00:00', '11:40:00'),
('COURSE', 'COURSE-OOP', 2, '14:00:00', '15:40:00'),
('COURSE', 'COURSE-OOP', 4, '08:00:00', '09:40:00'),
('COURSE', 'COURSE-DSA', 1, '14:00:00', '15:40:00'),
('COURSE', 'COURSE-DSA', 5, '10:00:00', '11:40:00'),
('COURSE', 'COURSE-ST', 2, '10:00:00', '11:40:00'),
('COURSE', 'COURSE-ST', 4, '14:00:00', '15:40:00'),
('COURSE', 'COURSE-SPM', 3, '14:00:00', '15:40:00'),
('COURSE', 'COURSE-SPM', 5, '08:00:00', '09:40:00'),
('COURSE', 'COURSE-SA', 3, '08:00:00', '09:40:00'),
('COURSE', 'COURSE-SA', 5, '14:00:00', '15:40:00');

INSERT INTO plan_items (item_code, title, description, start_time, end_time, status, created_at, updated_at) VALUES
('PLAN-SEED-001', '复习软件生命周期模型', '瀑布、迭代、敏捷对比', '2026-05-26 19:00:00', '2026-05-26 21:00:00', 'PENDING', NOW(), NOW()),
('PLAN-SEED-002', '面向对象设计练习', 'UML类图与继承多态', '2026-05-27 19:30:00', '2026-05-27 21:30:00', 'IN_PROGRESS', NOW(), NOW()),
('PLAN-SEED-003', '数据结构刷题', '链表、树与图算法', '2026-05-28 18:00:00', '2026-05-28 20:00:00', 'PENDING', NOW(), NOW()),
('PLAN-SEED-004', '软工需求分析阅读', '用例图与需求规格说明', '2026-05-29 20:00:00', '2026-05-29 22:00:00', 'PENDING', NOW(), NOW()),
('PLAN-SEED-005', '软件测试用例编写', '等价类与边界值分析', '2026-05-30 19:00:00', '2026-05-30 21:00:00', 'COMPLETED', NOW(), NOW());

INSERT INTO plan_resources (plan_item_code, resource_name, resource_url) VALUES
('PLAN-SEED-001', '软件工程导论讲义', 'https://example.com/se/lifecycle'),
('PLAN-SEED-001', '敏捷开发概述', 'https://example.com/se/agile'),
('PLAN-SEED-002', 'UML设计手册', 'https://example.com/oop/uml'),
('PLAN-SEED-003', '数据结构习题集', 'https://example.com/dsa/problems'),
('PLAN-SEED-004', '需求分析教材', 'https://example.com/se/requirements'),
('PLAN-SEED-005', '软件测试基础', 'https://example.com/st/testing');

INSERT INTO time_slots (relation_type, relation_code, day_of_week, start_time, end_time) VALUES
('PLAN', 'PLAN-SEED-001', 1, '19:00:00', '21:00:00'),
('PLAN', 'PLAN-SEED-002', 2, '19:30:00', '21:30:00'),
('PLAN', 'PLAN-SEED-003', 3, '18:00:00', '20:00:00'),
('PLAN', 'PLAN-SEED-004', 4, '20:00:00', '22:00:00'),
('PLAN', 'PLAN-SEED-005', 5, '19:00:00', '21:00:00');

INSERT INTO user_profiles (user_code, display_name, created_at, updated_at) VALUES
('USER-DEMO', '演示用户', NOW(), NOW());
