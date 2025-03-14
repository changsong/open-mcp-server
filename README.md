# Open MCP Server

A Model Context Protocol (MCP)  for numerous third-party software and third-party APIs. 
Once deployed, this service can offer enhanced support for your AI platform, such as enabling you to use Dofi to complete your platform

### Supported Third-party APIs


### Supported Third-party Software


### Waitlist for New Third-party APIs 


### Waitlist for New Third-party Software

## Configuration

```json
"mcpServers": {
   "open-mcp-server": {
      "command": "java",
      "args": [
        "-Dspring.ai.mcp.server.stdio=true",
        "-jar",
        "/the path of the jar/open-mcp-server-1.0-SNAPSHOT.jar",
        "jina.api.api-key= Your jina key"
      ]
    }
}
```


## License
This MCP OPEN Server is licensed under the MIT License. 
This means you are free to use, modify, and distribute the software, subject to the terms and conditions of the MIT License. For more details, please see the LICENSE file in the project repository.
