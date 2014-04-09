#!/usr/bin/python
import re
import csv
import commands 
import subprocess

#TODO Make it work
def fetch_commits_with_jira():
	git log --pretty=oneline | grep -E '[a-zA-Z]+\-[0-9]+' > /tmp/commits

#TODO Make it work	
def fetch_jiras_with_bugs():
	for i in [0..17]
		'curl https://appdirect.jira.com/rest/api/2/search?jql=issuetype+in%20(Bug)&fields=key,priority,created,updated&startAt=' + i + '&maxResults=1000'
	 
def list_files_from_commits():
	f = open('/tmp/commits', 'r')
	outcsv = csv.writer(open("/tmp/files.csv", "wb+"))
	line = f.readline()
	p = re.compile('[a-zA-Z]+\-[0-9]+|^\w{40,40}')
	while line:
		result = p.findall(line)
		sha = result[0]
		c = subprocess.Popen(['git', 'show', '--pretty=%ct', '--name-only', result[0]], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
		out, err = c.communicate()
		time = out.split("\n")[0]
		for fname in out.split("\n")[1:]:
			if fname != '':
				outcsv.writerow([time, sha, fname])
		line = f.readline()
	f.close()
 
def list_commits_with_jira():
	f = open('/tmp/commits', 'r')
	out = csv.writer(open("commits.csv", "wb+"))
	line = f.readline()
	p = re.compile('[a-zA-Z]+\-[0-9]+|^\w{40,40}')
	while line:
		result = p.findall(line)
		result.reverse()
		commit = result.pop()
		result =[jira.upper() for jira in set(result) if jira.lower() != "utf-8"]
		for jira in result:
			out.writerow([commit, jira])
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

list_commits_with_jira()
