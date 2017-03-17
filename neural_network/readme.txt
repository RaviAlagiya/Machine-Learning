neural_network <training_file> <test_file> <layers> <units_per_layer> <rounds>

The arguments provide to the program the following information:
 The first argument, <training_file>, is the path name of the training file, where the training data is stored. The path name can specify any file stored on the local computer.
 The second argument, <test_file>, is the path name of the test file, where the test data is stored. The path name can specify any file stored on the local computer.
 The third argument, <layers>, specifies how many layers to use. Note that the input layer is layer 1, so the number of layers cannot be smaller than 2.
  The fourth argument,<units_per_layer>, specifies how many perceptrons to place at each hidden layer. This number excludes the bias input. So, each hidden layer should contain <units_per_layer> perceptrons and one bias input unit. Note that this number is not applicable to units in the input layer, since those units are not perceptrons but simply provide the values of the input object and the bias input. Also, note that the number of perceptrons in the output layer is equal to the number of classes, and thus this number is independent of the <units_per_layer> argument.
 The fifth argument, <rounds>, is the number of training rounds that you should use.

To Run the code : Copy Unit.java, Layer.java, SampleData.java and neural_network.java then

1) type “javac neural_network.java” 
 <press enter>

2) java neural_network pendigits_training.txt pendigits_test.txt 2 50
<press enter>

3) java neural_network pendigits_training.txt pendigits_test.txt 3 20 20
<press enter>

