## Project Description
A simple topology initializer using mininet and floodlight

## Author
Jian Li (pyguni at gmail.com)

## Usage
### Compile the Java Source Files
In order to run the script, first need to compile the corresponding Java Source Files in src directory.
Use ant source builder to compile the entire source, a target directory will be generated.

	ant
    
Note that to compile the Java Source, you need to install the ant first.

### How to Initialize Flow Tables of Switches

* Initialize the flow table by using route #1

		./simpleTopoMod.sh init route1
		
* Initialize the flow table by using route #2

		./simpleTopoMod.sh init route2

### How to Remove all Flow Entries

	./simpleTopoMod.sh clear

### Clean Compiled Binary Files
Sometimes we need to clean the binary files located in target directory. The clean procedure is pretty easy, which is just running following command.

	ant clean
