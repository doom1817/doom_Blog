# Blog-Parent 后端项目技术栈分析文档

## 1. 项目概述

### 1.1 项目基本信息

- **项目名称**: blog-parent（个人博客系统）
- **项目类型**: Spring Boot 单体架构 Web 应用
- **开发语言**: Java 17
- **构建工具**: Maven
- **项目结构**: 多模块 Maven 项目（父工程 + blog-web 子模块）
- **启动端口**: 8080

### 1.2 项目定位

基于 Spring Boot + Spring Security + MyBatis-Plus + JWT 的个人博客系统后端，提供用户认证、文章管理、评论互动、数据统计等核心功能，采用 RESTful API 设计风格，支持前后端分离架构。

---

## 2. 技术栈详细列表

### 2.1 核心框架

#### 2.1.1 Spring Boot 3.4.0

- **定位**: 应用程序核心框架，提供自动配置和快速开发能力
- **应用场景**:
  - Web 应用启动和运行
  - 依赖注入和 Bean 管理
  - 自动配置和组件扫描
- **核心功能职责**:
  - 内嵌 Tomcat 服务器，无需外部部署
  - 自动配置 Spring Security、MyBatis-Plus 等组件
  - 提供 `@SpringBootApplication` 注解实现快速启动
- **相关配置文件**:
  - `d:\37.prog\java\java_idea\Blog\blog-parent\pom.xml` (父工程)
  - `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\pom.xml` (子模块)
  - `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\resources\application.yaml`

#### 2.1.2 Spring Security 6.x (通过 spring-boot-starter-security)

- **定位**: 安全认证和授权框架
- **应用场景**:
  - 用户身份认证（登录验证）
  - 接口访问权限控制（基于角色的访问控制 RBAC）
  - 密码加密存储
- **核心功能职责**:
  - 提供 BCrypt 密码加密器
  - 配置 HTTP 安全规则（公开接口、受保护接口）
  - 集成 JWT 无状态认证
  - 支持方法级权限控制（`@PreAuthorize`）
- **相关源代码**:
  - `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\java\com\doom\web\config\SecurityConfig.java`
  - `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\java\com\doom\web\filter\JwtAuthenticationFilter.java`
  - `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\java\com\doom\web\service\UserDetailsServiceImpl.java`

### 2.2 数据访问层框架

#### 2.2.1 MyBatis-Plus 3.5.7

- **定位**: MyBatis 的增强工具，简化数据库操作
- **应用场景**:
  - 单表 CRUD 操作（增删改查）
  - 复杂 SQL 查询（使用注解或 XML）
  - 分页查询、条件构造器
- **核心功能职责**:
  - 提供 `BaseMapper` 接口，内置常用 CRUD 方法
  - 支持条件构造器 `LambdaQueryWrapper` 实现动态 SQL
  - 支持元数据自动填充（创建时间、更新时间）
  - 支持代码生成和分页插件
- **相关源代码**:
  - `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\java\com\doom\web\mapper\ArticleMapper.java`
  - `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\java\com\doom\web\mapper\UserMapper.java`
  - `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\java\com\doom\web\mapper\CategoryMapper.java`
  - `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\java\com\doom\web\mapper\CommentMapper.java`
  - `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\java\com\doom\web\handler\MyMetaObjectHandler.java`

#### 2.2.2 MySQL Connector J (mysql-connector-j)

- **定位**: MySQL 数据库驱动
- **应用场景**: 连接 MySQL 数据库，执行 SQL 语句
- **核心功能职责**:
  - 提供 JDBC 连接
  - 执行 SQL 查询和更新
  - 事务管理
- **相关配置**:
  - `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\resources\application.yaml` (datasource 配置)

### 2.3 认证与授权组件

#### 2.3.1 JJWT (io.jsonwebtoken) 0.12.6

- **定位**: JWT（JSON Web Token）生成和解析库
- **应用场景**:
  - 用户登录后生成 JWT 令牌
  - 后续请求解析 JWT 令牌验证身份
- **核心功能职责**:
  - 生成包含用户名和角色的 JWT 令牌
  - 解析 JWT 令牌提取用户信息
  - 验证令牌签名和有效期
- **相关源代码**:
  - `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\java\com\doom\web\utils\JwtUtils.java`
  - `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\java\com\doom\web\filter\JwtAuthenticationFilter.java`

#### 2.3.2 BCrypt Password Encoder (Spring Security 内置)

- **定位**: 密码加密工具
- **应用场景**:
  - 用户注册时加密密码
  - 用户登录时验证密码
- **核心功能职责**:
  - 使用 BCrypt 算法加密密码（不可逆）
  - 支持密码匹配验证
- **相关源代码**:
  - `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\java\com\doom\web\config\SecurityConfig.java` (PasswordEncoder Bean 配置)
  - `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\java\com\doom\web\controller\AuthController.java`

### 2.4 Web 开发组件

#### 2.4.1 Spring Web (spring-boot-starter-web)

- **定位**: Web 开发核心组件
- **应用场景**:
  - RESTful API 接口开发
  - HTTP 请求处理
  - JSON 数据序列化/反序列化
