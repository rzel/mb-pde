// Behavioural patterns
// Command
//
// ivk    -> invoker
// cmd    -> command
// conCmd -> concreteCommand
// re     -> receiver

getdb($1)
DP[ivk,cmd,conCmd,re] = {uses[ivk,cmd]; inherits[conCmd,cmd]; uses[conCmd,re]}

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
