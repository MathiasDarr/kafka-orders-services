## Scrape REI.com ##

* This directory contains several scripts to scrape REI.com for products using BeautifulSoup & regular expressions

### How to run the product scraper ###
    * python3 scrape_rei_products.py
        - Script scrapes rei.com for products and writes to CSV
    * Run 'bash deploy.sh aws/local' from the data_model/products directory to create the DynamoDB Products table (defined using cloudformation)
    * python3 populate_dynamo_products.py
        - Script populates the dynamoDB table from the CSV files written in the first script.



