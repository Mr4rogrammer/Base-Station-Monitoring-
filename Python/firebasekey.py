
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db

cred = credentials.Certificate("key/key.json")

firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://mrtower-f2360-default-rtdb.firebaseio.com/'
})

ref = db.reference('/Tower/key')
ref.child("key").set('Hello, World!')

key = input("Enter the nogrok weburl : ")
ref.child("key").set(key)
