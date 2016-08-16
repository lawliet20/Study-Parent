#!/bin/bash

declare BASE_PATH="/mnt/apps/mkproject"
declare LOG_FOLDER="$BASE_PATH/log"`date +/%Y/%m`
declare LOG_FILE=$LOG_FOLDER"/auto_deploy.log."`date +%d`

#project code path, the code check from svn
declare MANAGE_PROJECT_PATH="$BASE_PATH/manage/pro-parent"
declare EDOU_PROJECT_PATH="$BASE_PATH/edou/ed-parent"

declare PACKAGEEVN="test"

#project package names
declare PACK_MANAGE_NAME="pro-manage.war"
declare PACK_WEIXIN_NAME="ed-weixin.war"
declare PACK_APPSERVER_NAME="ed-appserver.war"
declare PACK_WPOSP_NAME="ed-wposp.war"
declare PACK_EDCORE_ZIP_NAME="ed-core-service-0.0.1-SNAPSHOT-bin.zip"
declare PACK_EDCORE_NAME="ed-core-service-0.0.1-SNAPSHOT.jar"
declare PACK_EDQUERY_ZIP_NAME="ed-query-service-0.0.1-SNAPSHOT-bin.zip"
declare PACK_EDQUERY_NAME="ed-query-service-0.0.1-SNAPSHOT.jar"

#project package path
declare PACK_MANAGE_PATH="$MANAGE_PROJECT_PATH/pro-manage/target"
declare PACK_WEIXIN_PATH="$EDOU_PROJECT_PATH/ed-weixin/target"
declare PACK_APPSERVER_PATH="$EDOU_PROJECT_PATH/ed-appserver/target"
declare PACK_WPOSP_PATH="$EDOU_PROJECT_PATH/ed-wposp/target"
declare PACK_EDCORE_PATH="$EDOU_PROJECT_PATH/ed-core-service/target"
declare PACK_EDQUERY_PATH="$EDOU_PROJECT_PATH/ed-query-service/target"

#tomcat install path
declare MANAGE_TOMCAT_HOME="/home/tomcat/tomcat"
declare WEIXIN_TOMCAT_HOME="/home/tomcat/weixin/apache-tomcat-weixin"
declare APPSERVER_TOMCAT_HOME="/home/tomcat/appserver_8088_tomcat"
declare WPOSP_TOMCAT_HOME="/home/tomcat/tomcat"

#dubbo app path
declare EDCORE_HOME="/home/edcore"
declare EDQUERY_HOME="/home/edquery"

#deploy project path
declare MANAGE_WEB_HOME="/home/tomcat/service/webapps/mickong"
declare WEIXIN_WEB_HOME="/home/tomcat/weixin/apache-tomcat-weixin/webapps/ed-weixin"
declare APPSERVER_WEB_HOME="/home/tomcat/service/webapps/appserver"
declare WPOSP_WEB_HOME="/home/tomcat/tomcat/webapps/ed-wposp"

#dubbo application path
declare EDCORE_HOME="/home/edcore"
declare EDQUERY_HOME="/home/edquery"


function checklog() {
	if [ ! -d $LOG_FOLDER ]
	        then `mkdir -p $LOG_FOLDER`
	fi
}

#update code from svn
#maven clean project
#maven package project
function archive() {
	`checklog`
	cd $1
	if [ $? -ne 0 ]
		then
			echo `date +%Y/%m/%d_%T.%N`" --Failed to cd project home path">>$LOG_FILE
			echo "failed"
			return
	fi
	
	`svn update >>$LOG_FILE 2>>$LOG_FILE`
	if [ $? -ne 0 ]
		then
			echo `date +%Y/%m/%d_%T.%N`" --Failed to svnupdate">>$LOG_FILE
			echo "failed"
			return
	fi
	
	`mvn clean >>$LOG_FILE 2>>$LOG_FILE`
	if [ $? -ne 0 ]
		then
			echo `date +%Y/%m/%d_%T.%N`" --Failed to mvn clean">>$LOG_FILE
			echo "failed"
			return
	fi
	
	`mvn package -Dmaven.test.skip=true -Dmaven.resources.overwrite=true -P$PACKAGEEVN >>$LOG_FILE 2>>$LOG_FILE`
	if [ $? -ne 0 ]
		then
			echo `date +%Y/%m/%d_%T.%N`" --Failed to mvn package">>$LOG_FILE
			echo "failed"
			return
	else
		echo `date +%Y/%m/%d_%T.%N`" --success to mvn package">>$LOG_FILE
		echo "succeed"
	fi
	
}

