#!/usr/bin/python
import re
import csv
import commands 
import subprocess
 
def fetch_files():
	f = open('/tmp/commits', 'r')
	outcsv = csv.writer(open("/tmp/files.csv", "wb+"))
	line = f.readline()
	p = re.compile('[a-zA-Z]+\-[0-9]+|^\w{40,40}')
	while line:
	  result = p.findall(line)
          c = subprocess.Popen(['git', 'show', '--pretty=%ct', '--name-only', result[0]], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
	  out, err = c.communicate()
          time = out.split("\n")[0]
	  for fname in out.split("\n")[1:]:
		  if fname != '':
			outcsv.writerow([time, result[0], fname])
	  line = f.readline()
	f.close()
 
def fetch_commits():
	f = open('/tmp/commits', 'r')
	out = csv.writer(open("commits.csv", "wb+"))
	line = f.readline()
	p = re.compile('[a-zA-Z]+\-[0-9]+|^\w{40,40}')
	while line:
	  result = p.findall(line)
	  result.reverse()
	  commit = result.pop()
	  result =[item.upper() for item in set(result) if item.lower() != "utf-8"]
	  for item in result:
			out.writerow([commit,item])
	  line = f.readline()
	f.close()
 
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

fetch_files()
