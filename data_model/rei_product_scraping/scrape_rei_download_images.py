"""
This script scrapes REI.com for products & writes csv files for each category i.e mens-boots.  In order to not be
blocked, I include random sleeps which are pretty long so this may take a long time to run.  If you don't want to
wait just comment out the sleep

"""
# !/usr/bin/env python3

import requests
from bs4 import BeautifulSoup
import re
import csv
from selenium import webdriver
import boto3
import os
import time
from time import sleep
from random import randint

driver = webdriver.Chrome('/home/mddarr/data/libraries/selenium/chromedriver')
s3_client = boto3.client('s3')
BUCKET = 'dakobed-outdoor-recreation'


def get_product_detail(url, category):
    """
    This function scrapes product detail information information of an individual product
    :param url: product url
    :param category: cateogry of product
    :return: product dictionary with product name, vendor, colors, price & category
    """

    page = requests.get(url)
    soup = BeautifulSoup(page.text, 'html.parser')

    product_soup = soup.find("div", {"id": "product-container"})

    colors = parse_colors(product_soup)
    price = parse_price(product_soup)

    vendor = product_soup.find('input', {'name': 'vendor'})['value']

    product_name = product_soup.find('input', {'name': 'product_desc'})['value']
    parsed_name = product_name.split(' - ')[0]
    parsed_name = parsed_name.replace(vendor, '').lstrip()
    if len(colors) == 0:
        colors = None

    driver.get(url)

    img = driver.find_element_by_class_name('media-center-primary-image')
    # src = img.get_attribute('src')
    if not os.path.exists('products/{}'.format(category)):
        os.makedirs('products/{}'.format(category))

    image_file_name = 'products/{}/{}-{}.png'.format(category, vendor, parsed_name)
    with open(image_file_name, 'wb') as file:
        file.write(img.screenshot_as_png)

    with open(image_file_name, "rb") as f:
        s3_client.upload_fileobj(f, BUCKET, image_file_name)
    image_url = 'https://dakobed-outdoor-recreation.s3-us-west-2.amazonaws.com/{}'.format(image_file_name)
    product = {'name': str(parsed_name), 'vendor': str(vendor), 'colors': colors, 'price': price, 'url': url,
               'category': category, 'image_url': image_url}
    return product


def parse_price(product_soup):
    """
    This function parses the price from the product detail HTML
    :param product_soup:
    :return:
    """
    pscript = product_soup.find('script')
    ptext = str(pscript)
    price_iterator = re.finditer('"price":[0-9]+.[0-9]+', ptext)
    first_price = next(price_iterator)
    price_string = ptext[first_price.start():first_price.end()]
    price = price_string.split(':')[1]
    return price


def parse_colors(product_soup):
    """
    This function parses the colors from the product detail HTML
    :param product_soup:
    :return:
    """
    script_tag = product_soup.find('script')
    script_text = str(script_tag)
    color_iterator = re.finditer('"displayName":"[a-zA-Z]+"', script_text)

    colors = set()

    sizes = {'S', 'M', 'XL', 'XXL', 'XXXL', 'L', 'XS'}

    while True:
        try:
            next_color = next(color_iterator)
            color_match = script_text[next_color.start():next_color.end()]
            color = color_match.split(':')[1].replace('"', '')
            if color not in sizes:
                colors.add(color)
        except Exception as e:
            break

    return list(colors)


def get_products(category):
    """
    This function takes a category i.e 'mens-casual-jackets' (as found on the rei.com website) & returns a list of
    product urls :param category: :return: list of product urls
    """
    pagenumber = 1
    url = 'https://www.rei.com/c/{}'.format(category)

    page = requests.get(url)
    soup = BeautifulSoup(page.text, 'html.parser')
    navclass = soup.find("nav", {"class": "_3-4shQxwfGRyzNItrZFEiC"})
    npages = len(navclass.find_all('a', href=True))

    products = set()
    while pagenumber <= npages:
        url = 'https://www.rei.com/c/{}?page={}'.format(category, pagenumber)
        page = requests.get(url)
        soup = BeautifulSoup(page.text, 'html.parser')
        search_results = soup.find('div', {'id': 'search-results'})
        for a in search_results.find_all('a', href=True):
            products.add(a['href'])
            # print("Found the URL:", a['href'])
        pagenumber += 1
    return ['https://www.rei.com' + str(product) for product in products if str(product).find('rei-garage') < 0]


done = ['mens-casual-jackets', 'mens-boots', 'mens-insulated-jackets', 'mens-rain-jackets', 'mens-running-jackets',
        'mens-snow-jackets', 'mens-winter-boots']
categories = ['mens-casual-jackets', 'mens-boots', 'mens-insulated-jackets', 'mens-rain-jackets',
              'mens-running-jackets', 'mens-snow-jackets', 'mens-winter-boots', 'mens-fleece-and-soft-shell-jackets',
              'backpacking-packs', 'day-packs', 'womens-boots', 'womens-casual-jackets', 'womens-insulated-jackets',
              'womens-fleece-and-soft-shell-jackets', 'womens-rain-jackets', 'womens-running-jackets']
# This category wasn't working
windshells = 'mens-wind-shells'

products = get_products(categories[0])
all_products = []

for category in categories:
    products = get_products(category)
    parsed_products = []
    for i, product_url in enumerate(products):
        print(i)
        try:
            parsed_products.append(get_product_detail(product_url, category))
        except Exception as e:
            print(e)
        sleep(randint(3, 12))
    keys = ['name', 'vendor', 'colors', 'price', 'url', 'category', 'image_url']

    with open('products/{}.csv'.format(category), 'w') as output_file:
        dict_writer = csv.DictWriter(output_file, keys)
        dict_writer.writeheader()
        dict_writer.writerows(parsed_products)
