#!/bin/csh


# Precondition:
# -pass location of class files as argument $1
# -pass javex output file $2
# -for GoF AJP example compile java files


if ( $#argv < 2 ) then 
    echo ""
    echo "Error:  Invalid Argument Count"
    echo "Syntax: $0 <input_dir> <output_dir> [compile_ajp] "
    echo ""    
    exit
endif 


if ( ! -d $1 ) then 
   echo 'This directory does not exist:   ' $1 
   exit
endif

echo ''
echo 'Arguments passed:   ' 
echo '-directory      '$1 
echo '-project name   '$2
echo '-compile set:   '$3 
echo ''


# set file names and variables
set dir=$1
set out_javex='out_javex_'$2
set out_grok='out_grok_'$2 
set out_ql='out_ql_'$2
set script_grok=lift_to_classlevel.grok
set script_grok_private_constructor=lift_private_constructor.grok
set warning_grok=warning_grok
set warning_javac=warning_javac 
set input_eclipse=input.file.pde.txt


# clean files 
rm -rf $out_javex $out_grok $out_ql $warning_grok $warning_javac $input_eclipse 
rm -rf 'candidateinstances/*'
if ( ! -d candidateinstances ) then
    mkdir candidateinstances
endif
touch $input_eclipse

#echo 'Varibles:  '  
#echo $out_javex 
#echo $out_grok 
#echo $out_ql 
#echo ""


if ( $3 =~ 'compile_ajp' ) then 
    echo ""
    echo "Compile Java files"
    foreach java_dir ( AbstractFactory FactoryMethod adapter bridge builder chain command composite decorator flyweight interpreter iterator mediator memento observer prototype proxy singleton state strategy templatemethod visitor )
       rm -rf 'code/'$java_dir'/*.class'
       set java_files='code/'$java_dir'/*.java' 
       javac $java_files > $warning_javac 
    end
endif 


set classfiles=`find $dir -name '*.class'  | tr '\n' ' '`


echo ""
echo "Run Javex"
javex -f -l $classfiles > $out_javex
echo "Ready"


echo ""
echo "Run Grok"
grok $script_grok $out_javex $out_grok > $warning_grok 
echo "Ready"


echo ""
echo "Remove special characters that cause QL to fail: [ ]"
sed -e 's/\[//g' < $out_grok > grok
sed -e 's/\]//g' < grok > grok2
rm -rf $out_grok grok
mv grok2 $out_grok


echo "Run QL" 
foreach ql_script ( AbstractFactory FactoryMethod adapter bridge builder chain command composite decorator flyweight interpreter iterator mediator memento observer prototype proxy singleton state strategy templatemethod visitor )


    echo ''
    if ( $ql_script =~ 'singleton' ) then
        echo 'Singleton is a special case, due to the private constructor' 
	set out_grok_private = $dir'.private.constructor.out' 
	grok $script_grok_private_constructor $out_javex $out_grok_private
	run_readline.sh $out_grok_private >> $out_grok 
    	rm -rf $out_grok_private 
    endif


    echo 'QL script '$ql_script'.ql'
    set ql_input_script = 'ql/'$ql_script'.ql'
    set ql_input  = $out_grok
    set ql_output = 'candidateinstances/'$2'.'$ql_script'.ql.out.instances'

    foreach directory ( AbstractFactory FactoryMethod adapter bridge builder chain command composite decorator flyweight interpreter iterator mediator memento observer prototype proxy singleton state strategy templatemethod visitor )
        
	set pde_input = $ql_output' dynamicfacts/'$2'.'$directory'.RunPattern.txt dynamicdefinitions/'$2'.'$ql_script'.xml'
        echo $pde_input >> $input_eclipse

    end

    echo "Run QL in loop "
    ql $ql_input_script $ql_input $ql_output


    echo 'Add information of roles to top of ql output'
    rm -rf help_file
    mv $ql_output help_file

    if ($ql_script =~ 'AbstractFactory') then
      echo '// abstractFactory concreteFactory product abstractProduct '> $ql_output
    endif

    if ($ql_script =~ 'FactoryMethod') then
      echo '// creator concreteCreator concreteProduct product ' > $ql_output
    endif

    if ($ql_script =~ 'adapter') then
      echo '// client target adapter adaptee ' > $ql_output
    endif

    if ($ql_script =~ 'bridge') then
      echo '// refinedAbstraction abstraction implementer concreteImplementer ' > $ql_output
    endif

    if ($ql_script =~ 'builder') then
      echo '// director builder concreteBuilder product ' > $ql_output
    endif

    if ($ql_script =~ 'chain') then
      echo '// client handler concreteHandler ' > $ql_output
    endif

    if ($ql_script =~ 'command') then
      echo '// invoker command concreteCommand receiver ' > $ql_output
    endif

    if ($ql_script =~ 'composite') then
      echo '// leaf component composite ' > $ql_output
    endif

    if ($ql_script =~ 'decorator') then
      echo '// concreteComponent component decorator concreteDecorator ' > $ql_output
    endif

    if ($ql_script =~ 'flyweight') then
      echo '// flyweightFactory flyweight concreteFlyweight ' > $ql_output
    endif

    if ($ql_script =~ 'interpreter') then
      echo '// expression abstractExpression context ' > $ql_output
    endif

    if ($ql_script =~ 'iterator') then
      echo '// concreteAggregate iterator concreteIterator ' > $ql_output
    endif

    if ($ql_script =~ 'mediator') then
      echo '// concreteMediator mediator concreteColleague ' > $ql_output
    endif

    if ($ql_script =~ 'memento') then
      echo '// originator memento caretaker ' > $ql_output
    endif

    if ($ql_script =~ 'observer') then
      echo '// subject concreteSubject observer concreteObserver ' > $ql_output
    endif

    if ($ql_script =~ 'prototype') then
      echo '// prototypeInterface prototype client ' > $ql_output
    endif

    if ($ql_script =~ 'proxy') then
      echo '// realSubject subject proxy ' > $ql_output
    endif

    if ($ql_script =~ 'singleton') then
      echo '// client singleton ' > $ql_output
    endif

    if ($ql_script =~ 'state') then
      echo '// context state concreteState ' > $ql_output
    endif

    if ($ql_script =~ 'strategy') then
      echo '// context strategy concreteStrategy ' > $ql_output
    endif

    if ($ql_script =~ 'templatemethod') then
      echo '// abstractClass concreteClass ' > $ql_output
    endif

    if ($ql_script =~ 'visitor') then
      echo '// visitor concreteVisitor element concreteElement ' > $ql_output
    endif

    cat help_file >> $ql_output
    rm -rf help_file

end
 
 
echo "End Run.sh"