# stop tomcat
# deploy web project to tomcat
# start tomcat
function deploy_web() {		
		ppid=`ps -ef|grep "Dcatalina\.home=$2.*start" | awk '{ print $2 }' | grep '^[0-9]\+$'`;
		if [ $ppid ]
			then 
				kill -9 $ppid;
				echo "$2 stop successful!" >>$LOG_FILE;
			else
				echo "$2 stop failed! no process." >>$LOG_FILE;
		fi;
		`cp $4/$1 $3 >>$LOG_FILE 2>>$LOG_FILE`
		if [ $? -ne 0 ]
		then
				echo "failed"
				return
		fi
		cd $3
		`jar -xf $3/$1 >>$LOG_FILE 2>>$LOG_FILE`
		if [ $? -ne 0 ]
		then
				echo "jar -xf failed!" >>$LOG_FILE
				echo "failed"
				return
		else
				echo "jar -xf successful!" >>$LOG_FILE
		fi
		`$2/bin/startup.sh >>$LOG_FILE 2>>$LOG_FILE`
		if [ $? -ne 0 ]
		then
				echo "$2 start failed!" >>$LOG_FILE
				echo "failed"
				return
		else
				echo "$2 start successful!" >>$LOG_FILE
				echo "succeed"
		fi
}

# stop
# deploy dubbo
# start
function deploy_dubbo() {		
		ppid=`jps -l | grep "$1" | awk '{print $1}'`;
		if [ $ppid ]
			then 
				kill -9 $ppid;
				echo "$2 stop successful!" >>$LOG_FILE;
			else
				echo "$2 stop failed! no process." >>$LOG_FILE;
		fi;
		`cp $3/$1 $2 >>$LOG_FILE 2>>$LOG_FILE`
		if [ $? -ne 0 ]
		then
				echo "failed"
				return
		fi
		cd $2
		`jar -xf $2/$1 >>$LOG_FILE 2>>$LOG_FILE`
		if [ $? -ne 0 ]
		then
				echo "jar -xf failed!" >>$LOG_FILE
				echo "failed"
				return
		else
				echo "jar -xf successful!" >>$LOG_FILE
		fi
		`cp $3/$4 $2 >>$LOG_FILE 2>>$LOG_FILE`
		if [ $? -ne 0 ]
		then
				echo "failed"
				return
		fi
		`unzip -o $2/$4 >>$LOG_FILE 2>>$LOG_FILE`
		if [ $? -ne 0 ]
		then
				echo "unzip -o failed!" >>$LOG_FILE
				echo "failed"
				return
		else
				echo "unzip -o successful!" >>$LOG_FILE
		fi
		`$2/bin/run.sh start >>$LOG_FILE 2>>$LOG_FILE`
		if [ $? -ne 0 ]
		then
				echo "$2 start failed!" >>$LOG_FILE
				echo "failed"
				return
		else
				echo "$2 start successful!" >>$LOG_FILE
				echo "succeed"
		fi
}

# pro-manage
function manage() {
		local p_name="pro-manage"
		local result=""
		echo "==============$p_name archive begin...=================="
		result=`archive $MANAGE_PROJECT_PATH`
		if [ "$result"x != "succeed"x ]
			then
				echo `date +%Y/%m/%d_%T.%N`" --Failed to archive $p_name">>$LOG_FILE
				echo "==============$p_name archive failed!=================="
				return;
			else
				echo "==============$p_name archive successful!=================="
		fi
		
		echo "==============$p_name deploy_web begin...=================="
		result=`deploy_web $PACK_MANAGE_NAME $MANAGE_TOMCAT_HOME $MANAGE_WEB_HOME $PACK_MANAGE_PATH`
		if [ "$result"x != "succeed"x ]
		then
				echo "manage deploy failed!" >>$LOG_FILE
				echo "==============$p_name deploy_web failed!=================="
				return
		else
				echo "manage deploy successful!" >>$LOG_FILE
				echo "==============$p_name deploy_web successful!=================="
		fi
}

