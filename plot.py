import matplotlib
import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns

print matplotlib.style.available
matplotlib.style.use('classic')

plot_style = {
    'xtick.direction': 'out',
    'xtick.major.size': 5.0,
    'ytick.direction': 'out',
    'ytick.major.size': 3.0
}

sns.set_style('white', plot_style)
f, (ax1, ax2, ax3) = plt.subplots(3, figsize=(12, 14))
data = pd.read_csv("./results.csv")
columns = data.Benchmark.str.split(".")

for i in xrange(len(columns)):
    columns[i][4] = columns[i][4][:-5].capitalize()

datasets = pd.DataFrame(columns.tolist(), columns=['a', 'b', 'c', 'DataSet', "Library"])
df = pd.DataFrame(
    {
        'score': data.Score,
        'error': data['Score Error (99.9%)'],
        'dataset': datasets.DataSet,
        'library': datasets.Library
    }
)

companies = df[df.dataset == 'Companies'].sort_values('score', ascending=False)
if len(companies):
    companies.plot.barh(xerr='error', ax=ax1, color='orange', legend=False)
    ax1.set(xlim=(0, max(df.score) + companies.score.median()), ylabel='', xlabel='', title='75mb File')
    ax1.set_yticklabels(companies.library)

bla = df[df.dataset == 'Bla25Bench'].sort_values('score', ascending=False)
if len(bla):
    bla.plot.barh(xerr='error', ax=ax3, color='orange', legend=False)
    ax3.set(xlim=(0, max(bla.score) + bla.score.median()), ylabel='', xlabel='Avg Runtime in Milliseconds (Lower is better)', title='1mb File')
    ax3.set_yticklabels(bla.library)

ugh = df[df.dataset == 'Ugh10kBench'].sort_values('score', ascending=False)
if len(ugh):
    ugh.plot.barh(xerr='error', ax=ax2, color='orange', legend=False)
    ax2.set(xlim=(0, max(ugh.score) + ugh.score.median()), ylabel='', xlabel='', title='4mb File')
    ax2.set_yticklabels(ugh.library)

sns.plt.tight_layout()
sns.despine()
sns.plt.show()