- **核心功能职责**:
  - 提供 `@RestController` 注解实现 RESTful 接口
  - 支持 GET、POST、PUT、DELETE 等 HTTP 方法
  - 集成 Jackson 实现 JSON 处理
- **相关源代码**:
  - `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\java\com\doom\web\controller\` (所有 Controller 类)

#### 2.4.2 Spring Validation (spring-boot-starter-validation)

- **定位**: 参数校验组件
- **应用场景**: 接口参数校验（如 `@NotNull`、`@Size` 等）
- **核心功能职责**:
  - 提供参数校验注解
  - 自动校验请求参数
- **相关配置**: `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\pom.xml`

### 2.5 工具类库

#### 2.5.1 Lombok

- **定位**: Java 代码简化工具
- **应用场景**: 自动生成 Getter/Setter、构造器、toString 等方法
- **核心功能职责**:
  - `@Data`: 自动生成 Getter/Setter/toString/equals/hashCode
  - `@Slf4j`: 自动生成日志对象
- **相关配置**: `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\pom.xml`

#### 2.5.2 自定义工具类

- **Result**: 统一响应结果封装类
  - 路径: `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\java\com\doom\web\utils\Result.java`
  - 功能: 统一接口返回格式（code、msg、data）
- **JwtUtils**: JWT 令牌工具类
  - 路径: `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\java\com\doom\web\utils\JwtUtils.java`
  - 功能: 生成和解析 JWT 令牌

### 2.6 测试框架

#### 2.6.1 Spring Boot Test (spring-boot-starter-test)

- **定位**: 单元测试和集成测试框架
- **应用场景**: 编写单元测试和集成测试
- **核心功能职责**:
  - 提供 `@SpringBootTest` 注解启动测试环境
  - 集成 JUnit 5、Mockito 等测试工具
- **相关配置**: `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\pom.xml`

#### 2.6.2 Spring Security Test

- **定位**: Spring Security 测试支持
- **应用场景**: 测试安全认证和授权功能
- **相关配置**: `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\pom.xml`

---

## 3. 技术组件功能拆解

### 3.1 认证模块

#### 3.1.1 用户注册流程

**实现位置**: `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\java\com\doom\web\controller\AuthController.java`

**执行流程**:

1. 接收用户注册请求（用户名、密码、昵称）
2. 检查用户名是否已存在（查询数据库）
3. 使用 BCrypt 加密密码
4. 设置默认角色为 USER
5. 保存用户信息到数据库
6. 返回注册成功结果

**技术特性**:

- 密码加密：BCrypt 算法，不可逆
- 用户名唯一性校验
- 自动填充创建时间

#### 3.1.2 用户登录流程

**实现位置**: `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\java\com\doom\web\controller\AuthController.java`

**执行流程**:

1. 接收用户登录请求（用户名、密码）
2. 根据用户名查询用户信息
3. 使用 BCrypt 匹配密码
4. 生成 JWT 令牌（包含用户名和角色）
5. 返回令牌和用户基本信息

**技术特性**:

- 密码验证：BCrypt 匹配
- JWT 令牌生成：有效期 24 小时
- 无状态认证：不维护会话

#### 3.1.3 JWT 认证过滤器

**实现位置**: `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\java\com\doom\web\filter\JwtAuthenticationFilter.java`

**执行流程**:

1. 检查是否为 OPTIONS 请求，直接放行
2. 从 Authorization 请求头提取 Token（格式：Bearer xxx）
3. 解析 Token 获取用户名和角色
4. 将用户信息和权限封装到 SecurityContext
5. 继续执行后续过滤器链

**技术特性**:

- 继承 `OncePerRequestFilter`，确保每个请求只执行一次
- 支持跨域 OPTIONS 预检请求
- 角色必须加 "ROLE\_" 前缀

### 3.2 文章管理模块

#### 3.2.1 文章发布流程

**实现位置**: `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\java\com\doom\web\controller\UserArticleController.java`

**执行流程**:

1. 用户提交文章（标题、内容、分类 ID）
2. 从 SecurityContext 获取当前用户 ID
3. 设置文章状态为 0（待审核）
4. 初始化点击量为 0
5. 自动填充创建时间
6. 保存文章到数据库
7. 返回"已提交审核"提示

**技术特性**:

- 权限控制：需要登录认证
- 审核机制：初始状态为待审核
- 自动填充：创建时间、更新时间

#### 3.2.2 文章审核流程

**实现位置**: `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\java\com\doom\web\controller\ArticleAdminController.java`

**执行流程**:

1. 管理员查询待审核文章列表（status=0）
2. 管理员执行审核操作（通过/拒绝）
3. 更新文章状态（1:已通过, 2:已拒绝）
4. 返回审核结果

**技术特性**:

- 权限控制：仅限管理员访问
- 状态管理：待审核 → 已通过/已拒绝

#### 3.2.3 文章查询流程

**实现位置**: `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\java\com\doom\web\controller\PublicController.java`

**执行流程**:

1. 接收查询请求（可选分类 ID）
2. 查询已审核通过的文章（status=1）
3. 关联查询分类名和作者信息
4. 返回文章列表

**技术特性**:

- 公开访问：无需登录
- 多表关联：LEFT JOIN 查询分类和用户
- 动态 SQL：支持按分类筛选

### 3.3 评论管理模块

#### 3.3.1 发表评论流程

**实现位置**: `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\java\com\doom\web\controller\UserArticleController.java`

**执行流程**:

1. 用户提交评论（文章 ID、评论内容）
2. 从 SecurityContext 获取当前用户 ID
3. 自动填充评论时间
4. 保存评论到数据库
5. 返回"评论成功"提示

**技术特性**:

- 权限控制：需要登录认证
- 自动填充：评论时间

#### 3.3.2 查询评论流程

**实现位置**: `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\java\com\doom\web\controller\PublicController.java`

**执行流程**:

1. 接收文章 ID
2. 查询该文章的所有评论
3. 关联查询评论者信息（昵称、头像）
4. 返回评论列表

**技术特性**:

- 公开访问：无需登录
- 多表关联：LEFT JOIN 查询用户信息

### 3.4 数据统计模块

#### 3.4.1 平台概览统计

**实现位置**: `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\java\com\doom\web\controller\ArticleAdminController.java`

**统计指标**:

- 总文章数
- 总用户数
- 总分类数
- 总访问量（所有文章点击量之和）

**技术特性**:

- 权限控制：仅限管理员访问
- 聚合计算：Stream API 求和

#### 3.4.2 分类统计

**实现位置**: `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\java\com\doom\web\controller\ArticleAdminController.java`

**统计内容**: 各分类下的文章数量

**技术特性**:

- 权限控制：仅限管理员访问
- 分组统计：GROUP BY 查询

#### 3.4.3 点击量排行

**实现位置**: `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\java\com\doom\web\controller\ArticleAdminController.java`

**统计内容**: 点击量最高的前 10 篇文章

**技术特性**:

- 权限控制：仅限管理员访问
- 排序查询：ORDER BY view_count DESC LIMIT 10

---

## 4. 源代码、配置文件及资源文件路径

### 4.1 项目结构总览

```
blog-parent/
├── pom.xml                                    # 父工程 POM 文件
├── SUMMARY_Blog.md                            # 技术栈分析文档
├── blog-web/                                  # Web 子模块
│   ├── pom.xml                                # 子模块 POM 文件
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── doom/
│   │   │   │           └── web/
│   │   │   │               ├── BlogWebApplication.java    # 启动类
│   │   │   │               ├── config/                     # 配置类
│   │   │   │               │   └── SecurityConfig.java    # 安全配置
│   │   │   │               ├── controller/                 # 控制器层
│   │   │   │               │   ├── AuthController.java    # 认证接口
│   │   │   │               │   ├── PublicController.java  # 公开接口
│   │   │   │               │   ├── UserArticleController.java  # 用户文章接口
│   │   │   │               │   ├── ArticleAdminController.java  # 管理员文章接口
│   │   │   │               │   └── CategoryAdminController.java # 分类管理接口
│   │   │   │               ├── entity/                     # 实体类
│   │   │   │               │   ├── User.java              # 用户实体
│   │   │   │               │   ├── Article.java           # 文章实体
│   │   │   │               │   ├── Comment.java           # 评论实体
│   │   │   │               │   └── Category.java          # 分类实体
│   │   │   │               ├── vo/                         # 视图对象
│   │   │   │               │   ├── ArticleVO.java         # 文章视图对象
│   │   │   │               │   └── CommentVO.java         # 评论视图对象
│   │   │   │               ├── mapper/                     # 数据访问层
│   │   │   │               │   ├── UserMapper.java        # 用户 Mapper
│   │   │   │               │   ├── ArticleMapper.java     # 文章 Mapper
│   │   │   │               │   ├── CommentMapper.java     # 评论 Mapper
│   │   │   │               │   └── CategoryMapper.java    # 分类 Mapper
│   │   │   │               ├── service/                    # 服务层
│   │   │   │               │   ├── AuthService.java       # 认证服务
│   │   │   │               │   └── UserDetailsServiceImpl.java  # 用户详情服务
│   │   │   │               ├── filter/                     # 过滤器
│   │   │   │               │   └── JwtAuthenticationFilter.java  # JWT 认证过滤器
│   │   │   │               ├── handler/                    # 处理器
│   │   │   │               │   └── MyMetaObjectHandler.java  # 元数据自动填充
│   │   │   │               ├── exception/                 # 异常处理
│   │   │   │               │   └── GlobalExceptionHandler.java  # 全局异常处理
│   │   │   │               └── utils/                     # 工具类
│   │   │   │                   ├── Result.java             # 统一响应结果
│   │   │   │                   └── JwtUtils.java           # JWT 工具类
│   │   │   └── resources/
│   │   │       └── application.yaml                      # 应用配置文件
│   │   └── test/
│   │       └── java/
│   │           └── com/
│   │               └── doom/
│   │                   └── web/
│   │                       └── BlogWebApplicationTests.java
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── doom/
    │   │           └── blogparent/
    │   │               └── BlogParentApplication.java     # 父工程启动类
    │   └── resources/
    │       └── application.properties                    # 父工程配置
    └── test/
        └── java/
            └── com/
                └── doom/
                    └── blogparent/
                        └── BlogParentApplicationTests.java
