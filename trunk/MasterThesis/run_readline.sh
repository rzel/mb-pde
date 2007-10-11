#!/bin/bash

# User define Function (UDF)
processLine(){
  line="$@" # get all args
  echo 'private '$line' '$line  
}

### Main script stars here ###
# Store file name
FILE=""

# Make sure we get file name as command line argument 
# Else read it from standard input device
if [ "$1" == "" ]; then
   FILE="/dev/stdin"
else
   FILE="$1"	
   # make sure file exist and readable
   if [ ! -f $FILE ]; then 
  	echo "$FILE : does not exists"
  	exit 1
   elif [ ! -r $FILE ]; then
  	echo "$FILE: can not read"
  	exit 2
   fi
fi
# read $FILE using the file descriptors
exec 3<&0
exec 0<$FILE
while read line
do
	# use $line variable to process line in processLine() function
	processLine $line
done
exec 0<&3

exit 0

