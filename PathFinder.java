import ast
import re
import queue as Q

                    #creating prog for bfs
class BFSProg:
    def __init__(self,x=0,y=0,d=0):
        self.x=x
        self.y=y
        self.d=d

        # checking if z elevation difference<= threshold
    def satisfyconstraint(self, ab, bb, xa, ya):
        if xa >= 0 and xa < h and ya >= 0 and ya < w and ab >= 0 and ab < h and bb >= 0 and bb < w:
            # print("checking constraints a,b,x,y", a, b, x, y)
            if abs(z[ab][bb] - z[xa][ya]) <= maxht:
                # print("satisfying constraint for:",a,b," and",x,y)
                return True
            else:
                # print("not satisfying constraint for:",a, b, "and", x, y)
                return False
        else:
            # print("not satisfying constraint for:",a, b, "and", x, y)
            return False

        # checking if location is still in mars area
    def inMars(self,xb, yb, wb, hb):
        # print("in mars")
        if xb >= 0 and xb < hb and yb >= 0 and yb < wb:
            return True
        else:
            # print("outside mars")
            return False

            # finding min dist to reach target location
    def minDist(self,landxb, landyb, tx, ty, wb, hb):
        change_x = [-1, -1, -1, 0,0, 0, 1, 1, 1]
        change_y = [-1, 0, 1, -1, 0,1, -1, 0, 1]
        graphb=dict()                                #using graph to store in tree format where keys r parents and values are children
        # print("checkpoint1")
        q = []
        visited = [[False for i in range(wb)] for j in range(hb)]
        # print(visited)
        visited[landxb][landyb] = True
        q.append(BFSProg(landxb, landyb, 0))
        visitq=[]
        while (len(q) > 0):  # iterating the queue of visited notes to search for shortest path
            current = q[0]
            gkey=q.pop(0)
            # print("Current:",current)
            # print("gkey:",gkey)
            iB=0
            child=[]
            while iB<9:
            # for a in range(ty-1,ty+2):
            #     for b in range(tx-1,tx+2):
            #             print("checkpoint 5")
                        x = current.x + change_x[iB]
                        y = current.y + change_y[iB]
                        gi=0
                        if -1 < x < h and y>-1 and y<w:
                            if (self.inMars(x, y, w, h) and self.satisfyconstraint(current.x, current.y, x, y) and not visited[x][y]):
                                    visited[x][y] = True
                                    # print('visiting:',x,y)
                                    q.append(BFSProg(x, y, (current.d + 10)))
                                    # print("checkpoint5")
                                    visitq.append([x,y])
                                    # print('\n\nvisitq:',visitq)
                                    # print("current.d:",current.d)
                                    gi=1
                        iB=iB+1
                        if gi==1:
                            child.append([x,y])
                            # print('\nchild:',child)
                            # print('gkey.x,gkey.y:',gkey.x,gkey.y)
                            graphb[(gkey.x,gkey.y)]=child
        # print("\n graph:",graphb)
        return graphb

        #finding and returning the shortest path from source to destination
    def shortestpath(self, graphb, start, end):
        queue = []
        start = ast.literal_eval(start)
        end = ast.literal_eval(end)
        queue.append([start])
        while len(queue) != 0:
            path = queue.pop(0)
            node = path[-1]
            if node != end:
                for adjacent in graphb.get(tuple(node), ()):
                    new_path = list(path)
                    new_path.append(adjacent)
                    queue.append(new_path)
            elif node == end:
                return path


# IMPLEMENTING UCS

