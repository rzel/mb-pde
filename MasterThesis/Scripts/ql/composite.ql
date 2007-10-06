// Structural patterns
// Composite
//
// l  -> leaf
// c  -> component
// cp -> composite

getdb($1)
DP[l,c,cp] = {inherits[l,c]; inherits[cp,c]; uses[cp,c]}

// Remove Java API Libraries
DP_java = DP [&0 =~ "java.*"]
DP = DP - DP_java
DP_java = DP [&1 =~ "java.*"]
DP = DP - DP_java
DP_java = DP [&2 =~ "java.*"]
DP = DP - DP_java

putdb($2,{"DP"})
