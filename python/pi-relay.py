#!/usr/bin/env python
"""
Very simple HTTP server in python.

Usage::
    sudo python mock_OAuthServer.py [<port>]

From https://gist.github.com/bradmontgomery/2219997

"""


from http.server import BaseHTTPRequestHandler, HTTPServer
import socketserver
import json
from datetime import datetime, date, time, timedelta
import re
import argparse
import RPi.GPIO as GPIO
from time import sleep

Relay_channel = [17, 18]

parser = argparse.ArgumentParser()
parser.add_argument('-p', '--port', type=int, help='service listening port', required=True)
args = vars(parser.parse_args())

class S(BaseHTTPRequestHandler):
    def _set_headers(self):
        self.send_response(200)
        self.send_header('Content-type', 'application/json')
        self.end_headers()

    # handle all the POST's here
    def do_POST(self):
        self._set_headers()
        content_length = int(self.headers['Content-Length']) # <--- Gets the size of data
        post_data = self.rfile.read(content_length) # <--- Gets the POST  data itself
        #post_data = post_data.decode('utf-8')
        # debug statements
        print("request URL")
        print(self.path)
        print("in post method")
        print(post_data)
        # debug statements

        if self.path == '/lights/off':
            #load the request body which is JSON to Python map
            #data = json.loads(post_data)
            #input validation of json objects
            print ("lights off" )
            GPIO.output(17, GPIO.HIGH)
            GPIO.output(18, GPIO.HIGH)

        elif self.path == '/lights/on':
            print ("lights on")
            GPIO.output(17, GPIO.LOW)
            GPIO.output(18, GPIO.LOW)
            #sleep(0.5)
            #GPIO.output(17, GPIO.HIGH)
            #GPIO.output(18, GPIO.HIGH)
            #sleep(0.5)


        else:
            print ("lights off")

        print("hello world")    
        #self.wfile.write( json.dumps(d).encode() )

        
    # main function
def run(server_class=HTTPServer, handler_class=S, port=80):
    server_address = ('', port)
    httpd = server_class(server_address, handler_class)
    print ("setting up GPIO ... ")
    setup()
    print('Starting httpd... on port ' + str(port))
    httpd.serve_forever()

def setup():
	GPIO.setmode(GPIO.BCM)
	GPIO.setup(Relay_channel, GPIO.OUT, initial=GPIO.HIGH)
	

def destroy():
    print ("destroying GPIO setup")
    GPIO.output(Relay_channel, GPIO.LOW)
    GPIO.cleanup()

# main loop
if __name__ == "__main__":
    try:    
        run(port=args['port'])
    
    except KeyboardInterrupt:
        destroy()

