"""
This script creates & populates the users & the trips cassandra tables
"""
# !/usr/bin/env python3

import os
import csv
from utilities.cassandra_utilities import createCassandraConnection, createKeySpace
import numpy as np
from numpy.random import choice
import uuid

def create_products_table():
    create_products_table = """CREATE TABLE IF NOT EXISTS products(
        productid text,
        vendor text,
        name text, 
        image_url text,
        price float,
        cost float,
        category text,
        popularity int,
        inventory bigint,
        PRIMARY KEY(productid)) ;
    """
    dbsession.execute(create_products_table)


def populate_productsid_table(csv_file):
    insert_trip_data_point = """INSERT INTO products(productid, vendor, name, price, cost, category, popularity, inventory) VALUES(%s,%s,%s,%s,%s,%s, %s, %s);"""

    with open(csv_file, newline='') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            price = float(row['price'])
            percent_profit_draw = choice([.05, .1, .2, .25, .3], 1, p=[.2, .3, .3, .1, .1])
            popularity = int(np.random.normal(50,15))
            if popularity < 15:
                popularity = 15
            mean_cost = price * (1-percent_profit_draw[0])
            cost = mean_cost + np.random.normal(5)
            dbsession.execute(insert_trip_data_point, [str(uuid.uuid4()),row['vendor'], row['name'], float(row['price']), cost,row['category'],popularity, 3])


def populate_productsid():
    CSV_DIRECTORY = 'data/products'
    csv_files = []
    for file in os.listdir(CSV_DIRECTORY):
        file_path = '{}/{}'.format(CSV_DIRECTORY, file)
        if file_path.split('.')[-1] == 'csv':
            csv_files.append(file_path)
    for file in csv_files:
        populate_productsid_table(file)






#
#
#
# def create_prooducts_table():
#     create_products_table = """CREATE TABLE IF NOT EXISTS products(
#         vendor text,
#         name text,
#         image_url text,
#         price float,
#         cost float,
#         category text,
#         popularity int,
#         inventory bigint,
#         PRIMARY KEY((vendor, name)));
#     """
#     dbsession.execute(create_products_table)
#
#
# def populate_products_table(csv_file):
#     insert_trip_data_point = """INSERT INTO products(vendor, name, image_url, price, cost,category,popularity, inventory) VALUES(%s,%s,%s,%s,%s,%s, %s, %s);"""
#
#     with open(csv_file, newline='') as csvfile:
#         reader = csv.DictReader(csvfile)
#         for row in reader:
#             price = float(row['price'])
#             percent_profit_draw = choice([.05, .1, .2, .25, .3], 1, p=[.2, .3, .3, .1, .1])
#             popularity = int(np.random.normal(50,15))
#             if popularity < 15:
#                 popularity = 15
#             mean_cost = price * (1-percent_profit_draw[0])
#             cost = mean_cost + np.random.normal(5)
#             dbsession.execute(insert_trip_data_point, [row['vendor'], row['name'], row['image_url'], float(row['price']), cost,row['category'],popularity, 3])
#
#
# def populate_products():
#     CSV_DIRECTORY = 'data/products'
#     csv_files = []
#     for file in os.listdir(CSV_DIRECTORY):
#         file_path = '{}/{}'.format(CSV_DIRECTORY, file)
#         if file_path.split('.')[-1] == 'csv':
#             csv_files.append(file_path)
#     for file in csv_files:
#         populate_products_table(file)


def create_orders_table():
    create_orders_table = """CREATE TABLE IF NOT EXISTS orders(
        orderid text,
        customerid text,
        quantities list<bigint>,
        products list<text>,
        vendors list<text>,
        order_status text,
        PRIMARY KEY((orderID, customerID)));
        """
    dbsession.execute(create_orders_table)


def populate_orders():
    CSV_FILE = 'data/orders.csv'
    insert_trip_data_point = """INSERT INTO orders(orderID, customerID, quantities, products, vendors, order_status) VALUES(%s,%s,%s, %s, %s, %s);"""
    with open(CSV_FILE, newline='') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            quantities = [int(q) for q in row['quantities'][1:-1].split(',')]
            products = [q for q in row['products'][1:-1].split(',')]
            vendors = [q for q in row['vendors'][1:-1].split(',')]

            dbsession.execute(insert_trip_data_point, [row['orderID'], row['customerID'], quantities, products, vendors, row['order_status']]) #, float(row['total_price']))



def create_customers_table():
    create_customers_table = """CREATE TABLE IF NOT EXISTS customers(
        customerid text,
        first_name text,
        last_name text,
        email text,
        password text,
        purchases_per_month int,
        average_purchase_amount int,
        city text,
        PRIMARY KEY(customerid));
        """
    dbsession.execute(create_customers_table)


def populate_customers():
    CSV_FILE = 'data/customers/customers.csv'
    insert_customer = """INSERT INTO customers(customerid, first_name, last_name, email, password, purchases_per_month, average_purchase_amount, city) VALUES(%s,%s,%s, %s, %s, %s, %s, %s);"""
    with open(CSV_FILE, newline='') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:

            dbsession.execute(insert_customer, [row['customerid'], row['first_name'], row['last_name'], row['email'], row['password'], int(row['purchases_per_month']),int(row['average_purchase_amount']), row['city']])


if __name__ == '__main__':
    dbsession = createCassandraConnection()
    createKeySpace("ks1", dbsession)
    try:
        dbsession.set_keyspace('ks1')
    except Exception as e:
        print(e)
    create_orders_table()
    create_products_table()
    create_customers_table()

    populate_productsid()

    # populate_products()
    # populate_orders()
    # populate_customers()
    print("THE CASSANDRA DATABASE HAS BEEN SEEDED")