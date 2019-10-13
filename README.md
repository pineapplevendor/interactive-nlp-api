# interactive-nlp-api

See how a computer extracts information from what you wrote.

### Run the application locally

`lein ring server`

You can visit the swagger endpoint at http://localhost:3000/index.html

### Packaging and running as standalone jar

```
lein do clean, ring uberjar
java -jar target/server.jar
```

### Packaging as war

`lein ring uberwar`
