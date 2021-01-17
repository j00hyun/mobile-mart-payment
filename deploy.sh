#!/bin/bash

DOCKER_APP_NAME=automart-app

EXIST_BLUE=$(docker ps | grep ${DOCKER_APP_NAME}-blue)

if [ -z "$EXIST_BLUE" ]; then
    echo "blue up"
    docker-compose -f docker-compose.yml up -d --build ${DOCKER_APP_NAME}-blue-A
    docker-compose -f docker-compose.yml up -d --build ${DOCKER_APP_NAME}-blue-B

    sleep 10

    docker stop ${DOCKER_APP_NAME}-green-A
    docker stop ${DOCKER_APP_NAME}-green-B
    docker rm ${DOCKER_APP_NAME}-green-A
    docker rm ${DOCKER_APP_NAME}-green-B
else
    echo "green up"
    docker-compose -f docker-compose.yml up -d --build ${DOCKER_APP_NAME}-green-A
    docker-compose -f docker-compose.yml up -d --build ${DOCKER_APP_NAME}-green-B

    sleep 10

    docker stop ${DOCKER_APP_NAME}-blue-A
    docker stop ${DOCKER_APP_NAME}-blue-B
    docker rm ${DOCKER_APP_NAME}-blue-A
    docker rm ${DOCKER_APP_NAME}-blue-B
fi
