import sys
import os
import sqlite3
import re
import readline
import parse2
import make_sql
readline.parse_and_bind('tab: complete')


default_dbname="nwb_idx2.db"
dbname=default_dbname
con = None     # database connection
cur = None       # cursor


def open_database():
	global dbname, con, cur
	# this for development so don't have to manually delete database between every run
	con = sqlite3.connect(dbname)
	cur = con.cursor()

def show_available_files():
	global con, cur


def run_query(sql):
	global con, cur
	result=cur.execute(sql)
	rows=result.fetchall()
	num_rows = len(rows)
	for row in rows:
		print(row)


def get_and_run_queries():
	query = sys.argv[2]
	qi = parse2.parse(query)
	sql = make_sql.make_sql(qi)
	run_query(sql)

def main():
	global con, dbname
	if len(sys.argv) == 1:
		print("Database not specified; using '%s'" % dbname)
	else:
		dbname=sys.argv[1]
	if not os.path.isfile(dbname):
		sys.exit("ERROR: database '%s' was not found!" % dbname)
	open_database()
	get_and_run_queries()
	# print ("scanning directory %s" % dir)
	# scan_directory(dir)
	con.close()

if __name__ == "__main__":
	main()