class UCSProg:
    def __init__(self,x=0,y=0,d=0):
        self.x=x
        self.y=y
        self.d=d

        # checking if z elevation difference<= threshold
    def satisfyconstraint(self, a, b, x, y,w,h):
            # print("checking constraints a,b,x,y", a, b, x, y)
            if x >= 0 and x < h and y >= 0 and y < w and a >= 0 and a < h and b >= 0 and b < w:
                if abs(z[a][b] - z[x][y]) <= maxht:
                # print("satisfying constraint for:",a,b," and",x,y)
                    return True
                else:
                    # print("not satisfying constraint for:",a, b, "and", x, y)
                    return False
            else:
                # print("not satisfying constraint for:",a, b, "and", x, y)
                return False

    def generateGraph(self,w,h):
        graphu=dict()
        change_x = [-1, -1, -1, 0, 0, 1, 1, 1]
        change_y = [-1, 0, 1, -1, 1, -1, 0, 1]
        for x in range(h):
            for y in range(w):
                neighbour = dict()
                for i in range(8):
                    n=''
                    temp1x=x+change_x[i]
                    temp1y=y+change_y[i]
                    # print(temp1x,temp1y)
                    if -1<temp1x<h and -1<temp1y<w:
                        # if self.inMars(x,y,w,h) and self.satisfyconstraint(temp1x,temp1y,x,y):
                        if self.satisfyconstraint(temp1x, temp1y, x, y, w, h):
                            n=n+str(temp1x)+','+str(temp1y)
                            if  i==0 or i==2 or i==5 or i==7:
                                neighbour[n]=14
                            else:
                                neighbour[n]=10
                            graphu[str(x)+','+str(y)]=neighbour

                    # print(x,y)
        return graphu

    def shortestdist(self, graphu, land1x,land1y, target1x,target1y):
        emptylist=[]
        land=str(land1x)+','+str(land1y)
        target=str(target1x)+','+str(target1y)
        queue = Q.PriorityQueue()
        queue.put((0, land))
        countu=0
        visitedQ=[]
        while not queue.empty():
            node = queue.get()
            # z=node[1]
            if countu==0:
                current = node[1]
            else:
                current = node[1][len(node[1]) - 1]
            # print(current)
            if target in node[1]:
                print("Path found: " + str(node[1]) + ", Cost = " + str(node[0]))
                return node[1]
            else:
                pass
            cost = node[0]

            for neighbor in graphu[current]:
                if not (neighbor in visitedQ):
                    currentstr=current.split(',')
                    neighbourstr=neighbor.split(',')
                    if abs(z[int(currentstr[0])][int(currentstr[1])]-z[int(neighbourstr[0])][int(neighbourstr[1])])>maxht:
                        return emptylist
                    if countu==0:
                        temp=node[1][:].split()
                    else:
                        temp = node[1][:]
                    temp.append(neighbor)
                    visitedQ.append(current)
                    # print('temp:',temp)
                    queue.put((cost + graphu[current][neighbor], temp))
            countu = 1


# IMPLEMENTING A*

class AProg:
    def __init__(self,x=0,y=0,d=0):
        self.x=x
        self.y=y
        self.d=d

        # checking if z elevation difference<= threshold
    def satisfyconstraint(self, aua, bub, xux, yuy,wuw,huh):
            # print("checking constraints a,b,x,y", a, b, x, y)
            if xux >= 0 and xux < huh and yuy >= 0 and yuy < wuw and aua >= 0 and aua < huh and bub >= 0 and bub < wuw:
                if abs(z[aua][bub] - z[xux][yuy]) <= maxht:
                    # print("satisfying constraint for:",a,b," and",x,y)
                    return True
                else:
                    # print("not satisfying constraint for:",a, b, "and", x, y)
                    return False
            else:
                # print("not satisfying constraint for:",a, b, "and", x, y)
                return False

    def generateGraph(self,w,h):
        grapha=dict()
        change_x = [-1, -1, -1, 0, 0, 1, 1, 1]
        change_y = [-1, 0, 1, -1, 1, -1, 0, 1]
        for x in range(h):
            for y in range(w):
                neighbour = dict()
                for i in range(8):
                    n=''
                    temp1x=x+change_x[i]
                    temp1y=y+change_y[i]
                    # print(temp1x,temp1y)
                    if -1<temp1x<h and -1<temp1y<w:
                        # if self.inMars(x,y,w,h) and self.satisfyconstraint(temp1x,temp1y,x,y,w,h):
                        if self.satisfyconstraint(temp1x, temp1y, x, y, w, h):
                            n=n+str(temp1x)+','+str(temp1y)
                            if  i==0 or i==2 or i==5 or i==7:
                                neighbour[n]=14
                            else:
                                neighbour[n]=10
                            grapha[str(x)+','+str(y)]=neighbour

                    # print(x,y)
        return grapha

    def ashortestdist(self, grapha, land1x,land1y, targetax,targetay):
        emptylist=[]
        land=str(land1x)+','+str(land1y)
        target=str(targetax)+','+str(targetay)
        queue = Q.PriorityQueue()
        queue.put((0, land))
        count=0
        visitingQ=[]
        while not queue.empty():
            node = queue.get()
            # z=node[1]
            if count==0:
                current = node[1]
            else:
                current = node[1][len(node[1]) - 1]
            # print(current)
            if target in node[1]:
                print("Path found: " + str(node[1]) + ", Cost = " + str(node[0]))
                return node[1]
            else:
                pass
            cost = node[0]

            for neighbor in grapha[current]:
                if not (neighbor in visitingQ):
                    currentstr = current.split(',')
                    neighbourstr = neighbor.split(',')
                    if abs(z[int(currentstr[0])][int(currentstr[1])] - z[int(neighbourstr[0])][int(neighbourstr[1])])>maxht:
                        return emptylist
                    if count==0:
                        temp=node[1][:].split()
                    else:
                        temp = node[1][:]
                    temp.append(neighbor)
                    visitingQ.append(current)
                    # print('temp:',temp)
                    a = int(current[0])
                    b = int(current[2])
                    ux = int(neighbor[0])
                    uy = int(neighbor[2])
                    # print(z[a][b])
                    # print(z[ux][uy])
                    h=min(abs(ux-targetax),abs(uy-targetay),abs(z[ux][uy]-z[targetax][targetay]))
                    absz=abs(z[a][b] - z[ux][uy])
                    queue.put((cost + grapha[current][neighbor]+absz+h, temp))
            count = 1


