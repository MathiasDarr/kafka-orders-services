"""
Upload the images directory to s3
"""
import boto3
import os

s3_client = boto3.client('s3')
BUCKET = "dakobed-outdoor-recreation"

for file in os.listdir('products/mens-casual-jackets'):
    file_path = 'products/mens-casual-jackets/{}'.format(file)
    response = s3_client.upload_file(file_path, BUCKET, file_path)
