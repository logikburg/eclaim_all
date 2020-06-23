#!/bin/bash

. $HOME/mprs.env

if [ $# -lt 1 ]
then
	echo "===> Usage: /run_batch.sh jobName parameter"
	exit 1
fi

jobName=$1

echo "### Start Invoking the batch job: $jobName"
curl http://$BATCH_SERVER_HOST:$BATCH_SERVER_PORT/mprs-batch/batch/executeBatchJob?jobId=$jobName
