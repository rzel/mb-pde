// Structural patterns
// Bridge
//
// rfAbs  -> refinedAbstraction
// abs    -> abstraction
// imp    -> implementer
// conImp -> concreteImplementer

getdb($1)
DP[rfAbs,abs,imp,conImp] = {inherits[rfAbs,abs]; uses[abs,imp]; inherits[conImp,imp]}

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