```

### 4.2 配置文件详解

#### 4.2.1 父工程 POM 文件

**路径**: `d:\37.prog\java\java_idea\Blog\blog-parent\pom.xml`

**关键配置**:
- Spring Boot 版本: 3.4.0
- Java 版本: 17
- 子模块: blog-web
- 依赖管理: MyBatis-Plus 3.5.7, JJWT 0.12.5

#### 4.2.2 子模块 POM 文件
**路径**: `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\pom.xml`

**关键依赖**:
- spring-boot-starter-web: Web 开发
- spring-boot-starter-security: 安全认证
- mybatis-plus-spring-boot3-starter: 数据访问
- mysql-connector-j: MySQL 驱动
- jjwt-api/impl/jackson: JWT 令牌
- lombok: 代码简化
- spring-boot-starter-validation: 参数校验

#### 4.2.3 应用配置文件
**路径**: `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\resources\application.yaml`

**配置项**:
- 服务器端口: 8080
- 数据源: MySQL (localhost:3306/personal_blog)
- MyBatis-Plus: 日志输出、驼峰命名转换、主键自增
- JWT: 密钥、有效期 24 小时、请求头 Authorization
- 日志: com.doom 包级别 debug

---

## 5. 核心业务功能模块

### 5.1 用户认证模块

#### 5.1.1 功能概述
提供用户注册、登录、退出功能，采用 JWT 无状态认证机制。

#### 5.1.2 技术实现方案
- **密码加密**: BCrypt 算法，不可逆加密
- **身份认证**: JWT 令牌，有效期 24 小时
- **权限控制**: 基于角色的访问控制（RBAC）
  - ADMIN: 管理员，拥有所有权限
  - USER: 普通用户，只能操作自己的数据

#### 5.1.3 关键技术点
- Spring Security 配置（SecurityConfig）
- JWT 过滤器（JwtAuthenticationFilter）
- 用户详情服务（UserDetailsServiceImpl）
- 密码编码器（BCryptPasswordEncoder）

#### 5.1.4 模块交互关系

### 5.2 文章管理模块

#### 5.2.1 功能概述
提供文章发布、修改、删除、查询功能，支持文章审核机制。

#### 5.2.2 技术实现方案
- **文章状态管理**: 
  - 0: 待审核
  - 1: 已发布（公开可见）
  - 2: 已拒绝（不可见）
- **权限控制**:
  - 普通用户: 只能操作自己的文章
  - 管理员: 可操作所有文章
- **自动填充**: 创建时间、更新时间

#### 5.2.3 关键技术点
- MyBatis-Plus 条件构造器（LambdaQueryWrapper）
- 动态 SQL（@Select 注解）
- 多表关联查询（LEFT JOIN）
- 元数据自动填充（MyMetaObjectHandler）

#### 5.2.4 模块交互关系

### 5.3 评论管理模块

#### 5.3.1 功能概述
提供评论发表、删除、查询功能，支持公开浏览所有评论。

#### 5.3.2 技术实现方案
- **权限控制**: 
  - 发表评论: 需要登录
  - 查询评论: 公开访问
  - 删除评论: 只能删除自己的评论
- **关联查询**: 评论关联用户信息（昵称、头像）

#### 5.3.3 关键技术点
- 多表关联查询（LEFT JOIN）
- 权限校验（从 SecurityContext 获取当前用户）

#### 5.3.4 模块交互关系

### 5.4 分类管理模块

#### 5.4.1 功能概述
提供分类的增删改查功能，支持文章归类和分类筛选。

#### 5.4.2 技术实现方案
- **权限控制**: 仅限管理员访问
- **分类排序**: 按 sort 字段排序
- **分类统计**: 统计各分类下的文章数量

#### 5.4.3 关键技术点
- MyBatis-Plus BaseMapper
- 分组统计（GROUP BY）

#### 5.4.4 模块交互关系

### 5.5 数据统计模块

#### 5.5.1 功能概述
提供平台运营数据统计，包括文章数、用户数、分类数、访问量等。

#### 5.5.2 技术实现方案
- **权限控制**: 仅限管理员访问
- **统计指标**:
  - 平台概览: 总文章数、总用户数、总分类数、总访问量
  - 分类统计: 各分类下的文章数量
  - 点击量排行: Top 10 热门文章

#### 5.5.3 关键技术点
- 聚合计算（Stream API）
- 分组统计（GROUP BY）
- 排序查询（ORDER BY）

#### 5.5.4 模块交互关系

### 5.6 全局异常处理模块

#### 5.6.1 功能概述
统一处理 Controller 层抛出的异常，避免暴露堆栈信息给前端。

#### 5.6.2 技术实现方案
- **异常捕获**: @RestControllerAdvice 注解
- **统一响应格式**: Result 类封装

#### 5.6.3 关键技术点
- @ExceptionHandler 注解捕获异常
- 统一错误响应格式

#### 5.6.4 模块交互关系

---

## 6. 模块间交互关系

### 6.1 整体架构图

```
┌─────────────────────────────────────────────────────────────┐
│                        前端应用                               │
│                   (Vue/React/Angular)                        │
└───────────────────────┬─────────────────────────────────────┘
                        │ HTTP/HTTPS
                        ▼
