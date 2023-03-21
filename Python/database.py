import psycopg2
import json
from decimal import Decimal


def connectToDb():
    connection = psycopg2.connect(
    host="127.0.0.1",
    database="Tower",
    user="postgres",
    password="kamalesh",
    port="7077")
    return connection

def getDataFromLocation(pos_offset,neg_offset):
    conn = connectToDb()
    cur = conn.cursor()
    # Execute a SQL query
    sampleQuery = "SELECT * FROM Tower WHERE id > 1 AND id < 40;"
    query ="SELECT * FROM Tower WHERE lat > "+str(neg_offset[0])+" AND lat < "+str(pos_offset[0])+" AND lon > "+str(neg_offset[1])+" AND lon < "+str(pos_offset[1])+";"
    print(query)
    cur.execute(query)

    # Fetch the query results
    rows = cur.fetchall()
    jsonlist="["
    for row in rows:
        t = tuple(float(x) if isinstance(x, Decimal) else x for x in row)
        jsonlist = jsonlist + '{"id" :'+str(t[0])+',"radio": "'+str(t[1])+'","mcc" :'+str(t[2])+',"net":'+str(t[3])+',"area":'+str(t[4])+',"cell":'+str(t[5])+',"lon":'+str(t[6])+',"lat":'+str(t[7])+"},"

        print(jsonlist)
    jsonlist = jsonlist[:-1] +  "]"
    # Close the cursor and connection
    cur.close()
    conn.close()
    return jsonlist