#                           main function starts here
inputerror=0
# def inputerror():
#     outfile.write("FAIL\n")
#     print("closing IO files due to input error")
#     return 1

                                  #opening input&output files
infile=open("input.txt","r")
outfile=open("output.txt","w")    #write output as outfile.writelines(L) else str="FAIL" ...outfile.write(str)

                                    # ReadING INPUT.TXT
print("Reading algorithm:\n")
algo=infile.readline()
algo=algo.split()
# print(algo)
if len(algo)>1:
   inputerror=1
algo=algo[0]
if algo not in ["BFS","UCS","A*"]:
    print("Algo not listed")
    inputerror=1
print("algorithm to be used is ",algo)


print("\n reading w & H values")
wh=infile.readline().split()
if any(char.isalpha() for string in wh for char in string):
    inputerror=1
if len(wh)>2 or len(wh)<2:
    inputerror=1
w=int(wh[0])
h=int(wh[1])
if w<0 or h<0:
    inputerror=1
print(w,h)

print("\n reading landing site x y values")
xy=infile.readline().split()
if any(char.isalpha() for string in xy for char in string):
    inputerror=1
if len(xy)>2 or len(xy)<2:
    inputerror=1
landx=int(xy[0])
landy=int(xy[1])
# temp=landx
# landx=landy
# landy=temp
print(landx,landy)
if not (0<=landx<=w-1 or 0<=landy<=h-1):        #checking if landing is in mars area
    inputerror=1

print("\n reading threshold value")
maxht=infile.readline().split()
if any(char.isalpha() for string in maxht for char in string):
    inputerror=1
if len(maxht)>1 or len(maxht)<1:
    inputerror=1
maxht=int(maxht[0])
print(maxht)

print("\nreading no of target sites")
n=infile.readline().split()
if any(char.isalpha() for string in n for char in string):
    inputerror=1
if len(n)>1 or len(n)<1:
    inputerror=1
n=int(n[0])
print(n)

print("\nreading target sites location x1 y1... values")
xy=[]
for i in range(n):
    str1=infile.readline().split()
    if any(char.isalpha() for string in str1 for char in string):
        inputerror=1
    if len(str1)>2 or len(str1)<2:
        inputerror=1
    int_str1=[int(x) for x in str1]
    # print(int_str1[0])
    xy.append(int_str1)
for i in range(n):
    tempxy=xy[i][0]
    xy[i][0]=xy[i][1]
    xy[i][1]=tempxy
# print(xy)

print("\nreading z elevation values")
i=0
z=[]
for i in range(h):
    str1=infile.readline().split()
    if any(char.isalpha() for string in str1 for char in string):
        inputerror=1
    if len(str1)>w or len(str1)<w:
        inputerror=1
    int_str1=[int(x) for x in str1]
    z.append(int_str1)
print("z is")
print(z)

print("checking if eof reached or excess data given")
if len(infile.read()):
    print("more data")
    inputerror=1


path=""
temp=landx
landx=landy
landy=temp

                                      #RUNNING ALGORITHMS


