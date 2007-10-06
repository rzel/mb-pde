// Creational patterns
// Builder
// 
// dir    -> director
// bld    -> builder
// conBld -> concreteBuilder
// prod   -> product

getdb($1)
DP[dir,bld,conBld,prod] = {uses[dir,bld]; inherits[conBld,bld]; uses[conBld,prod]}

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
