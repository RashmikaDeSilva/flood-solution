import pandas as pd
from math import sin, cos, sqrt, atan2, radians

def distance(la1, lo1, la2, lo2):
    # approximate radius of earth in km
    R = 6373.0

    lat1 = radians(la1)
    lon1 = radians(lo1)
    lat2 = radians(la2)
    lon2 = radians(lo2)

    dlon = lon2 - lon1
    dlat = lat2 - lat1

    a = sin(dlat / 2)**2 + cos(lat1) * cos(lat2) * sin(dlon / 2)**2
    c = 2 * atan2(sqrt(a), sqrt(1 - a))

    distance = R * c

    return(distance)

df = pd.read_csv(r'data.csv')
#print(df)

dataArray = []
adjMat = []

for row in df.itertuples():
    #print(row)
    dataArray.append([row.Lat, row.Lon, list(map(int, str(row.Con).split(',')))])

#print the point list
print("{")
for i in dataArray:
    print("{", i[0], ", ", i[1], "}, ")

print("};\n\n")

#printing the adjecent Matrix

#initiating the matrix with all 0
for i in range(len(dataArray)):
    tmp = []
    for j in range(len(dataArray)):
        if (i == j):
            tmp.append(0.0000000000000001)
        else:
            tmp.append(0)
    adjMat.append(tmp)

#create the adj matrix
for i in range(len(dataArray)):
    for j in range(len(dataArray[i][2])):
        #print(adjMat[i][dataArray[i][2][j]])
        dis = distance(dataArray[i][0], dataArray[i][1], dataArray[dataArray[i][2][j]][0], dataArray[dataArray[i][2][j]][1])
        adjMat[i][dataArray[i][2][j]] = dis
        adjMat[dataArray[i][2][j]][i] = dis

print("{") 
for i in adjMat:
    print("{", end='')
    for j in i:
        print(j, end = ', ')
    print("},")
print("};")
