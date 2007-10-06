// Creational patterns
// Factory Method
//
// c    -> creator
// conC -> concreteCreator
// conP -> concreteProduct
// p    -> product

getdb($1)
DP[c,conC,conP,p] = {inherits[conC,c]; uses[conC,conP]; inherits[conP,p]}

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
