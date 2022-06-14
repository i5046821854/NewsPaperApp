from sqlalchemy import create_engine
from sqlalchemy.orm import scoped_session, sessionmaker
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String
from flask import Flask
from flask import request
from flask import jsonify
from werkzeug.serving import WSGIRequestHandler
import json

USER = "postgres"
PW = "qhfflqpdj^2"
URL = "database-1.cdga0jdfgjjn.ap-northeast-2.rds.amazonaws.com"
PORT = "5432"
DB = "postgres"
engine = create_engine("postgresql://{}:{}@{}:{}/{}".format(USER, PW, URL,PORT, DB))
db_session = scoped_session(sessionmaker(autocommit=False, autoflush=False, bind=engine))
Base = declarative_base()
Base.query = db_session.query_property()

class User(Base):
    __tablename__ = 'users'
    id = Column(Integer, primary_key=True)
    name = Column(String(50), unique=True)
    passwd = Column(String(120), unique=False)
    category = Column(String(50), unique=False)
    def __init__(self, name=None, passwd=None, category=None):
        self.name = name
        self.passwd = passwd
        self.category = category

    def __repr__(self):
        return f'<User {self.name!r}>'

class Scraps(Base):
    __tablename__ = 'scraps'
    id = Column(Integer, primary_key=True)
    title =  Column(String(1000), unique=True)
    author = Column(String(100), unique=False)
    published = Column(String(1000), unique=False)
    description = Column(String(2000), unique=False)
    url = Column(String(1000), unique=False)
    image = Column(String(1000), unique=False)
    category = Column(String(50), unique=False)
    userseq = Column(Integer, unique=False)

    def __init__(self, title=None, author=None, published=None, description = None, url=None, image = None, category = None, userseq = None):
        self.title = title
        self.author = author
        self.published = published
        self.description = description
        self.url = url
        self.image = image
        self.category = category
        self.userseq = userseq


    def __repr__(self):
        return f'<Scraps {self.title!r}>'


# Base.metadata.drop_all(bind=engine)
Base.metadata.create_all(bind=engine)

WSGIRequestHandler.protocol_version = "HTTP/1.1"

app = Flask(__name__)

@app.route("/adduser", methods=['POST'])
def add_user():
    content = request.get_json(silent=True)
    name = content["name"]
    passwd = content["password"]
    category = content["categories"]
    print(name, passwd, category)
    u = User(name=name, passwd=passwd, category=category)
    db_session.add(u)
    db_session.commit()
    return jsonify(check=True)


@app.route("/idcheck", methods=['GET'])
def idCheck():
    name = request.args.get('name')
    if db_session.query(User).filter_by(name=name).first() is None:
        return jsonify(check=True)
    else:
        return jsonify(check=False)


@app.route("/login", methods=['POST'])
def login():
    content = request.get_json(silent=True)
    name = content["name"]
    passwd = content["password"]
    check = False
    users = db_session.query(User).all()
    result = {}
    result["check"] = False
    for i in users:
        if i.name == name and i.passwd == passwd:
            check = True
            result["check"] = True
            result["userSeq"] = i.id
            result["categories"] = i.category
            result["name"] = i.name
    print(result)
    return jsonify(result)

@app.route("/mypage", methods=['POST'])
def mypagepost():
    content = request.get_json(silent=True)
    title = content["title"]
    author = content["author"]
    published = content["published"]
    description = content["description"]
    url = content["url"]
    image = content["image"]
    category = content["category"]
    userseq = int(content["userseq"])


    if db_session.query(Scraps).filter_by(title=title).first() is None:
        scrap = Scraps(title=title, author=author, published=published, description = description, url=url, image = image, category = category, userseq = userseq)
        db_session.add(scrap)
        db_session.commit()
        return jsonify(status=True)
    else:
        return jsonify(status=False)

@app.route("/mypage", methods=['GET'])
def mypageget():
    seq = int(request.args.get('userseq'))
    datas = db_session.query(Scraps).join(User, Scraps.userseq == User.id).filter(Scraps.userseq == seq).all()
    newsList = []
    result = {}
    for data in datas:
        temp = {}
        temp["title"] = data.title
        temp["author"] = data.author
        temp["published"] = data.published
        temp["description"] = data.description
        temp["url"] = data.url
        temp["image"] = data.image
        category = [data.category]
        temp["category"] = category
        temp["userseq"] = data.userseq
        newsList.append(temp)
    result["news"] = newsList
    return jsonify(result)

@app.route("/check", methods=['GET'])
def checkScrap():
    title = request.args.get('title')
    userseq = int(request.args.get('userseq'))

    print(title)
    if db_session.query(Scraps).filter_by(title = title, userseq = userseq).first() is None:
        return jsonify(status=False)
    else:
        return jsonify(status=True)

@app.route("/delete", methods=['GET'])
def deleteScrap():
    title = request.args.get('title')
    userseq = int(request.args.get('userseq'))

    scrap = db_session.query(Scraps).filter_by(title=title, userseq=userseq).first()
    if scrap is None:
        return jsonify(status=False)
    else:
        db_session.delete(scrap)
        db_session.commit()
        return jsonify(status=True)


if __name__ == "__main__":
    app.run(host='localhost', port=8888)