// Structural patterns
// Proxy
//
// rs -> realSubject
// s  -> subject
// p  -> proxy

getdb($1)
DP[rs,s,p] = {inherits[rs,s]; inherits[p,s]; uses[p,rs]}

// Remove Java API Libraries
DP_java = DP [&0 =~ "java.*"]
DP = DP - DP_java
DP_java = DP [&1 =~ "java.*"]
DP = DP - DP_java
DP_java = DP [&2 =~ "java.*"]
DP = DP - DP_java

putdb($2,{"DP"})
