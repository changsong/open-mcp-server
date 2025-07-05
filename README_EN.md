# Open MCP Server

[**中文版**](https://github.com/changsong/open-mcp-server/blob/master/README.md)

Open-MCP-Server is a service framework supporting the Model Context Protocol (MCP), designed to streamline integration between enterprise systems and AI platforms while providing business data support. The framework natively supports various protocols including open APIs, internal system APIs, and Dubbo interfaces, enabling AI systems to implement customized requirements.

## Core Features

 - Unified API adaptation for public/private services (supporting RESTful, gRPC, Dubbo, SOAP protocols)
- Context-aware data orchestration for AI workflows
- Extensible business logic architecture
- Custom AI solutions while maintaining heterogeneous system compatibility
- Intelligent AI assistant session management
- Third-party API integration (Juhe Data, stocks, news, videos, etc.)
- Automatic code generation functionality

## External API Functions

### 1. API Testing Interface

#### API Testing
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

**Features:**
- Support testing any HTTP API interface
- Automatic URL parameter handling for GET requests
- Cookie authentication support
- Support all HTTP methods (GET, POST, PUT, DELETE, etc.)
- Return JSON format test results

### 2. Code Generation Functionality

#### Generate Code
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

#### Download Generated Code
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

**Features:**
- Automatic API interface definition generation
- Request/Response DTO class generation
- Service implementation class generation
- Automatic parameter mapping and type conversion
- File download support
- Complete Spring Boot integration code generation

### 3. AI Assistant Functionality

#### Single Q&A
```bash
GET /ai-assistant/ai/ask?question=your_question
```

#### Create Session
```bash
POST /ai-assistant/session/create
```

#### Session Chat
```bash
POST /ai-assistant/session/chat
Content-Type: application/x-www-form-urlencoded

sessionId=xxx&message=user_message&systemPrompt=system_prompt
```

#### Get Session History
```bash
GET /ai-assistant/session/history?sessionId=xxx
```

#### Clear Session
```bash
POST /ai-assistant/session/clear
Content-Type: application/x-www-form-urlencoded

sessionId=xxx&reset=true
```

**Features:**
- Support single Q&A and session modes
- Dynamic MCP tool injection
- Session state management
- Customizable system prompts
- Complete conversation history records

### 4. AI Generation Functionality

#### Text Generation
```bash
GET /ai/generate?message=generation_content
```

#### Streaming Generation
```bash
GET /ai/generateStream?message=generation_content
```

#### MCP Tool Invocation
```bash
GET /ai/mcp?message=question_using_tools
```

**Features:**
- Azure OpenAI-based text generation
- Streaming response support
- MCP tool integration
- Support for multiple deployment models

### 5. Multi-Protocol Service Management

#### HTTP Service Management
```bash
# Get service list
GET /api/http/list

# Get single service
GET /api/http/{id}

# Create service
POST /api/http/save
Content-Type: application/json

# Update service
PUT /api/http/{id}
Content-Type: application/json

# Delete service
DELETE /api/http/{id}
```

#### Dubbo Service Management
```bash
# Get service list
GET /api/dubbo/list

# Get single service
GET /api/dubbo/{id}

# Create service
POST /api/dubbo/save
Content-Type: application/json

# Update service
PUT /api/dubbo/{id}
Content-Type: application/json

# Delete service
DELETE /api/dubbo/{id}
```

#### gRPC Service Management
```bash
# Get service list
GET /api/grpc/list

# Get single service
GET /api/grpc/{id}

# Create service
POST /api/grpc/save
Content-Type: application/json

# Update service
PUT /api/grpc/{id}
Content-Type: application/json

# Delete service
DELETE /api/grpc/{id}
```

#### SOAP Service Management
```bash
# Get service list
GET /api/soap/list

# Get single service
GET /api/soap/{id}

# Create service
POST /api/soap/save
Content-Type: application/json

# Update service
PUT /api/soap/{id}
Content-Type: application/json

# Delete service
DELETE /api/soap/{id}
```

### 6. Registry Connection Management

#### Basic CRUD Operations
```bash
# Get connection list
GET /api/registry/list

# Get single connection
GET /api/registry/{id}

# Create connection
POST /api/registry/save
Content-Type: application/json

# Update connection
PUT /api/registry/{id}
Content-Type: application/json

# Delete connection
DELETE /api/registry/{id}
```

#### Advanced Features
```bash
# Paginated query
GET /api/registry/page?connectionName=xxx&registryType=xxx&host=xxx&pageNum=1&pageSize=10

# Batch save
POST /api/registry/batch-save
Content-Type: application/json

# Batch delete
POST /api/registry/batch-delete
Content-Type: application/json
```

### 7. Language Settings

```bash
POST /api/setLanguage
Content-Type: application/json

{
    "language": "en_US"
}
```

## MCP Tool Functions

### Dubbo Service Tools

#### Get Service Information
```bash
# Tool Name: getDubboServiceInfo
# Parameters:
{
    "interfaceName": "com.example.DemoService"  # Fully qualified Dubbo service interface name
}
```

#### Invoke Service Method
```bash
# Tool Name: invokeDubboService
# Parameters:
{
    "interfaceName": "com.example.DemoService",  # Fully qualified Dubbo service interface name
    "methodName": "sayHello",                    # Method name to invoke
    "version": "1.0.0",                         # Service version (optional)
    "params": {                                 # Method parameters (JSON format)
        "param1": "value1",
        "param2": 123
    }
}
```

## Configuration

### Dubbo Configuration
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


### MCP Server Configuration
```json
"mcpServers": {
    "open-mcp-server": {
        "command": "java",
        "args": [
            "-Dspring.ai.mcp.server.stdio=true",
            "-jar",
            "/path/to/jar/open-mcp-server-1.0-SNAPSHOT.jar",
            "juhe.news.api-key=YOUR_JUHE_API_KEY"
        ]
    }
}
```

## Supported Features

### Multi-Protocol Support
- **HTTP/RESTful**: Complete CRUD operations and API testing
- **Dubbo**: Generic invocation, service discovery, parameter conversion
- **gRPC**: Service management and invocation
- **SOAP**: Web service support

### AI Integration
- **Azure OpenAI**: Text generation and conversation
- **MCP Tools**: Dynamic tool injection and invocation
- **Session Management**: Multi-session support and state persistence
- **Streaming Response**: Real-time data stream processing

### Development Tools
- **Code Generation**: Automatic API integration code generation
- **API Testing**: Online API testing tools
- **Registry Management**: Multi-registry connection management
- **Internationalization**: Multi-language support

### Third-party APIs
- **Juhe Data**: News, video, stock and other data services
- **Extensible**: Support for adding more third-party APIs

## License
Open-MCP-Server is licensed under the MIT License.
You are free to use, modify, and distribute this software under the terms of the MIT License. For detailed terms, please refer to the LICENSE file in the project repository.
