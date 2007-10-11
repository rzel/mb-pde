#!/bin/csh

echo "Start javac and javex for fact extraction"
echo "Start grok and ql processing to detect candidate instances"

echo
echo "______________________________________________________________________________________"
echo "ajp examples:"
echo

echo "Clear results"

rm -rf timing.txt
touch timing.txt 
date >> timing.txt

set grok_script=lift_to_classlevel.grok
set grok_script_private_constructor = lift_private_constructor.grok 
set input_eclipse = input.file.eclipse.txt
rm -rf $input_eclipse
rm -rf *.pinot.out *.javex.out *.ql.out* *.grok.out
rm -rf candidateinstances/
touch $input_eclipse

mkdir candidateinstances/

foreach directory (AbstractFactory FactoryMethod adapter bridge builder chain command composite decorator flyweight interpreter iterator mediator memento observer prototype proxy singleton state strategy templatemethod visitor ) 
#foreach directory ( prototype )

# removed prototype and facade 

  echo '#############################'
  echo '# '$directory

  set pinot_dir=$directory'/*.java'
  set pinot_out=$directory'.1.pinot.out'
  echo "run pinot"
  pinot -Xstdout $pinot_dir > $pinot_out
  
  set java_in=$directory'/*.java' 
  echo "run javac"
  javac $java_in

  set javex_dir=$directory'/*.class'
  set javex_out=$directory'.1.javex.out'
  set grok_out=$directory'.1.grok.out' 
  set grok_out_private=$directory'.private.constructor.out'
  set ql_dir='ql/'$directory'.ql'
  set ql_out=$directory'.1.ql.out'
  echo "run javex"
  javex -f -l $javex_dir > $javex_out 
  
  echo "run grok"
  grok $grok_script $javex_out $grok_out 
  grok $grok_script_private_constructor $javex_out $grok_out_private   
 
  echo '#########################'
  echo 'Create Eclipse input file for all AJP Patterns'
  
  echo "run ql"
  
  #ql $ql_dir $grok_out $ql_out 
  foreach ql_script ( AbstractFactory FactoryMethod adapter bridge builder chain command composite decorator flyweight interpreter iterator mediator memento observer prototype proxy singleton state strategy templatemethod visitor )
  #foreach ql_script ( prototype )   

    echo '#########################'
    echo 'Singleton is a special case, due to the private constructor'
    if ( $ql_script =~ 'singleton' ) then
       run_readline.sh $grok_out_private >> $grok_out 
    endif  


    echo 'Directory '$directory'  - QL script '$ql_script'.ql'
    set ql_input_script = 'ql/'$ql_script'.ql'
    set ql_input  = $grok_out
    set ql_output = 'candidateinstances/'$directory'.'$ql_script'.ql.out.instances'
    echo "run ql in double loop"
    ql $ql_input_script $ql_input $ql_output

    set pde_input = $ql_output' dynamicfacts/ajp_code.'$directory'.RunPattern.txt dynamicdefinitions/ajp_code.'$ql_script'.xml'
    echo $pde_input >> $input_eclipse
    
    echo ''
    echo 'Add information of roles to top of ql output'
    rm -rf help_file
    mv $ql_output help_file
 
    #if ($ql_script =~ 'AbstractFactory.ql') then 
    if ($ql_script =~ 'AbstractFactory') then
      echo '// abstractFactory concreteFactory product abstractProduct ' > $ql_output
    endif
    
    #if ($ql_script =~ 'FactoryMethod.ql') then 
    if ($ql_script =~ 'FactoryMethod') then
      echo '// creator concreteCreator concreteProduct product ' > $ql_output
    endif

    #if ($ql_script =~ 'adapter.ql') then 
    if ($ql_script =~ 'adapter') then
      echo '// client target adapter adaptee ' > $ql_output
    endif
    
    #if ($ql_script =~ 'bridge.ql') then 
    if ($ql_script =~ 'bridge') then
      echo '// refinedAbstraction abstraction implementer concreteImplementer ' > $ql_output
    endif    

    #if ($ql_script =~ 'builder.ql') then 
    if ($ql_script =~ 'builder') then
      echo '// director builder concreteBuilder product ' > $ql_output
    endif
    
    #if ($ql_script =~ 'chain.ql') then 
    if ($ql_script =~ 'chain') then
      echo '// client handler concreteHandler ' > $ql_output
    endif    
    
    #if ($ql_script =~ 'command.ql') then 
    if ($ql_script =~ 'command') then
      echo '// invoker command concreteCommand receiver ' > $ql_output
    endif
    
    #if ($ql_script =~ 'composite.ql') then 
    if ($ql_script =~ 'composite') then
      echo '// leaf component composite ' > $ql_output
    endif    
    
    #if ($ql_script =~ 'decorator.ql') then 
    if ($ql_script =~ 'decorator') then
      echo '// concreteComponent component decorator concreteDecorator ' > $ql_output
    endif
    
    #if ($ql_script =~ 'flyweight.ql') then 
    if ($ql_script =~ 'flyweight') then
      echo '// flyweightFactory flyweight concreteFlyweight ' > $ql_output
    endif
    
    #if ($ql_script =~ 'interpreter.ql') then 
    if ($ql_script =~ 'interpreter') then
      echo '// expression abstractExpression context ' > $ql_output
    endif
    
    #if ($ql_script =~ 'iterator.ql') then 
    if ($ql_script =~ 'iterator') then
      echo '// concreteAggregate iterator concreteIterator ' > $ql_output
    endif   
 
    #if ($ql_script =~ 'mediator.ql') then 
    if ($ql_script =~ 'mediator') then
      echo '// concreteMediator mediator concreteColleague ' > $ql_output
    endif
    
    #if ($ql_script =~ 'memento.ql') then 
    if ($ql_script =~ 'memento') then
      echo '// originator memento caretaker ' > $ql_output
    endif
    
    #if ($ql_script =~ 'observer.ql') then 
    if ($ql_script =~ 'observer') then
      echo '// subject concreteSubject observer concreteObserver ' > $ql_output
    endif
    
    #if ($ql_script =~ 'prototype.ql') then 
    if ($ql_script =~ 'prototype') then
      echo '// prototypeInterface prototype client ' > $ql_output
    endif    
    
    #if ($ql_script =~ 'proxy.ql') then 
    if ($ql_script =~ 'proxy') then
      echo '// realSubject subject proxy ' > $ql_output
    endif
    
    #if ($ql_script =~ 'singleton.ql') then 
    if ($ql_script =~ 'singleton') then
      echo '// client singleton ' > $ql_output
    endif
    
    #if ($ql_script =~ 'state.ql') then 
    if ($ql_script =~ 'state') then
      echo '// context state concreteState ' > $ql_output
    endif      
      
    #if ($ql_script =~ 'strategy.ql') then 
    if ($ql_script =~ 'strategy') then
      echo '// context strategy concreteStrategy ' > $ql_output
    endif
    
    #if ($ql_script =~ 'templatemethod.ql') then 
    if ($ql_script =~ 'templatemethod') then
      echo '// abstractClass concreteClass ' > $ql_output
    endif
    
    #if ($ql_script =~ 'visitor.ql') then 
    if ($ql_script =~ 'visitor') then
      echo '// visitor concreteVisitor element concreteElement ' > $ql_output
    endif          
            
    cat help_file >> $ql_output   
    rm -rf help_file
 
  end
end

date >> timing.txt
echo "done"
