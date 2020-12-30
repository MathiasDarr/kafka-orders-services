#!/bin/bash

if [[ -z $2 ]]
then
  stackname=microservices-stack
else
  stackname=$2
fi

aws cloudformation deploy \
      --template-file ecs_template.yaml \
      --stack-name ${stackname} \
      --capabilities CAPABILITY_NAMED_IAM
