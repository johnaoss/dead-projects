FROM ubuntu:16.04

MAINTAINER John Oss "john@johnoss.com"

RUN apt-get update -y && \
	apt-get install -y python3-pip python3-dev

COPY ./requirements.txt /app/requirements.txt

WORKDIR /app

RUN pip3 install -r requirements.txt

COPY . /app

RUN pip3 install .

RUN export FLASK_APP=flaskapp

ENTRYPOINT ["python3"]

CMD ["flaskapp/flaskapp.py"]
