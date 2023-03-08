# flink-ververica-catalog-proxy
Proxy to the internal Ververica Catalog. Proxy implementation calls API exposed by Ververica to get tables, views, functions definitions.


## Usage
Import jar to your jupyter notebook env

```
CREATE CATALOG vvp
WITH (
  'type' = 'ververica',
  'vvp-url' = 'http://localhost:8080'
);

USE CATALOG vvp;

SHOW TABLES;
```

## TODO
1. Functions
1. Add headers to config and process them