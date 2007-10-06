// Structural patterns
// Decorator
//
// cc -> concreteComponent
// c  -> component
// d  -> decorator
// cd -> concreteDecorator

getdb($1)
DP[cc,c,d,cd] = {inherits[cc,c]; inherits[d,c]; uses[d,c]; inherits[cd,d]}

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
