#!/bin/bash

PATH="`pwd`/venv/bin:$PATH"
if [ ! -d venv ]; then
    virtualenv -p /usr/bin/python3 venv
fi

. venv/bin/activate

echo "Installing requirements"

pip install -r requirements.txt

python -c "import nltk; nltk.download('reuters')"
python -c "import nltk; nltk.download('nonbreaking_prefixes')"
python -c "import nltk; nltk.download('perluniprops')"

echo "Generating doc2vec model"

python train.py model

cat <<EOF

Wejście do virtualenva:

$ . venv/bin/activate

Wyjście:

$ deactivate

Test gensima i nltk
$ python test.py model 1

EOF
    
