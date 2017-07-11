# seasame-rio-yars

[Yet Another RDF Serialization](https://www.researchgate.net/publication/309695477_RDF_Data_in_Property_Graph_Model) (YARS) serialization parser for [Sesame](https://bitbucket.org/openrdf/sesame). This project depends on [modified sesame-rio-api](https://github.com/lszeremeta/sesame-rio-api).

## Example supported YARS file

```
(bjf52cgbj8vcou3{v:'<http://example.com/text>'})
(92fqdijn7c79w3d{v:'example type'})
(bjf52cgbj8vcou3)-[http://example.com/type]->(92fqdijn7c79w3d)
```

You can convert [Turtle](https://www.w3.org/TR/turtle/) serialization to YARS supported by this parser using [ttl-to-yars](https://github.com/lszeremeta/ttl-to-yars) or simply use one of [generated samples](https://github.com/lszeremeta/yars-samples).

## See also
* [neo4j-sparql-extension-yars](https://github.com/lszeremeta/neo4j-sparql-extension-yars) - Neo4j [unmanaged extension](http://docs.neo4j.org/chunked/stable/server-unmanaged-extensions.html)
for [RDF](http://www.w3.org/TR/rdf-primer/) storage and
[SPARQL 1.1 query](http://www.w3.org/TR/sparql11-protocol/) features with support for YARS serialization,
* [sesame-rio-api](https://github.com/lszeremeta/sesame-rio-api) - modified Sesame API with added support for YARS serialization,
* [ttl-to-yars](https://github.com/lszeremeta/ttl-to-yars) - simple [Turtle](https://www.w3.org/TR/turtle/) to YARS converter
* [yars-samples](https://github.com/lszeremeta/yars-samples) - ready to use sample YARS files

## Author
Copyright (C) 2017 [Łukasz Szeremeta](https://github.com/lszeremeta). All rights reserved.

Distributed under standard [Sesame license](https://github.com/lszeremeta/sesame-rio-yars/blob/master/LICENSE).
