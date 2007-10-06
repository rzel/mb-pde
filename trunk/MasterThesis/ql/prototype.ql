// Creational patterns
// Prototype
//
// pi -> prototypeInterface 
// pr -> prototype
// c  -> client

getdb($1)
DP[pi,pr,c] = {uses[c,pr]; inherits[pr,pi]} 

// Remove Java API Libraries
DP_java = DP [&0 =~ "java.*"]
DP = DP - DP_java
DP_java = DP [&1 =~ "java.*"]
DP = DP - DP_java
DP_java = DP [&2 =~ "java.*"]
DP = DP - DP_java

putdb($2,{"DP"})