┌─────────────────────────────────────────────────────────────┐
│                    Spring Boot 应用                          │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────┐   │
│  │              Controller 层 (控制器)                 │   │
│  │  AuthController | PublicController |               │   │
│  │  UserArticleController | ArticleAdminController    │   │
│  └──────────────────────┬──────────────────────────────┘   │
│                         │                                    │
│  ┌──────────────────────▼──────────────────────────────┐   │
│  │              Filter 层 (过滤器)                      │   │
│  │         JwtAuthenticationFilter                    │   │
│  └──────────────────────┬──────────────────────────────┘   │
│                         │                                    │
│  ┌──────────────────────▼──────────────────────────────┐   │
│  │              Service 层 (服务层)                     │   │
│  │         AuthService | UserDetailsServiceImpl         │   │
│  └──────────────────────┬──────────────────────────────┘   │
│                         │                                    │
│  ┌──────────────────────▼──────────────────────────────┐   │
│  │              Mapper 层 (数据访问层)                  │   │
│  │  UserMapper | ArticleMapper | CommentMapper         │   │
│  │  CategoryMapper                                      │   │
│  └──────────────────────┬──────────────────────────────┘   │
│                         │                                    │
│  ┌──────────────────────▼──────────────────────────────┐   │
│  │              数据库层 (MySQL)                        │   │
│  │  sys_user | blog_article | blog_comment             │   │
│  │  sys_category                                       │   │
│  └─────────────────────────────────────────────────────┘   │
├─────────────────────────────────────────────────────────────┤
│              配置层 (Config)                                │
│  SecurityConfig | MyMetaObjectHandler |                    │
│  GlobalExceptionHandler                                   │
├─────────────────────────────────────────────────────────────┤
│              工具层 (Utils)                                 │
│  Result | JwtUtils                                          │
└─────────────────────────────────────────────────────────────┘
```

### 6.2 请求处理流程

#### 6.2.1 用户登录流程

```
1. 前端发送登录请求
   POST /api/auth/login
   {
     "username": "testuser",
     "password": "123456"
   }
   ↓
