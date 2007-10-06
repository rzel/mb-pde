// Behavioural patterns
// Template Method
//
// ac = abstractClass 
// cc = concreteClass

getdb($1)
DP[ac,cc] = {inherits[cc,ac]; uses[cc,ac]}

// Remove Java API Libraries
DP_java = DP [&0 =~ "java.*"]
DP = DP - DP_java
DP_java = DP [&1 =~ "java.*"]
DP = DP - DP_java

putdb($2,{"DP"})
 
