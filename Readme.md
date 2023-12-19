# Java Code Compiler

This project implements a Java compiler using JavaParser and Javassist. The compiler parses Java source code, generates corresponding bytecode, and provides functionality for code generation and manipulation..


## Prerequisites

Ensure you have the following prerequisites installed on your system:

- [Java SDK](https://www.oracle.com/java/technologies/javase-downloads.html) (version 8 or later)
- [Maven](https://maven.apache.org/download.cgi) (for building the project)

## Build

Build the project using Maven:

```bash
mvn clean install
```
## Features

*   **Bytecode Generation:** Generate bytecode from parsed Java source code.
*   **Code Manipulation:** Provide tools for manipulating Java bytecode.
*   **Custom Class Generation:** Dynamically create and save new Java classes.
*  **Javassist Integration:** Javassist for bytecode manipulation.

## Usage
Here's an example Java code snippet demonstrating how to use the compiler:
```java
try {
    byte[] errTrice = JavaCompiler.getByteCode(JavaFile);
} catch (Exception ex) {
    ex.printStackTrace();
}
```
## Testing

All available tests can be found in `/src/test/java`. The `/src/test/resources/` directory contains files with the source code of the Java code for the tests.
```bash
mvn clean compile test
```