2. 请求到达 AuthController.login()
   ↓
3. 调用 AuthService.login()
   ↓
4. UserMapper 查询用户信息
   SELECT * FROM sys_user WHERE username = ?
   ↓
5. BCrypt 验证密码
   passwordEncoder.matches(rawPassword, encodedPassword)
   ↓
6. 生成 JWT 令牌
   JwtUtils.generateToken(username, role)
   ↓
7. 返回登录结果
   {
     "code": 200,
     "msg": "操作成功",
     "data": {
       "token": "eyJhbGci...",
       "username": "testuser",
       "role": "USER"
     }
   }
```

#### 6.2.2 文章发布流程

```
1. 前端发送发布文章请求
   POST /api/user/article/add
   Headers: Authorization: Bearer <token>
   {
     "title": "文章标题",
     "content": "文章内容",
     "categoryId": 1
   }
   ↓
2. JwtAuthenticationFilter 拦截请求
   - 提取 Token
   - 解析 Token 获取用户信息
   - 设置 SecurityContext
   ↓
3. 请求到达 UserArticleController.addArticle()
   ↓
4. 从 SecurityContext 获取当前用户 ID
   ↓
5. 设置文章状态为 0（待审核）
   ↓
6. ArticleMapper 保存文章
   INSERT INTO blog_article (...)
   ↓
7. 返回发布结果
   {
     "code": 200,
     "msg": "已提交审核",
     "data": null
   }
```

### 6.3 安全认证流程

#### 6.3.1 JWT 认证流程

```
1. 客户端发送请求
   Headers: Authorization: Bearer <token>
   ↓
2. JwtAuthenticationFilter 拦截
   ↓
3. 检查是否为 OPTIONS 请求
   是 → 直接放行
   否 → 继续下一步
   ↓
4. 从请求头提取 Token
   String token = request.getHeader("Authorization")
   ↓
5. 解析 Token
   JwtUtils.parseToken(token)
   ↓
6. 验证 Token 有效性
   - 签名是否正确
   - 是否过期
   ↓
7. 提取用户信息
   String username = claims.getSubject()
   String role = claims.get("role")
   ↓
8. 创建 Authentication 对象
   UsernamePasswordAuthenticationToken
   ↓
9. 设置到 SecurityContext
   SecurityContextHolder.getContext().setAuthentication(auth)
   ↓
10. 继续执行后续过滤器链
```

#### 6.3.2 权限控制流程

```
1. 请求到达 Controller 方法
   ↓
2. Spring Security 检查方法权限注解
   - @PreAuthorize("hasRole('ADMIN')")
   - @PreAuthorize("hasRole('USER')")
   ↓
