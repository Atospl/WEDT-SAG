import sys
import pandas as pd
import numpy as np
from sklearn.metrics import auc, roc_curve
import matplotlib.pyplot as plt


def extract_tag_from_similarities(data, similarities, tag):
    """Extracts values from similarities where both articles are from
        the same tag"""

    tag_subset = data[data.tags == tag]

    sim_subset = similarities[similarities.x1.isin(tag_subset.id) & 
                              similarities.x2.isin(tag_subset.id)]

    return sim_subset


def get_ids_of_similar(data, tag):
    """Extracts list of tuples containing ids of similar articles"""
    tag_subset = data[data.tags == tag]
    identical_list = tag_subset[tag_subset.identical.notnull()]["identical"].tolist()
    identical_ret = []
    for item in identical_list:
        item = tuple([int(x) for x in item.split(',')])
        identical_ret.append(item)
    return identical_ret


def get_ground_truth_table(data, similarities, tag):
    """adds grount truth label to  similarities, to allow building ROC"""
    tag_similarities = extract_tag_from_similarities(data, similarities, tag)
    data_tag = data[data.tags == tag]
    similar_articles = get_ids_of_similar(data_tag, tag)
    gt_table = tag_similarities.apply(lambda x: check_ground_truth(x, similar_articles), axis=1)
    return gt_table


def check_ground_truth(x, similar_articles):
    (id1, id2) = x['x1'], x['x2']
    gt = 0
    for value in similar_articles:
        if id1 in value and id2 in value:
            gt = 1
    return pd.Series({'x1': id1, 'x2': id2, 'similarity': x['similarity'], 'gt': gt})


def create_roc(data, similarities, tag):
    gt = get_ground_truth_table(data, similarities, tag)
    fpr, tpr, thresholds = roc_curve(gt["gt"], gt["similarity"])
    # plot roc curve
    roc_auc = auc(fpr, tpr)
    print(roc_auc)
    fig = plt.figure()
    ax = fig.gca()
    ax.set_xticks(np.arange(0, 1.1, 0.1))
    ax.set_yticks(np.arange(0, 1.1, 0.1))
    for s in ["top", "right", "left", "bottom"]:
        ax.spines[s].set_visible(False)

    plt.plot(fpr, tpr, color='#0077bb',
             label='ROC AUC = {:.2f}'.format(roc_auc))
    plt.plot([0,1], [0,1], linestyle='--', lw=2, color='#cc3311',
             label='random', alpha=.8)
    plt.grid()
    plt.xlabel('False Positive Rate')
    plt.ylabel('True Positive Rate')
    plt.axis('scaled')
    plt.axis([-0.01, 1.01, -0.01, 1.01])
    plt.show()


if __name__ == "__main__":
    data = pd.read_csv(sys.argv[1], sep='\t')
    similarities = pd.read_csv(sys.argv[2], sep="\\")
    create_roc(data, similarities, sys.argv[3])
