# Proof of concept gensim doc2vec
## Instalacja

Testowany na pythonie 3.4

> $ sudo pip install virtualenv

> $ ./setup.sh

> $ . venv/bin/activate

## Test

Wczytuje model i znajduje w korpusie testowym dwie najbardziej podobne
wiadomości do wiadomości o podanym numerze z zakresu
[0, rozmiar korpusu]. Jak podany numer za duży to się wywali.

> $ python test.py MODEL_INPUT TEST_CASE_NUMBER

np.

> $ python test.py model 2

##Serwer

Uruchamianie (w virtualenv):

> $ python nlp_server.py

### Liczenie wektorów

> $ curl http://localhost:5000/vector -d "text=Quick brown fox jumped over the lazy dog" -X GET

Treść artykułu jako prarametr *text*, *GET*

### Porównywanie wektorów

Przykładowy json w *request.json*.

Json jako parametr *json*. Metoda *POST*.

> $ curl http://localhost:5000/similar -d "json=`cat request.json`"
