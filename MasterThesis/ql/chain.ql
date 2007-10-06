// Behavioural patterns
// Chain of Responsibiltity
// 
// c  -> client 
// h  -> handler 
// ch -> concreteHandler

getdb($1)
DP[c,h,ch] = {uses[c,h]; inherits[ch,h]; uses[ch,h]}

// Remove Java API Libraries
DP_java = DP [&0 =~ "java.*"]
DP = DP - DP_java
DP_java = DP [&1 =~ "java.*"]
DP = DP - DP_java
DP_java = DP [&2 =~ "java.*"]
DP = DP - DP_java

putdb($2,{"DP"})
