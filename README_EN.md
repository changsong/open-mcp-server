# Open MCP Server

[**中文版**](https://github.com/changsong/open-mcp-server/blob/master/README.md)

Open-MCP-Server is a service framework supporting the Model Context Protocol (MCP), designed to streamline integration between your systems and AI platforms while providing business data to AI systems. It natively supports integration with various open APIs, internal system APIs, Dubbo interfaces, and other protocols, empowering your AI systems to address customized requirements.

Key features include:

- Unified adaptation of public/private APIs (including RESTful, gRPC, Dubbo)

- Context-aware data orchestration for AI workflows

- Extensible architecture for bespoke business logic

- This enables tailored AI solutions while maintaining compatibility with heterogeneous systems.

If you require integration with other public APIs or software systems, please contact me.

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
        "/the path of the jar/open-mcp-server-1.0-SNAPSHOT.jar",
        "juhe.news.api-key= Your juhe key"
      ]
    }
}
```


## License
This MCP OPEN Server is licensed under the MIT License. 
This means you are free to use, modify, and distribute the software, subject to the terms and conditions of the MIT License. For more details, please see the LICENSE file in the project repository.
