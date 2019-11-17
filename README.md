# interactive-nlp-api

See how a computer extracts information from what you wrote.

### Run the application locally

`lein ring server`

### Packaging and running as standalone jar

```
lein do clean, ring uberjar
java -Xmx2500m -jar target/server.jar
```

### Packaging as jar

`lein ring uberjar`
