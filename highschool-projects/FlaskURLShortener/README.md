# FlaskApp
A URL shortener written in Python using Flask and SQLite3.

It is primarily meant as a way for me to get used to Python and Flask. 

**UPDATE 2018-07-18**: This was written in Grade 11, and as such is pretty awful. Proceed at your own risk.

## Installation
For a Docker installation from source, follow the following steps, if being used anywhere other than testing, please change the default variables, as they are insecure.

Assuming you've cloned the application already.

```bash
cd FlaskURLShortener/
docker build -t urlshortener .
docker run -d -it --name urlshortener -p 5000:5000 urlshortener:latest
```

Please note this is untested on any other platforms, and not ready for production usage in any way, shape or form.

## License

MIT.
