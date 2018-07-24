FROM iron/go:dev

WORKDIR /gopath

ENV GOPATH=/gopath

COPY . /gopath/src/github.com/johnaoss/linkedin_resume

WORKDIR /gopath/src/github.com/johnaoss/linkedin_resume

RUN go get -t ./...

RUN go build main.go

EXPOSE 5000

ENTRYPOINT [ "./main" ]