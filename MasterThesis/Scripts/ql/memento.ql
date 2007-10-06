// Behavioural patterns
// Memento
//
// ori -> originator
// m   -> memento
// ct  -> careTaker

getdb($1)
DP[ori,m,ct] = {uses[ct,ori]; uses[ct,m]}

// Remove Java API Libraries
DP_java = DP [&0 =~ "java.*"]
DP = DP - DP_java
DP_java = DP [&1 =~ "java.*"]
DP = DP - DP_java
DP_java = DP [&2 =~ "java.*"]
DP = DP - DP_java

putdb($2,{"DP"})