3. 从 SecurityContext 获取当前用户权限
   Authentication auth = SecurityContextHolder.getContext().getAuthentication()
   Collection<? extends GrantedAuthority> authorities = auth.getAuthorities()
   ↓
4. 验证用户是否拥有所需权限
   authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
   ↓
5. 权限验证通过
   - 继续执行业务逻辑
   ↓
6. 权限验证失败
   - 抛出 AccessDeniedException
   - GlobalExceptionHandler 捕获异常
   - 返回 403 错误
```

---

## 7. 数据库设计

### 7.1 数据库表结构

#### 7.1.1 sys_user（用户表）
| 字段名      | 类型         | 说明               |
| ----------- | ------------ | ------------------ |
| id          | BIGINT       | 用户ID，主键，自增 |
| username    | VARCHAR(50)  | 用户名，唯一       |
| password    | VARCHAR(100) | 密码，BCrypt 加密  |
| nickname    | VARCHAR(50)  | 昵称               |
| avatar      | VARCHAR(255) | 头像 URL           |
| role        | VARCHAR(20)  | 角色（ADMIN/USER） |
| create_time | DATETIME     | 注册时间           |

#### 7.1.2 blog_article（文章表）
| 字段名      | 类型         | 说明                                 |
| ----------- | ------------ | ------------------------------------ |
| id          | BIGINT       | 文章ID，主键，自增                   |
| title       | VARCHAR(100) | 文章标题                             |
| summary     | VARCHAR(500) | 文章摘要                             |
| content     | TEXT         | 文章内容                             |
| category_id | BIGINT       | 分类ID，外键                         |
| user_id     | BIGINT       | 作者ID，外键                         |
| status      | INT          | 状态（0:待审核, 1:已发布, 2:已拒绝） |
| view_count  | INT          | 点击量                               |
| create_time | DATETIME     | 创建时间                             |
| update_time | DATETIME     | 更新时间                             |

#### 7.1.3 blog_comment（评论表）
| 字段名      | 类型     | 说明               |
| ----------- | -------- | ------------------ |
| id          | BIGINT   | 评论ID，主键，自增 |
| article_id  | BIGINT   | 文章ID，外键       |
| user_id     | BIGINT   | 评论者ID，外键     |
| content     | TEXT     | 评论内容           |
| create_time | DATETIME | 评论时间           |

#### 7.1.4 sys_category（分类表）
| 字段名      | 类型        | 说明               |
| ----------- | ----------- | ------------------ |
| id          | BIGINT      | 分类ID，主键，自增 |
| name        | VARCHAR(50) | 分类名称           |
| sort        | INT         | 排序序号           |
| create_time | DATETIME    | 创建时间           |

### 7.2 表关系图

```
┌─────────────────────────────────────────────────────────────────────┐
│                          personal_blog 数据库                        │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌──────────────────────┐                                           │
│  │     sys_user         │                                           │
│  │  ─────────────────── │                                           │
│  │  id (PK)             │                                           │
│  │  username            │                                           │
│  │  password            │                                           │
│  │  nickname            │                                           │
│  │  avatar              │                                           │
│  │  role                │                                           │
│  │  create_time         │                                           │
│  └──────────┬───────────┘                                           │
│             │ 1                                                     │
│             │                                                       │
│             │ N                                                     │
│             ▼                                                       │
│  ┌──────────────────────┐    ┌──────────────────────┐             │
│  │   blog_article       │    │   blog_comment        │             │
│  │  ─────────────────── │    │  ─────────────────── │             │
│  │  id (PK)             │    │  id (PK)             │             │
│  │  title               │    │  article_id (FK)     │◄────────────┤
│  │  summary             │    │  user_id (FK)        │             │
│  │  content             │    │  content             │             │
│  │  category_id (FK)    │    │  create_time         │             │
│  │  user_id (FK)        │    └──────────────────────┘             │
│  │  status              │                                           │
│  │  view_count          │                                           │
│  │  create_time         │                                           │
│  │  update_time         │                                           │
│  └──────────┬───────────┘                                           │
│             │ N                                                     │
│             │                                                       │
│             │ 1                                                     │
│             ▼                                                       │
│  ┌──────────────────────┐                                           │
│  │   sys_category       │                                           │
│  │  ─────────────────── │                                           │
│  │  id (PK)             │                                           │
│  │  name                │                                           │
│  │  sort                │                                           │
│  │  create_time         │                                           │
│  └──────────────────────┘                                           │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘

表关系说明：

1. sys_user (用户表) 与 blog_article (文章表) 的关系：
   - 一对多关系（1:N）
   - 一个用户可以发布多篇文章
   - 外键：blog_article.user_id → sys_user.id

2. sys_user (用户表) 与 blog_comment (评论表) 的关系：
   - 一对多关系（1:N）
   - 一个用户可以发表多条评论
   - 外键：blog_comment.user_id → sys_user.id

3. blog_article (文章表) 与 blog_comment (评论表) 的关系：
   - 一对多关系（1:N）
   - 一篇文章可以有多条评论
   - 外键：blog_comment.article_id → blog_article.id

