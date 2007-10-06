// Behavioural patterns
// State
//
// c -> context
// s -> state
// cs -> concreteState

getdb($1)
DP[c,s,cs] = {uses[c,s]; inherits[cs,s]}

// Remove Java API Libraries
DP_java = DP [&0 =~ "java.*"]
DP = DP - DP_java
DP_java = DP [&1 =~ "java.*"]
DP = DP - DP_java
DP_java = DP [&2 =~ "java.*"]
DP = DP - DP_java

putdb($2,{"DP"})
