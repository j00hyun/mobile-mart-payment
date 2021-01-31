#!/bin/bash

#./gradlew bootJar 
# wait 

DOCKER_APP_NAME=automart-app

EXIST_BLUE=$(docker ps | grep ${DOCKER_APP_NAME}-blue)

if [ -z "$EXIST_BLUE" ]; then
    echo "blue up"
    docker-compose -f docker-compose.yml up -d --build ${DOCKER_APP_NAME}-blue-A
    docker-compose -f docker-compose.yml up -d --build ${DOCKER_APP_NAME}-blue-B
    echo "blue port(8081, 8083)로 변경합니다"
    docker exec -it nginx-for-atmt-server sh -c "cd /etc/nginx/conf.d; echo \"server 127.0.0.1:8081 max_fails=3 fail_timeout=10s;\nserver 127.0.0.1:8083 max_fails=3 fail_timeout=10s;\" |tee /etc/nginx/conf.d/upstream.conf" 
    echo "nginx reload"
    docker exec -it nginx-for-atmt-server nginx -s reload
    sleep 40

    docker stop ${DOCKER_APP_NAME}-green-A
    docker stop ${DOCKER_APP_NAME}-green-B
    docker rm ${DOCKER_APP_NAME}-green-A
    docker rm ${DOCKER_APP_NAME}-green-B
else
    echo "green up"
    docker-compose -f docker-compose.yml up -d --build ${DOCKER_APP_NAME}-green-A
    docker-compose -f docker-compose.yml up -d --build ${DOCKER_APP_NAME}-green-B
    echo "green port(8082, 8084)로 변경합니다"
    docker exec -it nginx-for-atmt-server sh -c "cd /etc/nginx/conf.d; echo \"server 127.0.0.1:8082 max_fails=3 fail_timeout=10s;\nserver 127.0.0.1:8084 max_fails=3 fail_timeout=10s;\" |tee /etc/nginx/conf.d/upstream.conf"
    echo "nginx reload"
    docker exec -it nginx-for-atmt-server nginx -s reload
    sleep 40

    docker stop ${DOCKER_APP_NAME}-blue-A
    docker stop ${DOCKER_APP_NAME}-blue-B
    docker rm ${DOCKER_APP_NAME}-blue-A
    docker rm ${DOCKER_APP_NAME}-blue-B
fi
