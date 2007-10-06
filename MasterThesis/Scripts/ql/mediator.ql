// Behavioural patterns
// Mediator
//
// cm -> concreteMediator
// m  -> mediator
// c  -> colleague
// cc -> concreteColleague

getdb($1)
DP[cm,m,cc] = {inherits[cm,m]; uses[cc,m]; uses[cm,cc]}

// Remove Java API Libraries
DP_java = DP [&0 =~ "java.*"]
DP = DP - DP_java
DP_java = DP [&1 =~ "java.*"]
DP = DP - DP_java
DP_java = DP [&2 =~ "java.*"]
DP = DP - DP_java


putdb($2,{"DP"})
