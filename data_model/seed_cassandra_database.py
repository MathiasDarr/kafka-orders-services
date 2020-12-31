"""
This script creates & populates the users & the trips cassandra tables
"""
# !/usr/bin/env python3

import os
import csv
from utilities.cassandra_utilities import createCassandraConnection, createKeySpace


def populate_products_table(csv_file):
    insert_trip_data_point = """INSERT INTO products(vendor, name, image_url, price, category, inventory) VALUES(%s,%s,%s,%s, %s, %s);"""

    with open(csv_file, newline='') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            dbsession.execute(insert_trip_data_point, [row['vendor'], row['name'], row['image_url'], float(row['price']),row['category'], 3])


def populate_products():
    create_trip_data_point_table = """CREATE TABLE IF NOT EXISTS products(
        vendor text,
        name text, 
        image_url text,
        price float,
        category text,
        inventory bigint,
        PRIMARY KEY((vendor, name)));
    """
    dbsession.execute(create_trip_data_point_table)

    CSV_DIRECTORY = 'data/products'
    csv_files = []
    for file in os.listdir(CSV_DIRECTORY):
        file_path = '{}/{}'.format(CSV_DIRECTORY, file)
        if file_path.split('.')[-1] == 'csv':
            csv_files.append(file_path)
    for file in csv_files:
        populate_products_table(file)


def populate_orders():

    create_orders_table = """CREATE TABLE IF NOT EXISTS orders(
        orderID text,
        customerID text,
        quantities list<int>,
        products list<text>,
        vendors list<text>,
        order_status text,
        PRIMARY KEY((orderID, customerID)));
        """

    dbsession.execute(create_orders_table)

    CSV_FILE = 'data/orders.csv'
    insert_trip_data_point = """INSERT INTO orders(orderID, customerID, quantities, products, vendors, order_status) VALUES(%s,%s,%s, %s, %s, %s);"""
    with open(CSV_FILE, newline='') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            quantities = [int(q) for q in row['quantities'][1:-1].split(',')]
            products = [q for q in row['products'][1:-1].split(',')]
            vendors = [q for q in row['vendors'][1:-1].split(',')]

            dbsession.execute(insert_trip_data_point, [row['orderID'], row['customerID'], quantities, products, vendors, row['order_status']]) #, float(row['total_price']))


if __name__ == '__main__':
    dbsession = createCassandraConnection()
    createKeySpace("ks1", dbsession)
    try:
        dbsession.set_keyspace('ks1')
    except Exception as e:
        print(e)
    populate_products()
    populate_orders()
    print("THE CASSANDRA DATABASE HAS BEEN SEEDED")