4. sys_category (分类表) 与 blog_article (文章表) 的关系：
   - 一对多关系（1:N）
   - 一个分类下可以有多篇文章
   - 外键：blog_article.category_id → sys_category.id
```

**ER 图（实体关系图）**：

```
┌─────────────┐         ┌─────────────┐         ┌─────────────┐
│  sys_user   │         │blog_article │         │sys_category │
├─────────────┤         ├─────────────┤         ├─────────────┤
│ id   (PK)   │◄────┐   │ id   (PK)   │◄────┐   │ id   (PK)   │
│ username    │     │   │ title       │     │   │ name        │
│ password    │     │   │ summary     │     │   │ sort        │
│ nickname    │     │   │ content     │     │   │ create_time │
│ avatar      │     │   │ category_id │────┘   └─────────────┘
│ role        │     │   │ user_id     │────┐
│ create_time │     │   │ status      │     │
└─────────────┘     │   │ view_count  │     │
                     │   │ create_time │     │
                     │   │ update_time │     │
                     │   └─────────────┘     │
                     │                      │
                     │         ┌─────────────┴─────┐
                     │         │                  │
                     │         ▼                  ▼
                     │   ┌─────────────┐   ┌─────────────┐
                     │   │blog_comment │   │blog_comment │
                     │   ├─────────────┤   ├─────────────┤
                     │   │ id   (PK)   │   │ id   (PK)   │
                     └───│ article_id  │   │ article_id  │
                         │ user_id     │   │ user_id     │
                         │ content     │   │ content     │
                         │ create_time │   │ create_time │
                         └─────────────┘   └─────────────┘
```

**数据流向说明**：

1. 用户注册/登录 → sys_user 表
2. 用户发布文章 → blog_article 表（关联 sys_user.id）
3. 用户发表评论 → blog_comment 表（关联 sys_user.id 和 blog_article.id）
4. 管理员创建分类 → sys_category 表
5. 文章关联分类 → blog_article.category_id 关联 sys_category.id

---

## 8. 接口文档

### 8.1 认证接口

#### 8.1.1 用户注册
- **接口**: POST /api/auth/register
- **权限**: 公开
- **请求参数**:
  ```json
  {
    "username": "testuser",
    "password": "123456",
    "nickname": "测试用户"
  }
  ```
- **响应示例**:
  ```json
  {
    "code": 200,
    "msg": "注册成功",
    "data": null
  }
  ```

#### 8.1.2 用户登录
- **接口**: POST /api/auth/login
- **权限**: 公开
- **请求参数**:
  ```json
  {
    "username": "testuser",
    "password": "123456"
  }
  ```
- **响应示例**:
  ```json
  {
    "code": 200,
    "msg": "操作成功",
    "data": {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
      "username": "testuser",
      "nickname": "测试用户",
      "role": "USER",
      "id": 1
    }
  }
  ```

#### 8.1.3 退出登录
- **接口**: POST /api/auth/logout
- **权限**: 公开
- **响应示例**:
  ```json
  {
    "code": 200,
    "msg": "退出成功",
    "data": null
  }
  ```

### 8.2 文章接口

#### 8.2.1 发布文章
- **接口**: POST /api/user/article/add
- **权限**: 需要登录
- **请求头**: Authorization: Bearer <token>
- **请求参数**:
  ```json
  {
    "title": "文章标题",
    "summary": "文章摘要",
    "content": "文章内容",
    "categoryId": 1
  }
  ```
- **响应示例**:
  ```json
  {
    "code": 200,
    "msg": "已提交审核",
    "data": null
  }
  ```

#### 8.2.2 查询文章列表
- **接口**: GET /api/public/article/list?categoryId=1
- **权限**: 公开
- **响应示例**:
  ```json
  {
    "code": 200,
    "msg": "操作成功",
    "data": [
      {
        "id": 1,
        "title": "文章标题",
        "summary": "文章摘要",
        "content": "文章内容",
        "categoryName": "技术分享",
        "nickname": "测试用户",
        "username": "testuser",
        "status": 1,
        "viewCount": 100,
        "createTime": "2025-12-25 10:00:00"
      }
    ]
  }
  ```

#### 8.2.3 查询文章详情
- **接口**: GET /api/public/article/{id}
- **权限**: 公开
- **响应示例**:
  ```json
  {
    "code": 200,
    "msg": "操作成功",
    "data": {
      "id": 1,
      "title": "文章标题",
      "summary": "文章摘要",
      "content": "文章内容",
      "categoryName": "技术分享",
      "nickname": "测试用户",
      "username": "testuser",
      "status": 1,
      "viewCount": 101,
      "createTime": "2025-12-25 10:00:00"
    }
  }
  ```

### 8.3 评论接口

#### 8.3.1 发表评论
- **接口**: POST /api/user/comment/add
- **权限**: 需要登录
- **请求头**: Authorization: Bearer <token>
- **请求参数**:
  ```json
  {
    "articleId": 1,
    "content": "评论内容"
  }
  ```
- **响应示例**:
  ```json
  {
    "code": 200,
    "msg": "评论成功",
    "data": null
  }
  ```

#### 8.3.2 查询评论列表
- **接口**: GET /api/public/comments/{articleId}
- **权限**: 公开
- **响应示例**:
  ```json
  {
    "code": 200,
    "msg": "操作成功",
    "data": [
      {
        "id": 1,
        "articleId": 1,
        "userId": 1,
        "content": "评论内容",
        "nickname": "测试用户",
        "avatar": "头像URL",
        "createTime": "2025-12-25 10:00:00"
      }
    ]
  }
  ```

### 8.4 管理员接口

#### 8.4.1 获取待审核文章列表
- **接口**: GET /api/admin/article/audit/list
- **权限**: 仅限管理员
- **请求头**: Authorization: Bearer <token>
- **响应示例**:
  ```json
  {
    "code": 200,
    "msg": "操作成功",
    "data": [
      {
        "id": 2,
        "title": "待审核文章",
        "summary": "文章摘要",
        "content": "文章内容",
        "categoryName": "技术分享",
        "nickname": "测试用户",
        "username": "testuser",
        "status": 0,
        "viewCount": 0,
        "createTime": "2025-12-25 10:00:00"
      }
    ]
  }
  ```

#### 8.4.2 审核文章
- **接口**: POST /api/admin/article/audit
- **权限**: 仅限管理员
- **请求头**: Authorization: Bearer <token>
- **请求参数**:
  ```json
  {
    "id": 2,
    "status": 1
  }
  ```
- **响应示例**:
  ```json
  {
    "code": 200,
    "msg": "审核操作已完成",
    "data": null
  }
  ```

#### 8.4.3 获取平台概览数据
- **接口**: GET /api/admin/article/summary
- **权限**: 仅限管理员
- **请求头**: Authorization: Bearer <token>
- **响应示例**:
  ```json
  {
    "code": 200,
    "msg": "操作成功",
    "data": {
      "articleCount": 100,
      "userCount": 50,
      "categoryCount": 10,
      "viewCount": 10000
    }
  }
  ```

---

## 9. 部署说明

### 9.1 环境要求
- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### 9.2 部署步骤

#### 9.2.1 数据库初始化
```sql
-- 创建数据库
CREATE DATABASE personal_blog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户表
CREATE TABLE sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(50),
    avatar VARCHAR(255),
    role VARCHAR(20) DEFAULT 'USER',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 创建分类表
