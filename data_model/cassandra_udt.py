from utilities.cassandra_utilities import createCassandraConnection, createKeySpace

class Foo(object):

    def __init__(self, street, zipcode, otherstuff):
        self.street = street
        self.zipcode = zipcode
        self.otherstuff = otherstuff


dbsession = createCassandraConnection()

dbsession.set_keyspace('ks1')
dbsession.execute("CREATE TYPE address (street text, zipcode int)")
dbsession.execute("CREATE TABLE users (id int PRIMARY KEY, location frozen<address>)")

insert_statement = dbsession.prepare("INSERT INTO users (id, location) VALUES (?, ?)")


# since we're using a prepared statement, we don't *have* to register
# a class to map to the UDT to insert data.  The object just needs to have
# "street" and "zipcode" attributes (which Foo does):
dbsession.execute(insert_statement, [0, Foo("123 Main St.", 78723, "some other stuff")])





# # when we query data, UDT columns that don't have a class registered
# # will be returned as namedtuples:
results = dbsession.execute("SELECT * FROM users")
first_row = results.one()
address = first_row.location
print(address)  # prints "Address(street='123 Main St.', zipcode=78723)"
street = address.street
zipcode = address.street