if algo=="BFS":
    print("running bfs")
    i=0
    count=0
    graphSB=dict()
    for i in range(n):
        path=''
        if inputerror==1:
            path='FAIL'
            break
        for y in range(0,1):        #extra loop added for making usage of break easier
            # print("Destination",xy[i][0],xy[i][1])

            # checking if landing site &target are same
            TequalsD=False
            if landx==xy[i][0] and landy==xy[i][1]:
                path=path+str(landx)+','+str(landy)
                TequalsD=True
                # outfile.write(path)
                print(path)
                print("target destination same so break")
                break

            #checking if site is reachable from atleast 1 of its 8 immediate neighbours and if target is in mars
            def isreachable(i,xy,z):
                if not (xy[i][0]>=0 and xy[i][1]>=0 and xy[i][0]<h and xy[i][1]<w):
                    print("target out of mars")
                    return False
                reachable1=True
                for p in range(xy[i][0]-1,xy[i][0]+2):
                    for q in range(xy[i][1]-1,xy[i][1]+2):
                        if p>=0 and p<h and q>=0 and q<w:
                            # print('location',p,q,'  destination:',xy[i][0],xy[i][1])
                            destx=xy[i][0]
                            desty=xy[i][1]
                            # print(' current z:',z[p][q])
                            # print('destination z:',z[destx][desty])
                            reachable1 = False
                            if (q!=xy[i][1] or p!=xy[i][0]) and abs(z[p][q]-z[xy[i][0]][xy[i][1]])<=maxht:
                                reachable1=True
                                # print("reachable from atleast 1 neighbour:",reachable1)
                                return reachable1
                # print(reachable1)
                return reachable1
            if not TequalsD:
                reachable=isreachable(i,xy,z)
                print("reachable from atleast 1 neighbour:",reachable)
                if not reachable:
                    path=path+'FAIL'
                    # print(path)
                    break
                else:
                    #program to run bfs
                    print("\nRunning bfs after all checks")
                    b=BFSProg()
                    if count==0:
                        graphSB=b.minDist(landx,landy,xy[i][0],xy[i][1],w,h)
                        count=1
                    print(graphSB)
                    print("\nShortest path is: ")
                    x=b.shortestpath(graphSB,str([landx,landy]),str([xy[i][0],xy[i][1]]))
                    # print(x)
                    if x==None:
                        path='FAIL'
                    else:
                        for pos in range(0, len(x)):
                            path = path + str(x[pos][0]) + "," + str(x[pos][1]) + " "
                            # print(path)
        print("finished processing",i,"th target")
        if path!='FAIL':
            fpath = re.split(',| ', path)
            # print(path)
            # print(fpath)
            # print(fpath[0])
            path = ''
            for fp in range(0, len(fpath)-1, 2):
                # print p
                tp = fpath[fp]
                fpath[fp] = fpath[fp + 1]
                fpath[fp + 1] = tp
                # print(fpath)
                path = path + fpath[fp] + ',' + fpath[fp + 1] + ' '
        path=path+'\n'
        print(path)
        outfile.writelines(path)



elif algo=="UCS":
    print("running ucs")
    i=0
    ucount=0
    ugraph = dict()
    for i in range(n):
        path=''
        if inputerror==1:
            path='FAIL'
            break
        for y in range(0,1):        #extra loop added for making usage of break easier
            # print("Destination",xy[i][0],xy[i][1])

            # checking if landing site &target are same
            TequalsD=False
            if landx==xy[i][0] and landy==xy[i][1]:
                path=path+str(landx)+','+str(landy)
                TequalsD=True
                # outfile.write(path)
                print(path)
                print("target destination same so break")
                break

            #checking if site is reachable from atleast 1 of its 8 immediate neighbours and if target is in mars
            def isreachable(i,xy,z):
                if not (xy[i][0]>=0 and xy[i][1]>=0 and xy[i][0]<h and xy[i][1]<w):
                    print("target out of mars")
                    return False
                reachable1 = True
                for p in range(xy[i][0]-1,xy[i][0]+2):
                    for q in range(xy[i][1]-1,xy[i][1]+2):
                        if p>=0 and p<h and q>=0 and q<w:
                            # print('location',p,q,'  destination:',xy[i][0],xy[i][1])
                            destx=xy[i][0]
                            desty=xy[i][1]
                            # print(' current z:',z[p][q])
                            # print('destination z:',z[destx][desty])
                            reachable1 = False
                            if (q!=xy[i][1] or p!=xy[i][0]) and abs(z[p][q]-z[xy[i][0]][xy[i][1]])<=maxht:
                                reachable1=True
                                # print("reachable from atleast 1 neighbour:",reachable1)
                                return reachable1
                # print(reachable)
                return reachable1
            if not TequalsD:
                reachable=isreachable(i,xy,z)
                print("reachable from atleast 1 neighbour:",reachable)
                if not reachable:
                    path=path+'FAIL'
                    # print(path)
                    break
                else:
                    #program to run ucs
                    print("\nRunning ucs after all checks")
                    u=UCSProg()
                    if ucount==0:
                        ugraph=u.generateGraph(w,h)
                        print(ugraph)
                        ucount=1
                    print("\nShortest path is: ")
                    # print('ugraph:',ugraph)
                    upath=u.shortestdist(ugraph,landx,landy,xy[i][0],xy[i][1])
                    # print('upath:',upath,type(upath))
                    if upath==None:
                        path='FAIL'
                    else:
                        for ii in range(len(upath)):
                            path=path+upath[ii]+' '
        print("finished processing",i,"th target")
        if path != 'FAIL':
            fpath = re.split(',| ', path)
            # print(path)
            # print(fpath)
            # print(fpath[0])
            path = ''
            for fp in range(0, len(fpath)-1, 2):
                # print p
                tp = fpath[fp]
                fpath[fp] = fpath[fp + 1]
                fpath[fp + 1] = tp
                # print(fpath)
                path = path + fpath[fp] + ',' + fpath[fp + 1] + ' '
        path=path+'\n'
        print('Path:',path)
        outfile.write(path)



