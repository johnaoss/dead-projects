# YASP

[![Build Status](https://travis-ci.org/johnaoss/yasp.svg?branch=master)](https://travis-ci.org/johnaoss/yasp)

Will eventually gather all information from your Linkedin Profile, and and serve it back to you as a resume.

Awaiting rewrite using the API wrapper I wrote.

## Planned Features

1. Serve webpage that allows one to input a URL to scrape from (a Linkedin Profile, or maybe using OAuth get a login through Linkedin?)
1. Scrape the data from LinkedIn and put it in a JSON object according to the JSONResume.org schema.
1. Validate that this data is safe to use.
1. Render a PDF/Webpage of the generated resume.
1. Profit!

## Currently finished features

1. JSON Validation
1. OAuth2.0 Login / API download.
1. Parsing of API Data.

## Credits

* Validation schema provided by [The JSONResume Project](https://github.com/jsonresume/resume-schema)
