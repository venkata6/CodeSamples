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

        #message loop for all three APIs
        if self.path == '/AuthCodes':
            #load the request body which is JSON to Python map
            data = json.loads(post_data)
            #input validation of json objects

            if data.get("client_secret") == None or  not data['client_secret'].startswith("OAUTH-TEST-SECRET"):
                d =   {  "error": "invalid_secret",  "error_description" : "client secret is missing OR client_secret must be 'OAUTH-TEST-SECRET'", "error_uri" : "" }
            elif data.get("client_id") == None or not data['client_id'].startswith("OAUTH-TEST-CLIENT"):
                d =   {  "error": "invalid_client",  "error_description" : "client_id is missing OR client_id must be 'OAUTH-TEST-CLIENT'", "error_uri" : "" }
            elif data.get("grant_type") == None or  data['grant_type'] != "authorization_code":
                d =   {  "error": "invalid_grant_type",  "error_description" : "grant_type is missing OR grant_type must be 'authorization_code'", "error_uri" : "" }
            elif data.get("code") == None or not data['code'].startswith("OAUTH-TEST-CODE")  :
                d =   {  "error": "invalid_code",  "error_description" : "code is missing OR code  must starting with  'OAUTH-TEST-CODE'", "error_uri" : "" }

            else:
                #if no input error, send the mock JSON response back
                patternForExpiresIn = re.compile("expires_in\s*=\s*(\d+)")
                expiresInMatch = patternForExpiresIn.search(data.get("code"))
                if expiresInMatch is None:
                    d = {"error": "invalid request",
                         "error_description": "expires_in is missing",
                         "error_uri": ""}
                else:
                    expiresIn = expiresInMatch.group(1)
                    expiresIn = int(expiresIn)
                    if expiresIn == 0:
                        expiresIn = 600

                    p = re.compile("roku_channel_id\s*=\s*(\d+)")
                    m = p.search(data.get("code"))
                    if m:
                        g = m.group(1)
                        d = {  'tokens' : [ {
                            'roku_channel_id': g,
                            'token': {  "access_token": "a-09vbgjsdou1430iav,valid_until= " + (datetime.utcnow()+timedelta(seconds=expiresIn)).strftime('%d-%m-%YT%H:%M:%SZ'),  "token_type" : "Bearer", "expires_in" : expiresIn,"refresh_token" : "r-reoirg94i3omvds2" }
                        }
                        ]
                        }
                    else:
                        d = {
                                "access_token": "a-09vbgjsdou1430iav,valid_until= " + (
                                datetime.utcnow() + timedelta(seconds=expiresIn)).strftime('%d-%m-%YT%H:%M:%SZ'),
                                      "token_type": "Bearer", "expires_in": expiresIn, "refresh_token": "r-reoirg94i3omvds2"
                            }

        # second API mock
        elif self.path == '/RefreshTokens':
            #data = json.loads(post_data, "UTF-8")
            expiresIn = 0
            data = json.loads(post_data)
            if data.get("client_secret") == None or not data['client_secret'].startswith("OAUTH-TEST-SECRET"):
                d =   {  "error": "invalid_secret",  "error_description" : "client_secret is missing  OR  not  'OAUTH-TEST-SECRET'", "error_uri" : "" }
            elif data.get("client_id") == None or not data['client_id'].startswith("OAUTH-TEST-CLIENT"):
                d =   {  "error": "invalid_client",  "error_description" : "client_id is  missing OR not 'OAUTH-TEST-CLIENT'", "error_uri" : "" }
            elif data.get("grant_type") == None or data['grant_type'] != "refresh_token":
                d =   {  "error": "invalid_grant_type",  "error_description" : "grant_type is missing OR not 'refresh_token'", "error_uri" : "" }
            elif data.get("refresh_token") == None or  not data['refresh_token'].startswith("r-reoirg94i3omvds2")  :
                d =   {  "error": "invalid_refresh_token",  "error_description" : "refresh_token is missing OR  not starting with  'r-reoirg94i3omvds2'", "error_uri" : "" }
            else:
                patternForExpiresIn = re.compile("expires_in\s*=\s*(\d+)")
                expiresInMatch = patternForExpiresIn.search(data.get("refresh_token"))
                if expiresInMatch is None:
                   expiresIn = 600

                        # if expiresInMatch is None:
                    #     d = {"error": "invalid request",
                    #          "error_description": "expires_in is missing",
                    #         "error_uri": ""}
                else:
                    expiresIn = expiresInMatch.group(1)
                    expiresIn = int(expiresIn)

                d = { "access_token": "a-09vbgjsdou1430iav,valid_until= " + (datetime.utcnow()+timedelta(seconds=expiresIn)).strftime('%d-%m-%YT%H:%M:%SZ'),  "token_type" : "Bearer", "expires_in" : expiresIn,"refresh_token" : "r-reoirg94i3omvds2" }

        # third API mock
        elif self.path == '/Artifacts':
            data = json.loads(post_data)

            if  data.get("client_id") == None or not data['client_id'].startswith("OAUTH-TEST-CLIENT"):
                d =   {  "error": "invalid_client",  "error_description" : "client_id is missing OR client_id must be 'OAUTH-TEST-CLIENT'", "error_uri" : "" }
            elif  data.get("grant_type") == None or data['grant_type'] != "access_token":
                d =   {  "error": "invalid_grant_type",  "error_description" : "grant_type is missing OR grant_type must be 'access_token'", "error_uri" : "" }
            elif  data.get("access_token") == None or not data['access_token'].startswith("a-09vbgjsdou1430iav")  :
                d =   {  "error": "invalid_accesstoken",  "error_description" : "access_token is missing OR access_token  must starting with  'a-09vbgjsdou1430iav'", "error_uri" : "" }

            elif data.get("roku_device_id") == None or  not data['roku_device_id']:
                d =   {  "error": "invalid_roku_device_id",  "error_description" : "roku_device_id cant be null or empty'", "error_uri" : "" }
            else:
                patternForValidUntil = re.compile("valid_until\s*=\s*(\d\d-\d\d-\d\d\d\dT\d\d:\d\d:\d\dZ)")
                validTimeMatch = patternForValidUntil.search(data.get("access_token"))

                if validTimeMatch is None:
                    d = {"error": "invalid valid until",
                         "error_description": "valid_until time missing",
                         "error_uri": ""}

                else:
                    validTime = validTimeMatch.group(1)
                    current_time = datetime.now()
                    valid_until = datetime.strptime(validTime, '%d-%m-%YT%H:%M:%SZ')

                    if  valid_until < current_time:
                        d = {"error": "context expired",
                             "error_description": "context expired",
                            "error_uri": ""}
                    else:
                        d = { "token_type": "urn:roku:oauth:token_type:artifact", "artifact" : "af-mdfi53450fslQ94" }
        else:
            d = "test"
        print("hello world")    
        print(d)
        self.wfile.write( json.dumps(d).encode() )

        
    # main function
def run(server_class=HTTPServer, handler_class=S, port=80):
    server_address = ('', port)
    httpd = server_class(server_address, handler_class)
    print('Starting httpd... on port ' + str(port))
    httpd.serve_forever()

# main loop
if __name__ == "__main__":
    run(port=args['port'])

