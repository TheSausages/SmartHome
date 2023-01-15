import numpy as np
import matplotlib.pyplot as plt
import csv
from datetime import datetime
import math

def draw_plots(x, y, names, labels, data_in_x=False):
    plt.clf()
    xpoints = np.array(x)
    ypoints = np.array(y)
    if data_in_x:
        plt.plot_date(xpoints, ypoints, label=labels, linewidth=3, xdate=data_in_x)
    else:
        plt.plot(xpoints, ypoints, label=labels, linewidth=3)
    plt.xlabel(names[0])
    plt.ylabel(names[1])
    plt.legend()
    plt.tight_layout()
    plt.show()
    #plt.savefig("plots/" + labels+'.png')

def handle_data_service_metrics(path):
    names = []
    number_of_requests = []
    data = []
    with open(path, newline='') as csvfile:
        spamreader = list(csv.reader(csvfile, delimiter=','))
        names = spamreader[0]
        for row in spamreader[1:]:
            number_of_requests.append(row[0])
            data.append(row[1:])

    for i, name in enumerate(names[1:]):
        new_data = []
        for el in data:
            new_data.append(el[i])
        draw_plots(number_of_requests, new_data, ['number_of_requests', name], name)

def handle_history_times(path):
    names = []
    from_date = []
    dataServiceDifference = []
    renderDifference = []
    fmt = '%Y-%m-%dT%H:%M:%S.%f'
    with open(path, newline='') as csvfile:
        spamreader = list(csv.reader(csvfile, delimiter=','))
        names = spamreader[0]
        for row in spamreader[1:]:
            from_date.append(row[0])
            dataServiceDifference.append(float(row[3]))
            t1 = datetime.strptime(row[4], fmt)
            t2 = datetime.strptime(row[5], fmt)
            renderDifference.append((t2 - t1).total_seconds())

    draw_plots(from_date, dataServiceDifference, ['Zakres początkowy danych', names[3]], names[3], True)
    draw_plots(from_date, renderDifference, ['Zakres początkowy danych', names[6]], names[6], True)

def draw_bar_plot(x, names, labels):
    plt.clf()
    y_pos = np.arange(len(x))
    plt.figure(figsize=(10,5))
    plt.ylim(0, math.ceil(max(x)))
    plt.bar(y_pos, x)
    plt.xticks(y_pos, names)
    plt.xlabel(labels[0])
    plt.ylabel(labels[1])
    plt.legend()
    plt.tight_layout()
    plt.show()

def main_page_times(path):
    names = []
    data = []
    with open(path, newline='') as csvfile:
        spamreader = list(csv.reader(csvfile, delimiter=','))
        names = spamreader[0]
        for row in spamreader[1:]:
            data.append([float(i) for i in row])
    draw_bar_plot(data[0], names, ['Metryka', 'Czas w milisekundach'])


#handle_history_times('history-times.csv')
#handle_data_service_metrics('data-service-metrics.csv')