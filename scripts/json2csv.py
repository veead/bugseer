#!/usr/bin/python

import csv
import json
import os

def json2csv():
	files = [f for f in os.listdir('.') if os.path.isfile(f) and f.endswith("json")]
	out = csv.writer(open("jira.csv", "wb+"))
	header = True
	for f in files:
		json_data=open(f)
		js = json.load(json_data)
		if header:
			out.writerow(["key", "priority", "updated", "created"])
			header = False
		for x in js["issues"]:
			out.writerow([x["key"], x["fields"]["priority"]["name"], x["fields"]["created"][:10], x["fields"]["updated"][:10]])
				
json2csv()
