#!/bin/sh

# Author: Marcel Birkner
# Date:   January 2008

cd /cs/research/bil/Marcel/dpd4j/mb-pde/MasterThesis
tar xvfz jdraw.src.tar.gz
set javafiles=`find jdomain/ -name '*.java'  | tr '\n' ' '`
pinot -Xstdout -classpath /cs/local/packages/jdk1.5.0_10/jre/lib/rt.jar:. $javafiles > pinot.results.4.jdraw.out

