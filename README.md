#JennWord

This code holds Miguel and Dan's entry to the [World in a Word](http://www.cameronius.com/research/cec/index.html) 64-Bit Design challenge.

The challenge website provides a Java framework and requires that applicants implement a new domain by extending and implementing the classes

	Domain.java
	Individual.java

An ant buildfile has been supplied to aid with building and executing the code. To do this, call __ant run__ from the JennWord directory. Note that the code provided for this entry relies on C++ code for visualisation [(Jenn3d)](http://jenn3d.org) and [Grammatical Evolution](http://www.grammatical-evolution.org). This code is provided in the [GEMapJenn](https://github.com/nacmacfeegle/JennWord/tree/master/GEMapJenn) sub-folder of this repository. 

###Important
The C++ source code of the combined Jenn3d+GE mapper must be compiled first before running the ant target.

To do so, run:

	make 
or
	make -f makefile.mac (if in a Mac environment) 

from GEMapJenn directory. 

Installations of libpng and freeglut3 libraries are required for the build to succeed. In a Mac environment, a recent installation of XCode is required. 

This software has not been tested in a Windows environment.