CREATE TABLE sys_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    sort INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 创建文章表
CREATE TABLE blog_article (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    summary VARCHAR(500),
    content TEXT,
    category_id BIGINT,
    user_id BIGINT NOT NULL,
    status INT DEFAULT 0,
    view_count INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES sys_category(id),
    FOREIGN KEY (user_id) REFERENCES sys_user(id)
);

-- 创建评论表
CREATE TABLE blog_comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    article_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (article_id) REFERENCES blog_article(id),
    FOREIGN KEY (user_id) REFERENCES sys_user(id)
);

-- 插入管理员账号（密码：admin123，BCrypt 加密）
INSERT INTO sys_user (username, password, nickname, role) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '管理员', 'ADMIN');
```

#### 9.2.2 修改配置文件
编辑 `d:\37.prog\java\java_idea\Blog\blog-parent\blog-web\src\main\resources\application.yaml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/personal_blog?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
```

#### 9.2.3 编译打包
```bash
cd d:\37.prog\java\java_idea\Blog\blog-parent
mvn clean package -DskipTests
```

#### 9.2.4 运行应用
```bash
cd blog-web\target
java -jar blog-system-0.0.1-SNAPSHOT.jar
```

#### 9.2.5 访问应用
- 应用地址: http://localhost:8080
- 管理员账号: admin / admin123

---

## 10. 总结

### 10.1 技术栈总结
本项目采用 Spring Boot 3.4.0 作为核心框架，集成 Spring Security 实现安全认证，MyBatis-Plus 简化数据访问，JJWT 实现 JWT 无状态认证，MySQL 作为数据存储。整体架构清晰，模块划分合理，技术选型成熟稳定。

### 10.2 核心功能总结
项目实现了完整的博客系统功能，包括用户认证、文章管理、评论互动、分类管理、数据统计等核心模块。采用 RESTful API 设计风格，支持前后端分离架构，具有良好的扩展性和可维护性。

### 10.3 技术亮点
- JWT 无状态认证，支持分布式部署
- Spring Security 细粒度权限控制
- MyBatis-Plus 自动填充和条件构造器
- 全局异常处理，统一响应格式
- 多表关联查询，优化数据展示

### 10.4 后续优化方向
- 添加 Redis 缓存，提升查询性能
- 实现文章搜索功能（Elasticsearch）
- 添加文件上传功能（MinIO/OSS）
- 实现邮件通知功能
- 添加日志审计功能
- 实现数据备份和恢复

---

**文档版本**: 1.0  
**创建日期**: 2025-12-25  
**作者**: doom  
**项目路径**: d:\37.prog\java\java_idea\Blog\blog-parent
