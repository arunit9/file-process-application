#!/bin/bash
echo "starting  mvn package of the file-upload-api SpringBoot REST"
cd file-process
mvn package

echo "Finished mvn package"

echo "Starting docker build of the file-process-api SpringBoot REST"
docker build -t arunit9/file-process-api .

echo "Finished docker build of the file-process-api SpringBoot REST"

echo "Starting docker build of file-process-web Angular"
cd ..
cd file-process-web
docker build -t arunit9/file-process-web .

echo "Finished docker build of file-process-web Angular"

echo "Starting docker stack"
cd ..
cd file-process
docker-compose up

echo "Script execution successful"
