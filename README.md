# interactive-nlp-api

FIXME

## Usage

### Run the application locally

`lein ring server`

You can visit the swagger endpoint at http://localhost:3000/index.html

The application relies on being able to make calls to the Stanford OpenNLP Server. 

You can start that server locally by downloading it and running

```
java -mx4g -cp "*" edu.stanford.nlp.pipeline.StanfordCoreNLPServer -port 9000 -timeout 15000`
```

from the directory you placed it in.

### To run a file in the repl ###

`(use 'interactive-nlp-api.openie-facade :reload-all)` 

### Packaging and running as standalone jar

```
lein do clean, ring uberjar
java -jar target/server.jar
```

### Packaging as war

`lein ring uberwar`

## License

Copyright ©  FIXME
