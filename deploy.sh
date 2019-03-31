aws cloudformation package --template-file sam.yaml --output-template-file output-sam.yaml --s3-bucket order-aggregator

aws cloudformation deploy --template-file output-sam.yaml --stack-name order-aggregator --capabilities CAPABILITY_IAM

aws cloudformation describe-stacks --stack-name order-aggregator
