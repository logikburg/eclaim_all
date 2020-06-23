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
# Jason YU		        08 Feb 2018     Initial version 
# **********************************************************************************

#-----------------------------------
# Parameters
#-----------------------------------
SCRIPT_DIR=`dirname $0`
#-----------------------------------
# Basic
#-----------------------------------
. ~/mprs.env
SECURITY_PROP=`pwd`/../config/security.properties
MPRS_ENV=`grep ss.app.environment < $SECURITY_PROP | cut -d= -f2 | sed -e 's/\r//g'`
JOB_LIST=(UPDATE_END_DATE SUPP_PROMOTION_POST SYNC_POST_MASTER)
LOG_DIR=$MPRS_HOME/log
EMAIL_PROP=`pwd`/../config/security.properties
JOB_NAME=
JOB_START_TIME=
JOB_END_TIME=
LOG_FILE=mprs_batch_jobs.log

#-----------------------------------
# Checking
#-----------------------------------
CNT_JOB=0
ERR_CNT=0
ERROR_JOB_LIST=${LOG_DIR}/ErrJobList.txt

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
	WriteLog "End running job for $JOB_LIST"
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
	SMTP_SERVER=`grep mail.host < $EMAIL_PROP | cut -d= -f2 | sed -e 's/\r//g'`
	EMAIL_FROM=`grep mail.sender < $EMAIL_PROP | cut -d= -f2 | sed -e 's/\r//g'`
	EMAIL_TO="mprssupport@ho.ha.org.hk"
	#EMAIL_TO="ykm698@ha.org.hk,msl332@ha.org.hk"
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
		mailSubject="MPRS System Message [$MPRS_ENV] - MPRS Batch Jobs were executed successfully"
	else
		mailSubject="MPRS System Message [$MPRS_ENV] - MPRS Batch Jobs were executed with error"
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
    if [ "$WK_STATUS" == "SUCCEEDED" ]; then
      echo $2
    else
      echo $2
      echo ""
      echo "Here is the list of batch job(s) failed to run:"
      echo ""

      while read line
      do
          echo "$line"
      done < $ERROR_JOB_LIST

      echo ""
    fi

    echo ""
    echo "Total number of jobs run successfully: $CNT_JOB"
    echo ""
    echo ""
    echo ""
}

function StartBatchJob
{
	echo "### Start Invoking the batch job..."
	#echo $SCRIPT_DIR
    #echo '' > $ERROR_JOB_LIST
    cd $SCRIPT_DIR
    for job in ${JOB_LIST[*]}
    do
		echo $job
		$SCRIPT_DIR/run_batch.sh $job
		
		ERROR_CODE=$?
	   
		if [ ${ERROR_CODE} != 0 ]; then
			echo $job >> $ERROR_JOB_LIST
			ERR_CNT=`expr $ERR_CNT + 1`
			WriteLog "Error! $job run failed"
		else
			CNT_JOB=`expr $CNT_JOB + 1`
			WriteLog "$job run successfully"
		fi

    done
	
	if [ $ERR_CNT -gt 0 ]; then
		return 1
    else
        return 0
    fi

}
############################################################################
## Main Program
############################################################################

JOB_START_TIME=`date +"%Y-%m-%d %H:%M:%S"`
WriteLog "--------------------------------------------------------"
WriteLog "Start running job for $JOB_LIST"
WriteLog "Start time: $JOB_START_TIME"
WriteLog "--------------------------------------------------------"

WriteLog "Environment: $MPRS_ENV"
echo "Environment: $MPRS_ENV"
WriteLog "Log folder: $LOG_DIR"
#############################################################
## Step 1 - Start Batch Job
#############################################################
StartBatchJob

RC_BC=$?

PrintEnd
ArchiveLog

#############################################################
## Step 2 - Send notification email
#############################################################
if [ $RC_BC == 1 ]; then
    EmailToSupport "FAILED" "MPRS Batch jobs were completed with error. Please take necessary actions."
    exit 1;
else
    EmailToSupport "SUCCEEDED" "MPRS Batch jobs were completed successfully. No action is required."
    exit 0;
fi

