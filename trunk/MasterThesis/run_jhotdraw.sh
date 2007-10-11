#!/bin/csh

echo "Start javac and javex for fact extraction"
echo "Start grok and ql processing to detect candidate instances"

echo
echo "_______________________________________________________________"
echo

set classfiles=`find jhotdraw/JHotDraw_7.0.8 -name '*.class'  | tr '\n' ' '`
set javafiles=`find jhotdraw/JHotDraw_7.0.8 -name '*.java'  | tr '\n' ' '`
set javex_out=jHotDraw.javex.out
set grok_out=jHotDraw.grok.out
set grok_out1=jHotDraw.grok.out1
set grok_out2=jHotDraw.grok.out2
set grok_script=lift_to_classlevel.grok
set ql_out=jHotDraw.ql.out
set pinot_out=jHotDraw.pinot.out

rm -rf $javex_out $pinot_out $grok_out $ql_out $grok_out1 $grok_out2 

date
echo "Run Javex"
javex -f -l $classfiles > $javex_out 
echo "Done with jHotDraw"

date
echo "Run Pinot"
pinot -Xstdout $javafiles > $pinot_out

date
echo "Run Grok"
grok $grok_script $javex_out $grok_out

echo "Remove special characters that cause QL to fail: [ ]"    
sed -e 's/\[//g' < $grok_out > $grok_out1
sed -e 's/\]//g' < $grok_out1 > $grok_out2
rm -rf $grok_out
mv $grok_out2 $grok_out

echo "Run QL" 
date 
echo "QL Adapter"
ql ql/adapter.ql $grok_out jHotDraw.ql.adapter.instances  
date
echo "QL Command"
ql ql/command.ql $grok_out jHotDraw.ql.command.instances 
date
echo "QL Composite"
ql ql/composite.ql $grok_out jHotDraw.ql.composite.instances
date
 
