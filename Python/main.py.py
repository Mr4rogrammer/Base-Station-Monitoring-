import psycopg2
from conversion import offset_coords_metres
from database import *
from flask import Flask

towerApi = Flask(__name__)

def getValueFromDatabaseJson(lat,lon):
    pos_offset = offset_coords_metres(lat,lon,1000,1000)
    neg_offset = offset_coords_metres(lat,lon,-1000,-1000)
    return getDataFromLocation(pos_offset,neg_offset)

@towerApi.get('/location/<float:lat>/<float:lon>')
def converter_with_type(lat,lon):
    return getValueFromDatabaseJson(lat,lon)

@towerApi.route('/')
def mrprogrammer():
    return "Mr.Programmer"

if __name__ == '__main__':
    towerApi.run()
