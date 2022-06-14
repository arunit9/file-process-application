# file-process-application

## Application Overview

The File Process Application Consists of 3 components
- file-process: Spring Boot REST services for user authentication, files and statistics
- file-process-web: Angular front-end web application
- MySQL database

Once the build/run scripts are executed, a docker stack will be running with 3 containers corresponding to the above components

## Building and Running the Application

### Build

#### Prerequisites

The following are required:
- Jdk 1.8 (preferred) or jdk 11
- Maven
- Docker
- docker-compose
- port 8080, 8081 and 3306 must be free

If you do not have the above installed, please run below supplementary scripts in the given order. Executable permission need to be given prior to execution.
- sudo su
- chmod +x setup.sh
- chmod +x docker_setup.sh
- chmod +x docker_compose_setup.sh
- ./setup.sh
- ./docker_setup.sh
- ./docker_compose_setup.sh

Please note that the command line will prompt a few questions while running for which you need to type “y” (yes).

Once done, please verify maven, java, docker and docker-compose versions to see if installation had been successful.

#### Project Build

The project can be cloned or downloaded from https://github.com/arunit9/file-process-application.git

Open a terminal inside the file-process-application directory. The project_build.sh is located here. This file will build docker images and run docker containers for the file-process (SpringBoot REST API), MySQL database and the file-process-web (Angular).

**NOTE: This script contains docker commands, hence you may have to sudo su**

- chmod +x project_build.sh
- ./project_build.sh

Once the execution has completed, you should see 3 docker containers as below.
- file-process-web at port 8081
- file-process-api at port 8080
- app-mysql at port 3306

The application can be accessed via a web browser at localhost:8081

### Run

The above process can be run, re-run as many times as you like to build and run the application. The project_run.sh is provided as a quicker option to simply just run the application. It will simply start the docker stack using docker images in your local docker (file-process-api and file-process-web) or Docker Hub (app-mysql). Please ensure that you have run the project_build.sh atleast once before running this script.

**NOTE: I have not pushed my docker images to the Docker Hub**


- chmod +x project_run.sh
- ./project_run.sh

### Stop
The project_stop.sh can be executed to stop the application (and the docker stack).

- chmod +x project_stop.sh
- ./project_stop.sh

**NOTE: The file process Spring Boot application is set to delete the content of the file upload directory and drop and recreate the database tables at start.**

## Design
Following decisions were made to the best of my best knowledge considering the requirements of the assignment.

### Queue
A java based queue was implemented within the file-process Spring Boot application rather than using an out of the box message broker queue such as ActiveMQ. The REST methods will add filenames to the queue and a seperate thread will poll the queue and process the files asynchronously. This was based on the below points of the assignment,

- Implement "internal" processing queue system
- Make processing run in a separate thread using appropriate Spring tools

### Filenames
Filenames will be considered unique by itself (irrespective of the user who uploads them). Hence, duplicate filenames will overwrite data in the queue, database and the file upload directory. This was based on the below point of the assignment,

- Unprocessed files (waiting in the queue) would be replaced in the queue by the files with the  same name being uploaded. (But put at the end of the queue.)

### File processing
Only files having .opi extension would be accepted by the REST methods. The content should be in xml format. A .opi file not having XML data would still be accepted by the REST. However, it will fail during file processing later and will be indicated to the user with a "Error_" prefix attached to the filename. No statistics will be available for such files.

- Valid users can upload a BOY format file

The xml will be parsed and a count of ocurrences of each unique element - subelements - parameter combination will be calculated based on XML nodes and their child nodes until a node with no childdren is reached. It is assumed that the "parameter" mentioned in the assignment is the name of the node with no child nodes (not the value of that node). This was assumed considering the purpose of a .opi file is to configure components in a Control System.

- The application should parse the input BOY format file, count the occurrences of all the elements and their parameters, and store the statistics in a DB.
- The statistics should be presented in dot-separated  format (e(0).e(1).e(2)....e(n)) where the e(x) represents the element name, the e(x+1) is the sub-element  of e(x) and e(n) is the parameter name. 

**Example 01:**
```xml
<display typeId="org.csstudio.opibuilder.Display" version="1.0.0">
    <widget typeId="org.csstudio.opibuilder.widgets.TextUpdate" version="1.0">
        <border_alarm_sensitive>true</border_alarm_sensitive>
        <visible>true</visible>
        <vertical_alignment>1</vertical_alignment>
        <show_units>true</show_units>
    </widget>
</display>
```

**Statistics**
- display = 1
- display.widget = 1
- display.widget.border_alarm_sensitive = 1
- display.widget.visible = 1
- display.widget.vertical_alignment = 1
- display.widget.show_units = 1

**Example 02:**
```xml
<items>
    <item id="0001" type="donut">
        <name>Cake</name>
        <ppu>0.55</ppu>
        <batters>
            <batter id="1001">Regular</batter>
            <batter id="1002">Chocolate</batter>
            <batter id="1003">Blueberry</batter>
        </batters>
        <topping id="5001">None</topping>
        <topping id="5002">Glazed</topping>
        <topping id="5005">Sugar</topping>
        <topping id="5006">Sprinkles</topping>
        <topping id="5003">Chocolate</topping>
        <topping id="5004">Maple</topping>
    </item>
    <item id="0001" type="donut">
        <name>Cake</name>
        <ppu>0.55</ppu>
        <batters>
            <batter id="1001">Regular</batter>
            <batter id="1002">Chocolate</batter>
            <batter id="1003">Blueberry</batter>
        </batters>
        <topping id="5001">None</topping>
        <topping id="5002">Glazed</topping>
        <topping id="5005">Sugar</topping>
        <topping id="5006">Sprinkles</topping>
        <topping id="5003">Chocolate</topping>
        <topping id="5004">Maple</topping>
    </item>
</items>
```

**Statistics**
- items = 1
- items.item = 2
- items.item.ppu = 2
- items.item.name = 2
- items.item.topping = 12
- items.item.batters = 2
- items.item.batters.batter = 6

### User Interface
Angular was used for the user interface rather than server rendered HTML content such as JSP, Thymleaf. Angular provides a better user experience and code maintainability and has extensive support for REST services and handing access tokens. Angular was selected over JavaFX since it is by definition a technology for web applications where as JavaFX is a client application platform for desktop, mobile and embedded systems although it can be deployed to run inside a web browser. This was based on the following points of the assignment,

- Design and implement a Spring Boot web application
- Enable user login and the usage of access token in subsequent requests