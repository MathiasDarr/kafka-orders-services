"""
This script reads the csv files written by the script scrape_rei_products.py and inserts the products into the Products
dynamoDB table.
"""
# !/usr/bin/env python3

import csv
import os
import boto3
from boto3.dynamodb.types import Decimal
from time import sleep


def insert_product(product, table):
    return table.put_item(
        Item={
            'vendor': product['vendor'],
            'productName': product['name'],
            'colors': product['colors'],
            'price': Decimal(product['price']),
            'category': product['category'],
            'image_url': product['image_url']
        }
    )


def create_products_table():
    try:
        resp = dynamodb.create_table(
            AttributeDefinitions=[
                {
                    "AttributeName": "vendor",
                    "AttributeType": "S"
                },
                {
                    "AttributeName": "productName",
                    "AttributeType": "S"
                },
                {
                    "AttributeName": "category",
                    "AttributeType": "S"
                },
            ],
            TableName="Products",
            KeySchema=[
                {
                    "AttributeName": "vendor",
                    "KeyType": "HASH"
                },
                {
                    "AttributeName": "productName",
                    "KeyType": "RANGE"
                }
            ],
            ProvisionedThroughput={
                "ReadCapacityUnits": 1,
                "WriteCapacityUnits": 1
            },
            GlobalSecondaryIndexes=[
                {
                    'IndexName': 'categoryGSI',
                    'KeySchema': [
                        {
                            'AttributeName': 'category',
                            'KeyType': 'HASH',
                        },
                    ],
                    'Projection': {
                        'ProjectionType': 'ALL',
                    },
                    'ProvisionedThroughput': {
                        'ReadCapacityUnits': 1,
                        'WriteCapacityUnits': 1,
                    }
                },
            ],

        )
        return resp
    except Exception as e:
        print(e)


def create_categories_table():
    try:
        dynamodb.create_table(
            AttributeDefinitions=[
                {
                    "AttributeName": "category",
                    "AttributeType": "S"
                },
                {
                    "AttributeName": "subcategory",
                    "AttributeType": "S"
                },
            ],
            TableName='Categories',
            KeySchema=[
                {
                    "AttributeName": "category",
                    "KeyType": "HASH"
                },
                {
                    "AttributeName": "subcategory",
                    "KeyType": "RANGE"
                }
            ],
            ProvisionedThroughput={
                "ReadCapacityUnits": 1,
                "WriteCapacityUnits": 1
            },
        )

    except Exception as e:
        print(e)


def insert_order(order, table):
    return table.put_item(
        Item={
            'orderID': order['orderID'],
            'customerID': order['customerID'],
            'vendors': order['vendors'],
            'products': order['products'],
            'order_status': order['order_status']
        }
    )


def create_orders_table():
    try:
        resp = dynamodb.create_table(

            TableName="Orders",

            AttributeDefinitions=[
                {
                    "AttributeName": "customerID",
                    "AttributeType": "S"
                },
                {
                    "AttributeName": "orderID",
                    "AttributeType": "S"
                },

            ],

            KeySchema=[
                {
                    "AttributeName": "customerID",
                    "KeyType": "HASH"
                },
                {
                    "AttributeName": "orderID",
                    "KeyType": "RANGE"
                }
            ],
            ProvisionedThroughput={
                "ReadCapacityUnits": 1,
                "WriteCapacityUnits": 1
            },
        )
        return resp
    except Exception as e:
        print(e)


def populate_orders():
    table = dynamodb.Table('Orders')

    with open('data/orders.csv', newline='') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            insert_order(row, table)


def populate_products():
    # sleep(5)
    table = dynamodb.Table('Products')

    CSV_DIRECTORY = 'data/products'
    csv_files = []
    for file in os.listdir(CSV_DIRECTORY):
        file_path = '{}/{}'.format(CSV_DIRECTORY, file)
        if file_path.split('.')[-1] == 'csv':
            csv_files.append(file_path)

    for file in csv_files:
        with open(file, newline='') as csvfile:
            reader = csv.DictReader(csvfile)
            for row in reader:
                insert_product(row, table)


def populate_categories():
    table = dynamodb.Table('Categories')

    categories = [('mens-boots', 'boots', 'M'), ('day-packs', 'backpacks', ''), ('backpacking-packs', 'backpacks', ''),
                  ('womens-running-jackets', 'jackets', 'W'), ('womens-rain-jackets', 'jackets', 'w'),
                  ('womens-insulated-jackets', 'jackets', 'W'),
                  ('womens-fleece-and-soft-shell-jackets', 'jackets', 'W', 'womens-casual-jackets', 'jackets', 'W'),
                  ('womens-boots', 'boots', 'W'), ('mens-winter-boots', 'boots', 'M'),
                  ('mens-snow-jackets', 'jackets', 'M'), ('mens-running-jackets', 'jackets', 'M'),
                  ('mens-rain-jackets', 'jackets', 'M'), ('mens-insulated-jackets', 'jackets', 'M'),
                  ('mens-fleece-and-soft-shell-jackets', 'jackets', 'M'),
                  ('mens-casual-jackets', 'jackets', 'M')
                  ]

    for category in categories:
        table.put_item(
            Item={
                'category': category[0],
                'subcategory': category[1],
                'gender': category[2]
            }
        )


if __name__ == '__main__':
    # dynamodb = boto3.resource('dynamodb',endpoint_url="http://localhost:4566")
    dynamodb = boto3.resource('dynamodb', endpoint_url="http://localhost:8000")

    create_products_table()
    create_categories_table()

    populate_products()
    populate_categories()

    create_orders_table()
    populate_orders()
