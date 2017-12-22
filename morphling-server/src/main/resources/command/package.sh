#!/bin/bash

START_TIME=`date +%s`
WORKSPACE=${data_dir}/workspace/

if [ -z ${project_name} ]; then
    echo 'project name is empty... '
    exit 1
fi

PROJECT_HOME=${WORKSPACE}${project_name}
PACKAGE_HOME=${data_dir}/package/${project_name}

if [ ! -d ${PACKAGE_HOME} ]; then
    mkdir -p ${PACKAGE_HOME}
fi

if [ -z ${git_url} ]; then
    echo 'git url is empty... '
    exit 1
fi
if [ -z ${git_branch} ]; then
    echo 'git branch is empty... '
    exit 1
fi


if [ ! -d ${PROJECT_HOME} ]; then
    echo ">>> init project..."
    mkdir -p ${PROJECT_HOME}
    cd ${PROJECT_HOME}
    git init ${PROJECT_HOME}
    git fetch --tags --progress ${git_url} +refs/heads/*:refs/remotes/origin/*
    git config remote.origin.url ${git_url} # timeout=10
    git config --add remote.origin.fetch +refs/heads/*:refs/remotes/origin/* # timeout=10
    git config core.sparsecheckout # timeout=10
fi

cd ${PROJECT_HOME}

git fetch --tags --progress ${git_url} +refs/heads/*:refs/remotes/origin/*
git checkout -f ${git_branch}
git pull origin ${git_branch}
git reset --hard HEAD

mvn clean install -U -Dmaven.test.skip=true -Dmaven.compile.fork=true -pl ${module} -am -P${profile}

cd ${module}/target

if [ ! -f ${project_name}.tar.gz ];  then
    echo "error:no such file ${project_name}.tar.gz, package fail"
    exit 1
fi

echo "package success"
echo "==============================================================="
echo "move package to ${PACKAGE_HOME}/${project_name}_${git_branch}.tar.gz"


\cp -pr -f ${project_name}.tar.gz ${PACKAGE_HOME}/${project_name}_${git_branch}.tar.gz
# \cp -pr -f ${project_name}.tar.gz ${PACKAGE_HOME}/${project_name}.tar.gz


END_TIME=`date +%s`

echo "exec time $((END_TIME-START_TIME)) s"
exit 0