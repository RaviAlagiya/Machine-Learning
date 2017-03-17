Copy Unit.java, Layer.java, SampleData.java and neural_network.java then

1) type “javac neural_network.java” 
 <press enter>

2) java neural_network pendigits_training.txt pendigits_test.txt 2 50
<press enter>

3) java neural_network pendigits_training.txt pendigits_test.txt 3 20 20
<press enter>

Note : 

file path should be absolute if file is not in the same directory as program.

If above sequence gives error, please follow below sequence to run the code.

[rxa2485@omega Assignment_6]$ javac Unit.java 
[rxa2485@omega Assignment_6]$ javac Layer.java 
[rxa2485@omega Assignment_6]$ javac SampleData.java 
[rxa2485@omega Assignment_6]$ javac neural_network.java 
[rxa2485@omega Assignment_6]$ java neural_network pendigits_training.txt pendigits_test.txt 2 50
[rxa2485@omega Assignment_6]$ java neural_network pendigits_training.txt pendigits_test.txt 3 20 20