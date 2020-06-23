#!/bin/ksh

# **********************************************************************************
# Script for Batch Job
#
# input parameter
# exit code:
# 	 0 : Completed and Succeeded
# 	 1 : Program error
#
#
# Created/Updated by    Date            Description [Ref.]
# ------------------    -----------     --------------------------------------------
# Lily MOK		        27 Jul 2018     Initial version 
# **********************************************************************************

#-----------------------------------
# Parameters
#-----------------------------------
SCRIPT_DIR=`dirname $0`
SCRIPT_DIR="$(cd $SCRIPT_DIR; pwd)"

#-----------------------------------
# Basic
#-----------------------------------
. ~/mprs.env
BASE_DIR="$(cd $SCRIPT_DIR/../; pwd)/"
SECURITY_PROP=$MPRS_HOME/config/security.properties
REPORT_PROP=$MPRS_HOME/config/report.properties
MPRS_ENV=`grep ss.app.environment < $SECURITY_PROP | cut -d= -f2 | sed -e 's/\r//g'`
LOG_DIR=$MPRS_HOME/log
LOG_FILE=mprs_report_batch_access.log
JOB_START_TIME=
JOB_END_TIME=

REPORT_DIR=`grep report.batch.userAccessDir < $REPORT_PROP | cut -d= -f2 | sed -e 's/\r//g'`

#-----------------------------------
# SCP
#-----------------------------------
RPT_STAGING_USER=`grep report.scp.staging.user < $REPORT_PROP | cut -d= -f2 | sed -e 's/\r//g'`
RPT_STAGING_SERVER=`grep report.scp.staging.server < $REPORT_PROP | cut -d= -f2 | sed -e 's/\r//g'`
RPT_STAGING_PORT=`grep report.scp.staging.port < $REPORT_PROP | cut -d= -f2 | sed -e 's/\r//g'`
RPT_STAGING_FOLDER=`grep report.scp.staging.folder < $REPORT_PROP | cut -d= -f2 | sed -e 's/\r//g'`
SERVER_STAGING=${RPT_STAGING_USER}@${RPT_STAGING_SERVER}:${RPT_STAGING_FOLDER}

#-----------------------------------
# Email
#-----------------------------------
SMTP_SERVER=
EMAIL_FROM=
EMAIL_TO=
EMAIL_CC=

############################################################################
## Functions
############################################################################
function WriteLog
{
	LOG_TIME=`date +"%Y-%m-%d %H:%M:%S"`
	echo "[$LOG_TIME] $1" >> $LOG_DIR/$LOG_FILE
	echo "[$LOG_TIME] $1"
}

function PrintEnd
{
	JOB_END_TIME=`date +"%Y-%m-%d %H:%M:%S"`
	WriteLog "--------------------------------------------------------"
	WriteLog "End running Report Batch Access"
	WriteLog "End time: $JOB_END_TIME"
	WriteLog "--------------------------------------------------------"
}

function ArchiveLog
{
	mv $LOG_DIR/$LOG_FILE $LOG_DIR/${LOG_FILE%.*}_`date +"%Y%m%d%H%M%S"`.log
}

function EmailToSupport
{
	WriteLog "Sending email to support"
	echo "Sending email to support"
	
	GetConfig
		
	ERROR_CODE=$?
	
	if [ ${ERROR_CODE} == 0 ]; then
		SendMessage $1 "$2"                   
		WriteLog "End sending email"
	fi
}

function GetConfig
{
	SMTP_SERVER=`grep mail.host < $SECURITY_PROP | cut -d= -f2 | sed -e 's/\r//g'`
	EMAIL_FROM=`grep mail.sender < $SECURITY_PROP | cut -d= -f2 | sed -e 's/\r//g'`
	EMAIL_TO="mprssupport@ho.ha.org.hk"
	EMAIL_CC=""

	if [ "$SMTP_SERVER" == "" ]; then
		WriteLog "ERROR! Cannot get SMTP Server"
		return 1
	fi

	if [ "$EMAIL_FROM" == "" ]; then
		WriteLog "ERROR! Cannot get email From address"
		return 1
	fi

	if [ "$EMAIL_TO" == "" ]; then
		WriteLog "ERROR! Cannot get email To address"
		return 1
	fi

	WriteLog "SMTP SERVER=$SMTP_SERVER"
	WriteLog "MAIL FROM=$EMAIL_FROM"
	WriteLog "MAIL TO=$EMAIL_TO"
  
	return 0
}

