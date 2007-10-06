// Behavioural patterns
// Iterator
//
// it  -> Iterator
// ca  -> concreteAggregate
// cit -> concreteIterator

getdb($1)
DP[ca,it,cit] = {uses[ca,it]; inherits[cit,it]}

// Remove Java API Libraries
DP_java = DP [&0 =~ "java.*"]
DP = DP - DP_java
DP_java = DP [&1 =~ "java.*"]
DP = DP - DP_java
DP_java = DP [&2 =~ "java.*"]
DP = DP - DP_java

putdb($2,{"DP"})
