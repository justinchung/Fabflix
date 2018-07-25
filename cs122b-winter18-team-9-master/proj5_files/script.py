import re;
file = []
bleh = []
final = []
def parser(filename):
    with open(filename) as f:
        for line in f:
            x = re.sub('[A-Z]', '', line)
            #n = x.split(" ")
            file.append(x)
    for k in file:
        x = k.strip('\n')
        d = x.split(" ")
        bleh.append(d)
    for i in bleh:
        for x in i:
            final.append(int(x))

    count = 0
    TS = []
    TJ = []
    while (count < len(final)):
        check = count % 2
        if (check == 0):
            TS.append(final[count])
        else:
            TJ.append(final[count])
        count += 1

    print "The average time it takes for the search servlet to run completely for a query is "+ str(sum(TS)/float(len(TS))) + " nanoseconds."
    print "The average time spent on the parts that use JDBC, per query is " + str(sum(TJ)/float(len(TJ))) + " nanoseconds."

if _name_ == "_main_":
    parser("log.txt")