function edcore() {
		local log_name="ed-core"
		chmod 666 $LOG_FILE
		echo "==============$p_name deploy $log_name begin...=================="
		result=`su - edcore -c "$EDCORE_HOME/bin/deploy_dubbo.sh $PACK_EDCORE_NAME $EDCORE_HOME $PACK_EDCORE_PATH $PACK_EDCORE_ZIP_NAME $LOG_FILE"`
		if [ "$result"x != "succeed"x ]
		then
				echo "$log_name deploy failed!" >>$LOG_FILE
				echo "==============deploy $log_name failed!=================="
				return
		else
				echo "$log_name deploy successful!" >>$LOG_FILE
				echo "============== deploy $log_name successful!=================="
		fi
}

function edquery() {
		local log_name="ed-query"
		chmod 666 $LOG_FILE
		echo "==============$p_name deploy $log_name begin...=================="
		result=`su - edquery -c "$EDQUERY_HOME/bin/deploy_dubbo.sh $PACK_EDQUERY_NAME $EDQUERY_HOME $PACK_EDQUERY_PATH $PACK_EDQUERY_ZIP_NAME $LOG_FILE"`
		if [ "$result"x != "succeed"x ]
		then
				echo "$log_name deploy failed!" >>$LOG_FILE
				echo "==============deploy $log_name failed!=================="
				return
		else
				echo "$log_name deploy successful!" >>$LOG_FILE
				echo "============== deploy $log_name successful!=================="
		fi
}

function weixin() {
		local log_name="ed-weixin"
		echo "==============$p_name deploy $log_name begin...=================="
		result=`deploy_web $PACK_WEIXIN_NAME $WEIXIN_TOMCAT_HOME $WEIXIN_WEB_HOME $PACK_WEIXIN_PATH`
		if [ "$result"x != "succeed"x ]
		then
				echo "$log_name deploy failed!" >>$LOG_FILE
				echo "==============deploy $log_name failed!=================="
				return
		else
				echo "$log_name deploy successful!" >>$LOG_FILE
				echo "==============deploy $log_name successful!=================="
		fi
}

function appserver() {
		local log_name="ed-appserver"
		echo "==============$p_name deploy $log_name begin...=================="
		result=`deploy_web $PACK_APPSERVER_NAME $APPSERVER_TOMCAT_HOME $APPSERVER_WEB_HOME $PACK_APPSERVER_PATH`
		if [ "$result"x != "succeed"x ]
		then
				echo "$log_name deploy failed!" >>$LOG_FILE
				echo "==============deploy $log_name failed!=================="
				return
		else
				echo "$log_name deploy successful!" >>$LOG_FILE
				echo "============== deploy $log_name successful!=================="
		fi
}

function wposp() {
		local log_name="ed-wposp"
		echo "==============$p_name deploy $log_name begin...=================="
		result=`deploy_web $PACK_WPOSP_NAME $WPOSP_TOMCAT_HOME $WPOSP_WEB_HOME $PACK_WPOSP_PATH`
		if [ "$result"x != "succeed"x ]
		then
				echo "$log_name deploy failed!" >>$LOG_FILE
				echo "==============deploy $log_name failed!=================="
				return
		else
				echo "$log_name deploy successful!" >>$LOG_FILE
				echo "============== deploy $log_name successful!=================="
		fi
}

# edou
function edou() {
		local p_name="edou"
		local result=""
		local log_name=""
		echo "==============$p_name archive begin...=================="
		result=`archive $EDOU_PROJECT_PATH`
		if [ "$result"x != "succeed"x ]
			then
				echo `date +%Y/%m/%d_%T.%N`" --Failed to archive $p_name">>$LOG_FILE
				echo "==============$p_name archive failed!=================="
				return;
			else
				echo "==============$p_name archive successful!=================="
		fi
	
		edcore
		edquery	
		weixin
		appserver
		wposp
		
		echo "============== deploy $p_name all successful!=================="
}

case "$1" in
   'edou')
     edou
     ;;
   'manage')
     manage
     ;;
   'p_e')
     archive $EDOU_PROJECT_PATH
     ;;
   'P_m')
     archive $MANAGE_PROJECT_PATH
     ;;
   'edcore')
     edcore
     ;;
   'edquery')
     edquery
     ;;
   'weixin')
     weixin
     ;;
   'app')
     appserver
     ;;
   'wposp')
     wposp
     ;;
  *)
     echo "Usage: $0 {p_e|p_m|edou|manage|edcore|edquery|weixin|app|wposp}"
     exit 1
  ;;
esac