function SendMessage
{
	WK_STATUS=$1
	WK_MSG=$2
	mailFrom=$EMAIL_FROM
	mailTOs=$EMAIL_TO
	mailCCs=$EMAIL_CC
	mailSubject=''
	if [ "$WK_STATUS" == "SUCCEEDED" ]; then
		mailSubject="MPRS System Message [$MPRS_ENV] - MPRS Report Batch Access Maintenance was executed successfully"
	else
		mailSubject="MPRS System Message [$MPRS_ENV] - MPRS Report Batch Access Maintenance was executed with error"
	fi

	mailBody=`MessageBody $1 "$2"`
	(
		cat << !
$mailBody
!
	) | mail -v -c "$mailCCs" -s "$mailSubject" "$mailTOs"
}

function MessageBody
{
    WK_STATUS=$1
    WK_MSG=$2
    #if [ "$WK_STATUS" == "SUCCEEDED" ]; then
      echo $2
    #else
    #  echo $2
    #  echo ""
    #  echo "Here is the list of batch job(s) failed to run:"
    #  echo ""

    #  while read line
    #  do
    #      echo "$line"
    #  done < $ERROR_JOB_LIST

    #  echo ""
    #fi

    echo ""
    echo ""
    echo ""
    echo ""
}

function StartTransfer
{
	echo "### Start scp to Staging Server $RPT_STAGING_SERVER"

	cd $REPORT_DIR
	
	DT=`date -d"1 day ago" +"%Y%m%d"`
	echo "$DT"
	
	if [ "$RPT_STAGING_PORT" == "" ]; then
		echo "scp -rp $REPORT_DIR/* $SERVER_STAGING"
		scp -rp "$REPORT_DIR/${DT}_Report_Batch_User_Access.txt" "$SERVER_STAGING/user_access/${DT}_Report_Batch_User_Access.txt"
	else
		echo "scp -P $RPT_STAGING_PORT -rp $REPORT_DIR/* $SERVER_STAGING"
		scp -P $RPT_STAGING_PORT -rp "$REPORT_DIR/${DT}_Report_Batch_User_Access.txt" "$SERVER_STAGING/user_access/${DT}_Report_Batch_User_Access.txt"
	fi
	
	ERROR_CODE=$?
		
	if [ ${ERROR_CODE} == 1 ]; then
		echo "Error in scp to Staging Server $RPT_STAGING_SERVER"
		return 1
	else
		echo "End scp to Staging Server $RPT_STAGING_SERVER"
		return 0
	fi
}
############################################################################
## Main Program
############################################################################

JOB_START_TIME=`date +"%Y-%m-%d %H:%M:%S"`
WriteLog "--------------------------------------------------------"
WriteLog "Start running Report Batch Access"
WriteLog "Start time: $JOB_START_TIME"
WriteLog "--------------------------------------------------------"

WriteLog "Environment: $MPRS_ENV"
echo "Environment: $MPRS_ENV"
WriteLog "Log folder: $LOG_DIR"
#############################################################
## Step 1 - Start Batch Job
#############################################################
StartTransfer

RC_BC=$?

#############################################################
## Step 2 - Send notification email
#############################################################
if [ $RC_BC == 1 ]; then
    EmailToSupport "FAILED" "MPRS Report Batch Access Maintenance was completed with error. Please take necessary actions."
	PrintEnd
	ArchiveLog
    exit 1;
else
    EmailToSupport "SUCCEEDED" "MPRS Report Batch Access Maintenance was completed successfully. No action is required."
	PrintEnd
	ArchiveLog
    exit 0;
fi
