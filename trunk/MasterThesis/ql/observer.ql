// Behavioural patterns
// Observer
//
// cs -> concreteSubject
// s  -> subject
// ob -> observer
// co -> concreteObserver

getdb($1)
DP[s,cs,ob,co] = {inherits[co,ob]; uses[co,cs]; uses[s,ob]} 

// Remove Java API Libraries
DP_java = DP [&0 =~ "java.lang.*"]
DP = DP - DP_java
DP_java = DP [&1 =~ "java.lang.*"]
DP = DP - DP_java
DP_java = DP [&2 =~ "java.lang.*"]
DP = DP - DP_java
DP_java = DP [&3 =~ "java.lang.*"]
DP = DP - DP_java

// Remove Java API Libraries
DP_java = DP [&0 =~ "java.awt.*"]
DP = DP - DP_java
DP_java = DP [&1 =~ "java.awt.*"]
DP = DP - DP_java
DP_java = DP [&2 =~ "java.awt.*"]
DP = DP - DP_java
DP_java = DP [&3 =~ "java.awt.*"]
DP = DP - DP_java

// Remove Java API Libraries
DP_java = DP [&0 =~ "java.util.*"]
DP = DP - DP_java
DP_java = DP [&1 =~ "java.util.*"]
DP = DP - DP_java
DP_java = DP [&2 =~ "java.util.*"]
DP = DP - DP_java
DP_java = DP [&3 =~ "java.util.*"]
DP = DP - DP_java

// Remove Java API Libraries
DP_java = DP [&0 =~ "javax.swing.*"]
DP = DP - DP_java
DP_java = DP [&1 =~ "javax.swing.*"]
DP = DP - DP_java
DP_java = DP [&2 =~ "javax.swing.*"]
DP = DP - DP_java
DP_java = DP [&3 =~ "javax.swing.*"]
DP = DP - DP_java

putdb($2,{"DP"})
