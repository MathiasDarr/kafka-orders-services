"""
This file contains some code snippets that I made use of

"""
import os
import boto3
import csv


def rename_local_casual_jackets():
    """
    use os to rename local files
    """
    for file in os.listdir('products/mens-casual-jackets'):
        file_path = 'products/mens-casual-jackets/{}'.format(file)
        os.rename(file_path, file_path+'.png')



def rename_objects_in_s3():
    """
    Rename files stored in s3 that did not have the proper termination '.png'
    S3 does not have a copy command so this will be done by copying the object to a new location & then deleing the old object.

    Try this bucket policy

    {
        "Version": "2012-10-17",
        "Id": "Policy1606093917701",
        "Statement": [
            {
                "Sid": "Stmt1606093916507",
                "Effect": "Allow",
                "Principal": "*",
                "Action": "s3:*",
                "Resource": "arn:aws:s3:::dakobed-outdoor-recreation"
            }
        ]
    }

    I am receiving an access denied error ??
    """

    BUCKET = "dakobed-outdoor-recreation"

    s3_resource = boto3.resource("s3")
    s3_client = boto3.client("s3")

    keys = [e['Key'] for p in s3_client.get_paginator("list_objects_v2").paginate(Bucket=BUCKET)
              for e in p['Contents']]
    casual_jackets = [key for key in keys if 'products/mens-casual-jackets' in key]
    for key in casual_jackets:
        if 'mens-casual-jackets':
            s3_resource.Object(BUCKET, key + '.png').copy_from(CopySource=key)
            s3_resource.Object(BUCKET, key).delete()


"""
I am receiving an access denied error
"""

"""
Modify line in csv 
"""


def modify_image_url():
    """
    This function writes a new csv file for the one that was incorrectly saved.
    """
    bucket_url = "https://dakobed-outdoor-recreation.s3-us-west-2.amazonaws.com/"  # {}"

    products = []
    with open('products/mens-casual-jackets.csv') as inf:
        reader = csv.reader(inf.readlines())
        for i, line in enumerate(reader):
            if i == 0:
                continue
            print(line[6])
            product_dictionary = {'name': line[0], 'vendor': line[1], 'colors': line[2], 'price': line[3],
                                  'url': line[4],
                                  'category': line[5], 'image_url': '{}/{}'.format(bucket_url, line[6])}
            # 'image_url': line[6] + '.png'}
            products.append(product_dictionary)
        keys = ['name', 'vendor', 'colors', 'price', 'url', 'category', 'image_url']
        with open('products/{}.csv'.format('casual-jackets'), 'w') as output_file:
            dict_writer = csv.DictWriter(output_file, keys)
            dict_writer.writeheader()
            dict_writer.writerows(products)


modify_image_url()



