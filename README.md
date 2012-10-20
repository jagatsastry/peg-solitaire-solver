All the java files are in the package "ai". 
The code can be run by using either the classes directory as the classpath or by zipping up the classes into a jar file, and passing the jar in the classpath. 

To test the code written, do the following

Enter the board's configuration in a file, say inp.txt, one row per line in the following way, 
where X represents a hole with peg, 0 represents an empty hole and "-" represents an invalid position. 

--XXX--
--XXX--
XXXXXXX
XXX0XXX
XXXXXXX
--XXX--
--XXX--

Multiple board configurations can be present in the input file, in which case all the inputs are considered in sequence.

To test the input configurations, do the following

java -cp ai.jar:/home/classes ai.TestPegSolver [input_file [algorithm_type_code]]

input file
Input file, containing the board configurations to be solved. When not passed, a fixed set of hardcoded configurations are used.

Algorithm type code : 
1 - DFS
2 - A* with Manhattan Distance Heuristic
3 - A* with Weighted Position Matrix Heuristic

It's purely optional. When not passed, all the algorithms are used for each board in the above sequence.
