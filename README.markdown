# DB schema

# XML-RPC

ho creato una lista di funzioni, se concordiamo che queste sono a grandi linee quelle che ci servono, poi posso passare allo sviluppo xml-rpc in modo 
che l'applicazione java possa interfacciarsi direttamente con esse senza bisogno di accedere al database. una volta concordata la lista e i parametri
le mettiamo in bella forma con la documentazione di request/response

tutti i metodi implementati sono accessibili attraverso form di test attualmente visualizzata sul default controller

## post:
### create 
- params: lat, lon, datetime, timezone, content, source, tw_statusid, tw_replyid
*Implemented* $this->Post_model->create($content, $source, $datetime, $timezone, $id_user, $lat, $lon, $tw_statusid);

### read 
- params: lat, lon, geo_granularity, t_start, t_end, t_granularity
*Implemented* $result = $this->Post_model->read( $lat, $lon, $geo_granularity, $t_start, $t_end, $t_granularity );

### delete 
- params: (define parameters)
*Implemented* $this->Post_model->delete( $id );

- ? - do we need an "attach media" for already existent files?

## media
- create - params: id_post (can be empty? meaning: just to add an element to our db, it can be assigned to a post later with media.update), filename, file, description
- read - params: id_post
- update - params: id_media, id_post, filename, file, description

## terms / vocabulary
- vocabulary_create - params: description
- vocabulary_read - params: id_vocabulary (returns list of terms stored, maybe in json/object format?)
- vocabulary_delete - params: id_vocabulary
- term_create - params: id_vocabulary, name, id_termsyn (can be null), id_termparent (can be null)
- term_update - params: id_term, name, id_termsyn (can be null), id_termparent (can be null)
- term_delete - params:id_term

## query
### create 
- params: terms, contexts, lat, lon, datetime, timezone, (maybe an extra param to specify sources?)
*Implemented* $result = $this->fom_search->query( $terms, $since, $until, $where, $granularity, $source );

## cluster
- ? - specs still TBD
qua dovete aiutarmi voi, che metodi sono necessari per l'accesso / modifica di questa informazione?

## log
- create - params: id_user, action, meta (can be our notation for specific queries, e.g. query_id, terms, ...)
*Implemented* $this->fom_logger->log('', $action, $meta);

# Reference

- markdown syntax: http://daringfireball.net/projects/markdown/syntax

- uso di Git http://book.git-scm.com/3_distributed_workflows.html

- Veloce ed efficace tutorial per codeigniter + doctrine (doctrine non usato attualmente in FOM!)
http://www.phpandstuff.com/articles/codeigniter-doctrine-from-scratch-day-1-install-and-setup
