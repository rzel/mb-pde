// Behavioural patterns
// Visitor
//
// cv -> concreteVisitor
// v  -> visitor
// e  -> element
// ce -> concreteElement

getdb($1)
DP[v,cv,e,ce] = {inherits[cv,v];inherits[ce,e];uses[e,v]}

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
