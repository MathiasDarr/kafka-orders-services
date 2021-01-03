"""
Generate some data with faker & write to csvs
"""
# !/usr/bin/env python3
from random import shuffle, seed

from faker import Faker
from faker.providers.person.en import Provider
import csv
import random
import uuid

import numpy as np

fake = Faker()
fake.random.seed(4321)


def generate_customer_data():
    keys = ['customerid', 'first_name', 'last_name', 'email', 'password', 'city', 'seats', 'purchases_per_month', 'average_purchase_amount']
    cities = ['Seattle', 'Portland', 'Los Angeles', 'San Fransisco']
    with open('data/customers/customers.csv', 'w') as output_file:
        dict_writer = csv.DictWriter(output_file, keys)
        dict_writer.writeheader()
        customers = []

        for i in range(20000):
            first_name = fake.first_name()
            last_name = fake.last_name()
            email = first_name + last_name + '@gmail.com'
            password = '1!ZionTF'
            city = random.choice(cities)
            average_purchase_amount = int(np.random.normal(140, 35))

            if average_purchase_amount < 20:
                average_purchase_amount = 20

            customer = {'customerid': str(uuid.uuid4()), 'first_name': first_name, 'last_name': last_name, 'email': email,
                    'password': password, 'city': city, 'average_purchase_amount':average_purchase_amount, 'purchases_per_month': random.choice(list(range(10)))}
            customers.append(customer)
        dict_writer.writerows(customers)



if __name__=='__main__':
    generate_customer_data()
    print("DATA HAS BEEN GENERATED")
