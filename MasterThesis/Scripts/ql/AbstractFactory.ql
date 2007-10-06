// Creational patterns
// Abstract Factory
//
//	absFact -> abstractFactory
//	conFact -> concreteFactory
//	p       -> product
//	absP    -> abstractProduct

getdb($1)
DP[absFact,conFact,p,absP] = {inherits[conFact,absFact]; uses[conFact,p]; inherits[p,absP]}

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


