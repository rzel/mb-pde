// Structural patterns
// Adapter
// 
// client
// target
// adapter
// adaptee

getdb($1)
DP[client,target,adapter,adaptee] = {uses[client,adapter]; inherits[adapter,target]; uses[adapter,adaptee]}

// Remove Java API Libraries
DP_java = DP [&0 =~ "java.*"] 
DP = DP - DP_java
DP_java = DP [&1 =~ "java.*"]
DP = DP - DP_java
DP_java = DP [&2 =~ "java.*"]
DP = DP - DP_java
DP_java = DP [&3 =~ "java.*"]
DP = DP - DP_java

putdb($2,{"DP"})
