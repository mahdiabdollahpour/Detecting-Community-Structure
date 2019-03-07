#Detecting community structure 
## using label propagation with weighted coherent neighborhood propinquity

##a b s t r a c t


##### Community detection has become an important methodology to understand the organization and function of various real-world networks. The label propagation algorithm (LPA) is an almost linear time algorithm proved to be effective in finding a good community structure. However, LPA has a limitation caused by its one-hop horizon. Specifically, each node in LPA adopts the label shared by most of its one-hop neighbors; much network topology information is lost in this process, which we believe is one of the main reasons for its instability and poor performance. Therefore in this paper we introduce a measure named weighted coherent neighborhood propinquity (weighted-CNP) to represent the probability that a pair of vertices are involved in the same community. In label update, a node adopts the label that has the maximum weighted-CNP instead of the one that is shared by most of its neighbors. We propose a dynamic and adaptive weighted-CNP called entropic-CNP by using the principal of entropy to modulate the weights. Furthermore, we propose a framework to integrate the weighted-CNP in other algorithms in detecting community structure. We test our algorithm on both computer-generated networks and real-world networks. The experimental results show that our algorithm is more robust and effective than LPA in large-scale networks.
###### Hao Lou, Shenghong Li , Yuxin Zhao
###### School of Electronic Information and Electrical Engineering, Shanghai Jiaotong University, Shanghai 200240, China

[Link to Paper](https://www.sciencedirect.com/science/article/pii/S0378437113002173)