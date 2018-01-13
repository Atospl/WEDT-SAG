# Proof of concept gensim doc2vec
## Instalacja

Testowany na pythonie 3.4

> pip install gensim

> pip install nltk

Ściągniecie korpusu

> python -c 'import nltk; nltk.download("reuters")'

## Trening

Trenuje model doc2vec na korpusie wiadomości reutersa (treningowym) i zapisuje we
wskazanym pliku (czas treningu ~10s)

> python train.py MODEL_OUTPUT

np.

> python train.py model

## Testy

Wczytuje model i znajduje w korpusie testowym dwie najbardziej podobne
wiadomości do wiadomości o podanym numerze z zakresu
[0, rozmiar korpusu]. Jak podany numer za duży to się wywali.

> python test.py MODEL_INPUT TEST_CASE_NUMBER

np.

> python test.py model 2
