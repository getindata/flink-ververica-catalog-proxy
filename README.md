# flink-ververica-catalog-proxy
Proxy to the internal Ververica Catalog. Proxy implementation calls API exposed by Ververica to get tables, views, functions definitions.


## Usage
Import jar to your jupyter notebook env

```
CREATE CATALOG vvp
WITH (
  'type' = 'ververica',
  'gid.vvp.proxy.url' = 'http://localhost:8080'
);

USE CATALOG vvp;

SHOW TABLES;
```


Using https connection and vvp token:
```
CREATE CATALOG vvp
WITH (
  'type' = 'ververica',
  'gid.vvp.proxy.url' = 'https://localhost:8080',
  'gid.vvp.proxy.http.headers' = 'Authorization,Bearer <token>',
  'gid.vvp.proxy.http.security.cert.server' = '/home/user/vvp.crt'
);

USE CATALOG vvp;

SHOW TABLES;
```

## TODO
1. Functions