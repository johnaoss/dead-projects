"""	
	FlaskApp
	=========================
	A URL shortener that I wrote to get familiar with Flask and databases.

	:copyright: (c) 2016 by John Oss
	:license: TBD
"""

import os
import uuid
import re
import sqlite3
from urllib.parse import urlparse
from flask import Flask, render_template, abort, request, session, flash, redirect, url_for, g

app = Flask(__name__)
app.config.from_object(__name__)
app.config.update(dict(
    DATABASE=os.path.join(app.root_path, 'flaskapp.db'),
    SECRET_KEY='development key',
    USERNAME='admin',
    PASSWORD='default'
))

# Remove additional whitespace due to Jinja templates.
app.jinja_env.lstrip_blocks = True
app.jinja_env.trim_blocks = True

# CLI command that creates the DB, and also can be used to wipe the DB.
# Currently broken when attempting to put inside Docker image, so just left as is.
def init_db():
    db = get_db()
    with app.open_resource('schema.sql', mode='r') as f:
        db.cursor().executescript(f.read())
    db.commit()
    print('Initialized the database.')

# Modified DB 
def get_db():
    db = getattr(g, '_database', None)
    if db is None:
        db = g._database = sqlite3.connect(app.config['DATABASE'])
        db.row_factory = sqlite3.Row
    return db

# Called when the request ends
@app.teardown_appcontext
def close_connection(exception):
    db = getattr(g, '_database', None)
    if db is not None:
        db.close()

# Queries the DB and returns a list of dictionaries
def query_db(query, args=(), one=False):
    cur = get_db().execute(query, args)
    rv = cur.fetchall()
    return (rv[0] if rv else None) if one else rv

#Checks to see if a given string is a valid
def url_validator(link):
    try:
        result = urlparse(link)
        return True if [result.scheme, result.netloc, result.path] else False
    except:
        return False

# Main page
@app.route('/', methods=['GET', 'POST'])
def homepage():
    db = get_db()
    links = query_db('select mini, dest from links order by id desc')
    if request.method == 'POST' and request.form['link'] != "":
        link = str(request.form['link'])
        f_link = 'https://' + link
        if url_validator(link):
            u = uuid.uuid1()
            db.execute('insert into links (dest, mini) values (?, ?)',
                        [link, u.hex])
            db.commit()
        else:
            flash('Error: Not a valid URL')
    return render_template("index.html", links=links)

# Returns the hyperlinked result.
@app.route('/<string:link>')
def process_url(link):
    result = query_db('select * from links where mini=?', [link], one=True)
    if result is None:
        abort(404)
    dest = result['dest']
    if not re.match('^(http|https)://',dest):
        dest = 'https://' + dest
    return redirect(dest)

# Login for Admin (unsure of what to do with it yet)
@app.route('/login', methods=['GET', 'POST'])
def login():
    error = ''
    if request.method == 'POST':
        # TODO: Connect to DB for potential multi-user support
        if request.form['username'] != app.config['USERNAME'] and request.form['password'] != app.config['PASSWORD']:
            error = 'Invalid username or password'
        else:
            session['logged_in'] = True
            # flash is ugly, please fix
            flash('You were logged in')
            return redirect(url_for('homepage'))
    return render_template('login.html', error=error)

# TOOD: Beautify this.
@app.route('/logout')
def logout():
    session.pop('logged_in', None)
    flash('You have been logged out')
    return redirect(url_for('homepage'))

@app.errorhandler(404)
def page_not_found(e):
    return render_template("404.html"), 404

# TODO: Make more secure by using one of the deployment methods.
if __name__ == "__main__":
    app.run(host='0.0.0.0')
