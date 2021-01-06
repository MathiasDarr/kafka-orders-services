"""
This script creates & populates the users & the trips cassandra tables
"""
# !/usr/bin/env python3

import os
import csv
from utilities.cassandra_utilities import createCassandraConnection, createKeySpace

def create_prooducts_table():
    create_products_table = """CREATE TABLE IF NOT EXISTS products(
        vendor text,
        name text, 
        image_url text,
        price float,
        category text,
        inventory bigint,
        PRIMARY KEY((vendor, name)));
    """
    dbsession.execute(create_products_table)


def populate_products_table(csv_file):
    insert_trip_data_point = """INSERT INTO products(vendor, name, image_url, price, category, inventory) VALUES(%s,%s,%s,%s, %s, %s);"""

    with open(csv_file, newline='') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            dbsession.execute(insert_trip_data_point, [row['vendor'], row['name'], row['image_url'], float(row['price']),row['category'], 3])

def populate_products():
    CSV_DIRECTORY = 'data/products'
    csv_files = []
    for file in os.listdir(CSV_DIRECTORY):
        file_path = '{}/{}'.format(CSV_DIRECTORY, file)
        if file_path.split('.')[-1] == 'csv':
            csv_files.append(file_path)
    for file in csv_files:
        populate_products_table(file)


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
    create_prooducts_table()
    create_customers_table()

    populate_products()

    # populate_products()
    # populate_orders()
    # populate_customers()
    print("THE CASSANDRA DATABASE HAS BEEN SEEDED")