import os
import csv
from utilities.cassandra_utilities import createCassandraConnection, createKeySpace


def retreive_all_products():
    select_products = """SELECT * FROM products;"""
    products_result_set = dbsession.execute(select_products)
    products = [{'vendor':p[0], 'productname':p[1], 'category':p[2], 'price':p[5]} for p in products_result_set]

    return products




def create_stores_table():
    create_stores_table = """CREATE TABLE IF NOT EXISTS stores(
        storeid text,
        mean_neighberhood_income int, 
        mean_customers_pery_day int,
        PRIMARY KEY (storeid));
    """
    dbsession.execute(create_stores_table)



def create_store_inventory_table():
    create = """
     create table store_inventory (
      storeid text,
      vendor text,
      name text,
      inventory int,
      PRIMARY KEY((storeid), vendor, name)      
  );
    
    """
    dbsession.execute(create)


def populate_stores_inventory(stores):
    insert_trip_data_point = """INSERT INTO stores(storeid, mean_neighberhood_income, mean_customers_pery_day) VALUES(%s,%s,%s);"""

    for store in stores:
        dbsession.execute(insert_trip_data_point, [store['storeid'], store['mean_neighberhood_income'], store['mean_customers_pery_day']])



dbsession = createCassandraConnection()
createKeySpace("ks1", dbsession)
try:
    dbsession.set_keyspace('ks1')
except Exception as e:
    print(e)
# create_store_inventory_table()
create_stores_table()

cities_store_data = [
    {'storeid':'seattle1', 'mean_neighberhood_income':120000, 'mean_customers_pery_day': 5000},
    {'storeid': 'portland1', 'mean_neighberhood_income': 95000, 'mean_customers_pery_day': 4500},
    {'storeid': 'losangles1', 'mean_neighberhood_income': 65000, 'mean_customers_pery_day': 7000},
    {'storeid': 'denver1', 'mean_neighberhood_income': 100000, 'mean_customers_pery_day': 8500},
]

create_stores_table()
populate_stores_inventory(cities_store_data)



# products = retreive_all_products()
#
# keys = ['storeid', 'average_purchase_amount']
# cities = ['Seattle', 'Portland', 'Los Angeles', 'San Fransisco']
# with open('data/stores/stores.csv', 'w') as output_file:
#
