#!/bin/bash

redis_container=redis-test

echo "stopping redis container $redis_container"
docker stop $redis_container

echo "removing redis container $redis_container"
docker rm $redis_container

echo "starting redis container $redis_container"
docker run --name $redis_container -p 6379:6379 -v ~/.docker_volumes/$redis_container:/data redis

echo "finished 😁"