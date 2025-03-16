# Open MCP Server

[**English Version**](https://github.com/changsong/open-mcp-server/blob/master/README_EN.md)

Open-MCP-Server 是一个支持模型上下文协议（Model Context Protocol, MCP）的服务框架，旨在简化企业系统与AI平台的集成，为AI系统提供业务数据支持。本框架原生兼容各类开放API、内部系统API、Dubbo接口等多协议接入，赋能AI系统实现定制化需求。

核心功能：

- 公/私有API统一适配（支持 RESTful、gRPC、Dubbo 等协议）

- AI工作流上下文感知数据编排

- 可扩展的业务逻辑架构

- 在保持异构系统兼容性的同时，实现定制化AI解决方案

如需集成其他公共API或软件系统，请联系开发者。

### Supported Third-party APIs
[juehe data](https://www.juhe.cn/)


### Waitlist for New Third-party APIs 

## Configuration

```json
"mcpServers": {
"open-mcp-server": {
"command": "java",
"args": [
"-Dspring.ai.mcp.server.stdio=true",
"-jar",
"/jar包路径/open-mcp-server-1.0-SNAPSHOT.jar",
"juhe.news.api-key= 您的聚合密钥"
]
}
}
```


## License
open-mcp-server 采用 MIT 许可证授权。
根据 MIT 许可证条款，您可以自由使用、修改和分发本软件。具体条款请参阅项目仓库中的 LICENSE 文件。