#!/bin/bash
cd `dirname $0`


# 使用自己的conf文件传递到spring启动脚本种
source ./conf/run.conf


DEPLOY_DIR=`pwd`

SERVER_NAME=${server_name}

if [ -z "$SERVER_NAME" ]; then
    echo 'package error,cant get server name!'
    exit 1;
fi

LOGS_DIR=${log_dir}
if [ -z "$LOGS_DIR" ]; then
    LOGS_DIR=/app/logs/${server_name}/
fi
if [ ! -d ${LOGS_DIR} ]; then
    mkdir -p ${LOGS_DIR}
fi

PID_DIR=${pid_dir}
if [ -z "$PID_DIR" ]; then
    PID_DIR=/app/data/run/
fi
if [ ! -d ${PID_DIR} ]; then
    mkdir -p ${PID_DIR}
fi

if [ $ENV = "product" ]; then
    # 线上环境增加全异步日志
    # JAVA_OPTS=$JAVA_OPTS" -DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector -DAsyncLogger.WaitStrategy=busyspin"
    JAVA_OPTS=$JAVA_OPTS" -DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector"
fi





do_dump(){

    PIDS=`ps -ef | grep java | grep "$DEPLOY_DIR" |awk '{print $2}'`
    if [ -z "$PIDS" ]; then
        echo "ERROR: The $SERVER_NAME does not started!"
        exit 1
    fi

    DUMP_DIR=${LOGS_DIR}/dump
    if [ ! -d ${DUMP_DIR} ]; then
        mkdir -p ${DUMP_DIR}
    fi
    DUMP_DATE=`date +%Y%m%d%H%M%S`
    DATE_DIR=${DUMP_DIR}/${DUMP_DATE}
    if [ ! -d ${DATE_DIR} ]; then
        mkdir -p ${DATE_DIR}
    fi

    echo -e "Dumping the $SERVER_NAME ...\c"
    for PID in $PIDS ; do
        jstack $PID > ${DATE_DIR}/jstack-$PID.dump 2>&1
        echo -e ".\c"
        jinfo $PID > ${DATE_DIR}/jinfo-$PID.dump 2>&1
        echo -e ".\c"
        jstat -gcutil $PID > ${DATE_DIR}/jstat-gcutil-$PID.dump 2>&1
        echo -e ".\c"
        jstat -gccapacity $PID > ${DATE_DIR}/jstat-gccapacity-$PID.dump 2>&1
        echo -e ".\c"
        jmap $PID > ${DATE_DIR}/jmap-$PID.dump 2>&1
        echo -e ".\c"
        jmap -heap $PID > ${DATE_DIR}/jmap-heap-$PID.dump 2>&1
        echo -e ".\c"
        jmap -histo $PID > ${DATE_DIR}/jmap-histo-$PID.dump 2>&1
        echo -e ".\c"
        if [ -r /usr/sbin/lsof ]; then
        /usr/sbin/lsof -p $PID > ${DATE_DIR}/lsof-$PID.dump
        echo -e ".\c"
        fi
    done

    if [ -r /bin/netstat ]; then
    /bin/netstat -an > /netstat.dump 2>&1
    echo -e ".\c"
    fi
    if [ -r /usr/bin/iostat ]; then
    /usr/bin/iostat > ${DATE_DIR}/iostat.dump 2>&1
    echo -e ".\c"
    fi
    if [ -r /usr/bin/mpstat ]; then
    /usr/bin/mpstat > ${DATE_DIR}/mpstat.dump 2>&1
    echo -e ".\c"
    fi
    if [ -r /usr/bin/vmstat ]; then
    /usr/bin/vmstat > ${DATE_DIR}/vmstat.dump 2>&1
    echo -e ".\c"
    fi
    if [ -r /usr/bin/free ]; then
    /usr/bin/free -t > ${DATE_DIR}/free.dump 2>&1
    echo -e ".\c"
    fi
    if [ -r /usr/bin/sar ]; then
    /usr/bin/sar > ${DATE_DIR}/sar.dump 2>&1
    echo -e ".\c"
    fi
    if [ -r /usr/bin/uptime ]; then
    /usr/bin/uptime > ${DATE_DIR}/uptime.dump 2>&1
    echo -e ".\c"
    fi

    echo "OK!"
    echo "DUMP: ${DATE_DIR}"

}


case "$1" in
    start)
        ./${server_name}.jar start
    ;;
    stop)
        ./${server_name}.jar stop
    ;;
    status)
        ./${server_name}.jar status
    ;;
    restart)
        ./${server_name}.jar restart
    ;;
    dump)
        do_dump
    ;;
    *)
        echo "Usage ${0} <start|stop|status|restart|dump>"
        exit 1
    ;;
esac
