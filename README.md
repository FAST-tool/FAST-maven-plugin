# FAST-maven-plugin

This repository is a implementation of the [FAST Approaches to Scalable Similarity-based Test Case Prioritization](https://github.com/icse18-FAST/FAST) project and the [FAST-Maven](https://github.com/FAST-tool/maven-FAST), it is based on the following publication:

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
   git clone https://github.com/FAST-tool/maven-FAST-plugin/
   ```

2. Open the repository where the FAST-maven-plugin repository was cloned:
    ```bash
    cd maven-FAST-plugin
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
                    <groupId>br.ufpe.cin.fast-tool</groupId>
                    <artifactId>fast-maven-plugin</artifactId>
                    <version>1.0.0</version>
                    <configuration>
                        <algorithm>FAST-pw</algorithm>
                    </configuration>
                    <executions>
                        <execution>
                            <goals>
                                <goal>FAST</goal>
                            </goals>
                        </execution>
                    </executions>
                </plugin>
                ...
            </plugins>
            ...
        </build>
        ```

   NOTE: The plugin will soon be published on [Maven Central Repository](https://search.maven.org/)

5. Run the project tests

      ```bash

         mvn test
      ```
      
      NOTE: **It is necessary that all test classes of your project are public to run the prioritized tests**


6. If your project uses the plugin [apache-rat-plugin](https://mvnrepository.com/artifact/org.apache.rat/apache-rat-plugin) add the following lines to the file pom.xml
      ```xml
         <plugin>
            <groupId>org.apache.rat</groupId>
            <artifactId>apache-rat-plugin</artifactId>
            <configuration>
               <excludes>
                  <exclude>.fast/**</exclude>
                  <exclude>src/test/java/fast/**</exclude>
                  ...
               </excludes>
            </configuration>
         </plugin>

      ```
   
7. (OPTIONAL) Instrumented execution

   By default, when running the tests prioritized by JUnit, the test execution report is not displayed;
   In order to run the tests while the report is displayed, a tool was developed that implements the test code and displays the execution report.

   To instrument the code, just add the following lines to the project's Java test classes:
      ```java
         import org.junit.jupiter.api.extension.ExtendWith;
         import fast.FASTTestWatcher;


         @ExtendWith(FASTTestWatcher.class)
      ```

      - To add instrumentation to the project's test classes, just run the command below:
      
         Note: The ```<subject>``` is the path of your project, for example '../my-projects/calculator'

         ```bash
            python3 ~/.m2/repository/br/ufpe/cin/fast-tool/fast-maven-plugin/1.0.0/FAST/tools/project-instrumentation.py <subject> add_instrumentation_to_the_project
         ```

      - To remove the instrumentation from the test classes, just run the command below:
         ```bash
            python3 ~/.m2/repository/br/ufpe/cin/fast-tool/fast-maven-plugin/1.0.0/FAST/tools/project-instrumentation.py <subject> add_instrumentation_to_the_project
         ```
