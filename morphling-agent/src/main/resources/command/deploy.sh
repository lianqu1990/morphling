#!/bin/bash

if [ -z "$project_name" ]; then
    echo 'deploy error,cant get project name!'
    exit 1;
fi

deploy(){
    echo "download package from server,project is ${project_name},tag is ${tag}..."
    wget -qO /app/tmp/${project_name}.tag.gz http://192.168.100.20:11110/download/${project_name}_${tag}.tar.gz
    echo "stop server ..."
    /app/${project_name}/server.sh stop
    echo "install package..."
    rm -rf /app/${project_name}/*
    tar -zvxf /app/tmp/${project_name}.tag.gz -C /app
    echo "start server..."
    /app/${project_name}/server.sh start
}

case "$1" in
    start)
        /app/${project_name}/server.sh start
    ;;
    stop)
        /app/${project_name}/server.sh stop
    ;;
    status)
        /app/${project_name}/server.sh status
    ;;
    restart)
        /app/${project_name}/server.sh restart
    ;;
    dump)
        /app/${project_name}/server.sh dump
    ;;
    deploy)
        deploy
    ;;
    *)
        echo "Usage ${0} <start|stop|status|restart|dump>"
        exit 1
    ;;
esac