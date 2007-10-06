#!/bin/bash

/cs/research/bil/Marcel/pinot/pinot/bin/pinot -Xstdout -classpath jhotdraw > pinot_jhotdraw 
/cs/research/bil/Marcel/pinot/pinot/bin/pinot -Xstdout AbstractFactory/*.java > pinot_AbstractFactory
/cs/research/bil/Marcel/pinot/pinot/bin/pinot -Xstdout FactoryMethod/*.java > pinot_FactoryMethod
/cs/research/bil/Marcel/pinot/pinot/bin/pinot -Xstdout adapter/*.java > pinot_adapter
/cs/research/bil/Marcel/pinot/pinot/bin/pinot -Xstdout bridge/*.java > pinot_bridge
/cs/research/bil/Marcel/pinot/pinot/bin/pinot -Xstdout builder/*.java > pinot_builder
/cs/research/bil/Marcel/pinot/pinot/bin/pinot -Xstdout chain/*.java > pinot_chain
/cs/research/bil/Marcel/pinot/pinot/bin/pinot -Xstdout command/*.java > pinot_command
/cs/research/bil/Marcel/pinot/pinot/bin/pinot -Xstdout composite/*.java > pinot_composite
/cs/research/bil/Marcel/pinot/pinot/bin/pinot -Xstdout headfirst_composite_menu/*.java > pinot_composite_hft
/cs/research/bil/Marcel/pinot/pinot/bin/pinot -Xstdout decorator/*.java > pinot_decorator
/cs/research/bil/Marcel/pinot/pinot/bin/pinot -Xstdout facade/*.java > pinot_facade 
/cs/research/bil/Marcel/pinot/pinot/bin/pinot -Xstdout flyweight/*.java > pinot_flyweight
/cs/research/bil/Marcel/pinot/pinot/bin/pinot -Xstdout interpreter/*.java > pinot_interpreter
/cs/research/bil/Marcel/pinot/pinot/bin/pinot -Xstdout iterator/*.java > pinot_iterator
/cs/research/bil/Marcel/pinot/pinot/bin/pinot -Xstdout mediator/*.java > pinot_mediator
/cs/research/bil/Marcel/pinot/pinot/bin/pinot -Xstdout memento/*.java > pinot_memento
/cs/research/bil/Marcel/pinot/pinot/bin/pinot -Xstdout observer/*.java > pinot_observer
/cs/research/bil/Marcel/pinot/pinot/bin/pinot -Xstdout prototype/*.java > pinot_prototype
/cs/research/bil/Marcel/pinot/pinot/bin/pinot -Xstdout proxy/*.java > pinot_proxy
/cs/research/bil/Marcel/pinot/pinot/bin/pinot -Xstdout singleton/*.java > pinot_singleton
/cs/research/bil/Marcel/pinot/pinot/bin/pinot -Xstdout state/*.java > pinot_state
/cs/research/bil/Marcel/pinot/pinot/bin/pinot -Xstdout strategy/*.java > pinot_strategy
/cs/research/bil/Marcel/pinot/pinot/bin/pinot -Xstdout templatemethod/*.java > pinot_templatemethod
/cs/research/bil/Marcel/pinot/pinot/bin/pinot -Xstdout visitor/*.java > pinot_visitor
