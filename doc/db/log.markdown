# log modifiche database + q&a

## 10.11.17

ho dovuto aggiungere il campo 

## 10.11.13

> Q: Quali cluster saranno salvati nel DB, solo i semantici o tutti? Se tutti, ogni cluster ha bisogno di avere i termini associati salvati?
A: m... boh. come impatta questa decisione sulle funzionalita'? in ogni caso hai ragione tu: e' bene probabilmente salvare la query in un campo specifico per rispondere alla domanda successiva.

> Q: Ho pensato di aggiungere un campo per memorizzare la query originale dell'utente, non so se può servire (dipende dalla risposta all'altra domanda)
A: ottima idea, aggiunto campo nel db

> Q: una Query deve avere dei termini associati -E- presenti nel db?
A: l'idea era di usare i tag come sorgente descrittiva della query stessa, ma probabilmente l'idea di salvare una stringa come origine della query nel db e' vincente. devo purtroppo rispondere con una domanda: ha senso a questo punto inserire una tabella di relazione (fom_querytag)? potrebbe rimanere inutilizzata probabilmente

> Q: Le granularità sono definite nel DB come varchar, come le posso trattare? Pensavo di dichiararle come interi e intenderle come km per la gran. spaziale e ore per quella temporale, è troppo limitante?
A: per quanto riguarda la granularita' spaziale pensavo di affidarmi esattamente a quanto definito da dev.twitter.com (http://dev.twitter.com/doc/get/geo/search): This is the minimal granularity of place types to return and must be one of: poi, neighborhood, city, admin or country. If no granularity is provided for the request neighborhood is assumed. Setting this to city, for example, will find places which have a type of city, admin or country.

> Q: un termine ha al più un sinonimo?
A: probabilmente no, ma non ci incasiniamo ancora di piu' per adesso. se la vediamo come un termine principale che viene usato come riferimento, allora si, un termine ha al piu' un sinonimo quando non e' il primario. probabilmente il campo puo' essere rimosso del tutto usando la tabella di relazione aggiunta recentemente.

> Q: Chi popola il vocabolario?
A: di nuovo rispondo con una domanda: sarebbe possibile cacciare termini dentro i(l) vocabolari(o) man mano che le query vengono eseguite ed espanse da wiki?