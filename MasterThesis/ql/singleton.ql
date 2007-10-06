// Creational patterns
// Singleton
//
// Note: private constructor
//		 returns uniqueinstance
// -> c  = client  
// -> s  = singleton

getdb($1)
DP[c,s] = {uses[c,s]; private[s]}

// Remove Java API Libraries
DP_java = DP [&0 =~ "java.*"]
DP = DP - DP_java
DP_java = DP [&1 =~ "java.*"]
DP = DP - DP_java

putdb($2,{"DP"})
