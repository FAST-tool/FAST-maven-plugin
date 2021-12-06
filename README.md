# FAST-maven-plugin

This repository is a implementation of the [FAST Approaches to Scalable Similarity-based Test Case Prioritization](https://github.com/icse18-FAST/FAST) project and the [FAST parameterized](https://github.com/DinoSaulo/FAST-parameterized), it is based on the following publication:

> Breno Miranda, Emilio Cruciani, Roberto Verdecchia, and Antonia Bertolino. 2018. FAST Approaches to Scalable Similarity-based Test Case Prioritization. In *Proceedings of ICSE’18: 40th International Conference on Software Engineering, Gothenburg, Sweden, May 27-June 3, 2018 (ICSE’18)*, 11 pages. DOI: [10.1145/3180155.3180210](http://dx.doi.org/10.1145/3180155.3180210)


It contains materials needed to perform FAST project prioritization for projects developed using Java in conjunction with [Maven](https://maven.apache.org/)

Project Replication
---------------
In order to replicate the project follow these steps:

### Pre-requisites

1. Have git installed - [Download](https://git-scm.com/downloads)

2. Have Python version 3 installed - [Download](https://www.python.org/downloads/)

3. Have the pip installed - [How to install pip](https://pip.pypa.io/en/stable/cli/pip_install/)

4. Have the Java version 11+ installed - [Download](https://www.oracle.com/java/technologies/downloads/)

### Getting started

1. Clone the repository:
   ```bash
   git clone https://github.com/DinoSaulo/FAST-maven-plugin
   ```

2. Open the repository where the FAST-maven-plugin repository was cloned:
    ```bash
    cd FAST-maven-plugin
    ```

3. Install the plugin in your environment:
   ```bash
   mvn install
   ```

4. Add the following lines to the Maven project that will have tests prioritized:

    - Dependencies

        ```xml
        </dependencies>
            ...
            <!-- https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter -->
            <dependency>
                <groupId>org.junit.jupiter</groupId>
                <artifactId>junit-jupiter</artifactId>
                <version>5.8.1</version>
                <scope>test</scope>
            </dependency>
            <!-- https://mvnrepository.com/artifact/org.junit.platform/junit-platform-runner -->
            <dependency>
                <groupId>org.junit.platform</groupId>
                <artifactId>junit-platform-runner</artifactId>
                <version>1.5.2</version>
                <scope>test</scope>
            </dependency>
            ...
        </dependencies>
        ```

    - Plugins

        ```xml
        <build>
            ...
            <plugins>
                ...
                <!-- https://mvnrepository.com/artifact/org.apache.maven.plugins/maven-surefire-plugin -->
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-surefire-plugin</artifactId>
                    <version>2.19.1</version>
                    <configuration>
                    <includes>
                        <include>**/FASTPrioritizedSuite.java</include>
                    </includes>
                    </configuration>
                </plugin>
                <plugin>
                    <groupId>sab2.br.ufpe.cin</groupId>
                    <artifactId>fast-maven-plugin</artifactId>
                    <version>0.0.1-SNAPSHOT</version>
                    <configuration>
                        <command>FAST-pw</command>
                    </configuration>
                    <executions>
                        <execution>
                            <goals>
                                <goal>version</goal>
                            </goals>
                        </execution>
                    </executions>
                </plugin>
                ...
            </plugins>
            ...
        </build>
        ```

- NOTE: The plugin will soon be published on [Maven Central Repository](https://search.maven.org/)