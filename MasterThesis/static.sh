#!/bin/csh

############################################################
#
# author:  Marcel Birkner
# contact: marcel.birkner@gmail.com
# date:    October 2007
#
# This file is used for the static analysis. It is called 
# from PDE and calls 3 programs that extract static facts
# from the java class file (javex), reduce these facts by
# lifting all facts to class level (grok) and finally compute
# all possible design pattern candidate instances (ql).


############################################################
# Verify input
#
# Precondition:
#  $1 javex 
#  $2 grok
#  $3 ql
#  $4 directory of the class files
#  $5 ql script
#  $6 ql output folder

if ( $#argv < 5 ) then 
    echo ''
    echo 'Error:  Invalid Argument Count'
    echo 'Syntax: $0 '
    echo ''  
    exit
endif 

if ( ! -d $4 ) then 
   echo 'This directory does not exist:   ' $4 
   exit
endif

if ( ! -e $5 ) then 
   echo 'This file does not exist:        ' $5 
   exit
endif

if ( ! -d $6 ) then 
   echo 'This directory does not exist:   ' $6
   mkdir $6
   echo 'Directory created:               ' $6
endif

echo ''
echo 'Arguments passed:' 
echo '-javex      '$1 
echo '-grok       '$2
echo '-ql         '$3 
echo '-directory  '$4
echo '-ql script  '$5 
echo ''


############################################################
# Set file names and clean up files

set javex_in=$4
set javex_out=javex.out 
set grok_out=grok.out
set grok_out_private=grok.private.out
set run_readline=run_readline.sh
set grok_script=lift_to_classlevel.grok
set grok_script_private_constructor=lift_private_constructor.grok
set grok_warning=grok.warning
set ql_out=ql.out
set ql_script=$5

# clean files
rm -rf $javex_out $grok_out $grok_out_private $grok_warning $ql_out
rm -rf $6'/*'


############################################################
# Run javex

echo 'Javex'
set classfiles=`find $javex_in -name '*.class'  | tr '\n' ' '`
$1 -f -l $classfiles > $javex_out
echo 'Javex done'


############################################################
# Run grok

echo 'Grok'
$2 $grok_script $javex_out $grok_out > $grok_warning
$2 $grok_script_private_constructor $javex_out $grok_out_private >> $grok_warning
$run_readline $grok_out_private >> $grok_out
rm -rf $grok_out_private 
echo 'Grok: Remove special characters that cause QL to fail: i.e. [  ] '
sed -e 's/\[//g' < $grok_out > grok.help
sed -e 's/\]//g' < grok.help > grok2.help
rm -rf $grok_out grok.help
mv grok2.help $grok_out
echo 'private x y' >> $grok_out 
echo 'Grok done'


############################################################
# Run ql

echo 'Ql'
$3 $ql_script $grok_out $ql_out
echo 'Ql done'