elif algo=="A*":
    print("running A*")
    # ui=0
    acount=0
    agraph=dict()
    for i in range(n):
        path=''
        if inputerror==1:
            path='FAIL'
            break
        for y in range(0,1):        #extra loop added for making usage of break easier
            # print("Destination",xy[i][0],xy[i][1])

            # checking if landing site &target are same
            TequalsD=False
            if landx==xy[i][0] and landy==xy[i][1]:
                path=path+str(landx)+','+str(landy)
                TequalsD=True
                # outfile.write(path)
                print(path)
                print("target destination same so break")
                break

            #checking if site is reachable from atleast 1 of its 8 immediate neighbours and if target is in mars
            def isreachable(i,xy,zzz):
                if not (xy[i][0]>=0 and xy[i][1]>=0 and xy[i][0]<h and xy[i][1]<w):
                    print("target out of mars")
                    return False
                reachable1 = True
                for p in range(xy[i][0]-1,xy[i][0]+2):
                    for q in range(xy[i][1]-1,xy[i][1]+2):
                        if p>=0 and p<h and q>=0 and q<w:
                            # print('location',p,q,'  destination:',xy[i][0],xy[i][1])
                            # destx=xy[i][0]
                            # desty=xy[i][1]
                            # print(' current z:',zzz[p][q])
                            # print('destination z:',zzz[destx][desty])
                            reachable1 = False
                            if (q!=xy[i][1] or p!=xy[i][0]) and abs(zzz[p][q]-zzz[xy[i][0]][xy[i][1]])<=maxht:
                                reachable1=True
                                # print("reachable from atleast 1 neighbour:",reachable1)
                                return reachable1
                # print(reachable)
                return reachable1
            if not TequalsD:
                reachable=isreachable(i,xy,z)
                print("reachable from atleast 1 neighbour:",reachable)
                if not reachable:
                    path=path+'FAIL'
                    # print(path)
                    break
                else:
                    #program to run ucs
                    print("\nRunning A* after all checks")
                    aprog=AProg()
                    if acount==0:
                        agraph=aprog.generateGraph(w,h)
                        # print(agraph)
                        acount=1
                    print("\nShortest path is: ")
                    apath=aprog.ashortestdist(agraph,landx,landy,xy[i][0],xy[i][1])
                    # print('apath:',apath,type(apath))
                    if apath==None:
                        path='FAIL'
                    else:
                        for ai in range(len(apath)):
                            path=path+apath[ai]+' '
        print("finished processing",i,"th target")
        if path != 'FAIL':
            fpath = re.split(',| ', path)
            # print(path)
            # print(fpath)
            # print(fpath[0])
            path = ''
            for fp in range(0, len(fpath)-1, 2):
                # print p
                tp = fpath[fp]
                fpath[fp] = fpath[fp + 1]
                fpath[fp + 1] = tp
                # print(fpath)
                path = path + fpath[fp] + ',' + fpath[fp + 1] + ' '
        path=path+'\n'
        print('Path:',path)
        outfile.write(path)

else:
    outfile.write("FAIL")

print("closing IO files at end")
infile.close()
outfile.close()

