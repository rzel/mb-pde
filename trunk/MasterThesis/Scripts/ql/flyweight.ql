// Structural patterns
// Flyweight
//
// ff -> flyweightFactory
// f  -> flyweight
// cf -> concreteFlyweight

getdb($1)
DP[ff,f,cf] = {inherits[cf,f]; uses[ff,cf]; uses[ff,f]}

// Remove Java API Libraries
DP_java = DP [&0 =~ "java.*"]
DP = DP - DP_java
DP_java = DP [&1 =~ "java.*"]
DP = DP - DP_java
DP_java = DP [&2 =~ "java.*"]
DP = DP - DP_java


putdb($2,{"DP"})
