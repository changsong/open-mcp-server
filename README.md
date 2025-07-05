# Open MCP Server

[**English Version**](https://gitee.com/changsong/open-mcp-server/blob/main/README_EN.md)

Open-MCP-Server 是一个支持模型上下文协议（Model Context Protocol, MCP）的服务框架，旨在简化企业系统与AI平台的集成，为AI系统提供业务数据支持。本框架原生兼容各类开放API、内部系统API、Dubbo接口等多协议接入，赋能AI系统实现定制化需求。

## 核心功能

- 公/私有API统一适配（支持 RESTful、gRPC、Dubbo、SOAP 等协议）
- AI工作流上下文感知数据编排
- 可扩展的业务逻辑架构
- 在保持异构系统兼容性的同时，实现定制化AI解决方案
- 智能AI助手会话管理
- 第三方API集成（聚合数据、股票、新闻、视频等）
- 代码自动生成功能

## 对外接口功能

### 1. API测试接口

#### 接口测试
```bash
POST /api/test
Content-Type: application/json

{
    "apiUrl": "http://api.example.com/data",
    "method": "GET",
    "cookie": "session=abc123",
    "requestData": "{\"param1\": \"value1\"}"
}
```

**功能说明：**
- 支持测试任意HTTP API接口
- 自动处理GET请求的URL参数拼接
- 支持Cookie认证
- 支持所有HTTP方法（GET、POST、PUT、DELETE等）
- 返回JSON格式的测试结果

### 2. 代码生成功能

#### 生成代码
```bash
POST /api/generate
Content-Type: application/json

{
    "apiUrl": "http://api.example.com/data",
    "method": "GET",
    "packagePath": "com.open.mcp.server.api",
    "apiName": "ExampleApi",
    "requestData": "{\"param1\": \"value1\"}",
    "responseData": "{\"result\": \"success\"}"
}
```

#### 下载生成的代码
```bash
POST /api/download
Content-Type: application/json

{
    "apiUrl": "http://api.example.com/data",
    "method": "GET",
    "packagePath": "com.open.mcp.server.api",
    "apiName": "ExampleApi",
    "requestData": "{\"param1\": \"value1\"}",
    "responseData": "{\"result\": \"success\"}"
}
```

**功能说明：**
- 自动生成API接口定义
- 生成请求/响应DTO类
- 生成服务实现类
- 自动处理参数映射和类型转换
- 支持文件下载功能
- 生成完整的Spring Boot集成代码

### 3. AI助手功能

#### 单次问答
```bash
GET /ai-assistant/ai/ask?question=你的问题
```

#### 创建会话
```bash
POST /ai-assistant/session/create
```

#### 会话聊天
```bash
POST /ai-assistant/session/chat
Content-Type: application/x-www-form-urlencoded

sessionId=xxx&message=用户消息&systemPrompt=系统提示词
```

#### 获取会话历史
```bash
GET /ai-assistant/session/history?sessionId=xxx
```

#### 清理会话
```bash
POST /ai-assistant/session/clear
Content-Type: application/x-www-form-urlencoded

sessionId=xxx&reset=true
```

**功能说明：**
- 支持单次问答和会话模式
- 动态注入MCP工具
- 会话状态管理
- 系统提示词自定义
- 完整的对话历史记录

### 4. AI生成功能

#### 文本生成
```bash
GET /ai/generate?message=生成内容
```

#### 流式生成
```bash
GET /ai/generateStream?message=生成内容
```

#### MCP工具调用
```bash
GET /ai/mcp?message=使用工具的问题
```

**功能说明：**
- 基于Azure OpenAI的文本生成
- 支持流式响应
- 集成MCP工具调用
- 支持多种部署模型

### 5. 多协议服务管理

#### HTTP服务管理
```bash
# 获取服务列表
GET /api/http/list

# 获取单个服务
GET /api/http/{id}

# 创建服务
POST /api/http/save
Content-Type: application/json

# 更新服务
PUT /api/http/{id}
Content-Type: application/json

# 删除服务
DELETE /api/http/{id}
```

#### Dubbo服务管理
```bash
# 获取服务列表
GET /api/dubbo/list

# 获取单个服务
GET /api/dubbo/{id}

# 创建服务
POST /api/dubbo/save
Content-Type: application/json

# 更新服务
PUT /api/dubbo/{id}
Content-Type: application/json

# 删除服务
DELETE /api/dubbo/{id}
```

#### gRPC服务管理
```bash
# 获取服务列表
GET /api/grpc/list

# 获取单个服务
GET /api/grpc/{id}

# 创建服务
POST /api/grpc/save
Content-Type: application/json

# 更新服务
PUT /api/grpc/{id}
Content-Type: application/json

# 删除服务
DELETE /api/grpc/{id}
```

#### SOAP服务管理
```bash
# 获取服务列表
GET /api/soap/list

# 获取单个服务
GET /api/soap/{id}

# 创建服务
POST /api/soap/save
Content-Type: application/json

# 更新服务
PUT /api/soap/{id}
Content-Type: application/json

# 删除服务
DELETE /api/soap/{id}
```

### 6. 注册中心连接管理

#### 基础CRUD操作
```bash
# 获取连接列表
GET /api/registry/list

# 获取单个连接
GET /api/registry/{id}

# 创建连接
POST /api/registry/save
Content-Type: application/json

# 更新连接
PUT /api/registry/{id}
Content-Type: application/json

# 删除连接
DELETE /api/registry/{id}
```

#### 高级功能
```bash
# 分页查询
GET /api/registry/page?connectionName=xxx&registryType=xxx&host=xxx&pageNum=1&pageSize=10

# 批量保存
POST /api/registry/batch-save
Content-Type: application/json

# 批量删除
POST /api/registry/batch-delete
Content-Type: application/json
```

### 7. 语言设置

```bash
POST /api/setLanguage
Content-Type: application/json

{
    "language": "zh_CN"
}
```

## MCP工具功能

### Dubbo服务工具

#### 获取服务信息
```bash
# 工具名称: getDubboServiceInfo
# 参数说明:
{
    "interfaceName": "com.example.DemoService"  # Dubbo服务接口全限定名
}
```

#### 调用服务方法
```bash
# 工具名称: invokeDubboService
# 参数说明:
{
    "interfaceName": "com.example.DemoService",  # Dubbo服务接口全限定名
    "methodName": "sayHello",                    # 要调用的方法名
    "version": "1.0.0",                         # 服务版本（可选）
    "params": {                                 # 方法参数（JSON格式）
        "param1": "value1",
        "param2": 123
    }
}
```

## 配置说明

### Dubbo配置
```yaml
dubbo:
  application:
    name: ${spring.application.name}
    qos-enable: false
  registry:
    address: zookeeper://127.0.0.1:2181
    timeout: 30000
  protocol:
    name: dubbo
    port: -1
  consumer:
    check: false
    timeout: 30000
    retries: 0
```

### 聚合数据API配置
```yaml
juhe:
  news:
    apikey: your_api_key
    baseUrl: http://v.juhe.cn
  video:
    apikey: ${juhe.news.apikey}
    baseUrl: ${juhe.news.baseUrl}
```

### MCP服务器配置
```json
"mcpServers": {
    "open-mcp-server": {
        "command": "java",
        "args": [
            "-Dspring.ai.mcp.server.stdio=true",
            "-jar",
            "/jar包路径/open-mcp-server-1.0-SNAPSHOT.jar",
            "juhe.news.api-key=您的聚合密钥"
        ]
    }
}
```

## 特性支持

### 多协议支持
- **HTTP/RESTful**: 完整的CRUD操作和API测试
- **Dubbo**: 泛化调用、服务发现、参数转换
- **gRPC**: 服务管理和调用
- **SOAP**: Web服务支持

### AI集成
- **Azure OpenAI**: 文本生成和对话
- **MCP工具**: 动态工具注入和调用
- **会话管理**: 多会话支持和状态保持
- **流式响应**: 实时数据流处理

### 开发工具
- **代码生成**: 自动生成API集成代码
- **接口测试**: 在线API测试工具
- **注册中心管理**: 多注册中心连接管理
- **国际化**: 多语言支持

### 第三方API
- **聚合数据**: 新闻、视频、股票等数据服务
- **可扩展**: 支持添加更多第三方API

## License
open-mcp-server 采用 MIT 许可证授权。
根据 MIT 许可证条款，您可以自由使用、修改和分发本软件。具体条款请参阅项目仓库中的 LICENSE 文件。