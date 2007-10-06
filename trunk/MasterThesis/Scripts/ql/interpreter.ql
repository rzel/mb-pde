// Behavioural patterns
// Interpreter
//
// ee -> expression
// ae -> abstractExpression
// c  -> context

getdb($1)
DP[ee,ae,c] = {inherits[ee,ae]; uses[ee,c]}

// Remove Java API Libraries
DP_java = DP [&0 =~ "java.*"]
DP = DP - DP_java
DP_java = DP [&1 =~ "java.*"]
DP = DP - DP_java
DP_java = DP [&2 =~ "java.*"]
DP = DP - DP_java



putdb($2,{"DP"})
