terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 3.0"
    }
  }
}

# Configure the AWS Provider
provider "aws" {
  region = "eu-west-1"
}

resource "aws_iam_role" "iam_for_lambda" {
  name = "iam_for_java_lambda"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
}

resource "aws_iam_role_policy_attachment" "test-attach" {
  role       = aws_iam_role.iam_for_lambda.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}
//
//data "archive_file" "lambda_zip" {
//  type        = "zip"
//  source_dir = "${path.module}/../build/distributions/iKovsky.zip"
//  output_path = "theBachEnd.zip"
//}

resource "aws_lambda_function" "java_ikovsky" {
  filename      =  "${path.module}/../build/distributions/iKovsky.zip"
  function_name = "java_ikovsky"
  role          = aws_iam_role.iam_for_lambda.arn
  handler       = "ikovsky.Handler"
  timeout       = 15

//  source_code_hash = "${data.archive_file.lambda_zip.output_base64sha256}"
  source_code_hash = filebase64sha256("${path.module}/../build/distributions/iKovsky.zip")

  runtime = "java8"
}

