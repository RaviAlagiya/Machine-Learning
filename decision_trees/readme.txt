input Format : 
Dtree <training_file> <test_file> <option> <pruning_thr>

The arguments provide to the program the following information:
The first argument, <training_file>, is the path name of the training file, where the training data is stored.
The second argument, <test_file>, is the path name of the test file, where the test data is stored. 
The third argument, <option>, can have four possible values: optimized, randomized, forest3, or forest15. It specifies how to train (learn) the decision tree.

*optimized: in this case, at each non-leaf node of the tree (starting at the root) the program identify the optimal combination  of attribute (feature) and threshold, i.e., the combination that leads to the highest information gain for that node.
*randomized: in this case, at each non-leaf node of the tree (starting at the root) the program choose the attribute (feature) randomly. The threshold is still optimized, i.e., it is chosen so as to maximize the information gain for that node and for that randomly chosen attribute.
*forest3: in this case, the program trains a random forest containing three trees. Each of those trees should be trained as discussed under the "randomized" option.
*forest15: in this case, the program trains a random forest containing 15 trees. Each of those trees should be trained as discussed under the "randomized" option.

Copy  Dtree.java ,Node.java and SampleData.java then

Compile the Dtree.java 
1) type “javac Dtree.java”
 <press enter>

2) type “java Dtree pendigits_training.txt pendigits_test.txt optimized 50”
<press enter>

3) type “java Dtree pendigits_training.txt pendigits_test.txt randomized 50”
<press enter>

4) type “java Dtree pendigits_training.txt pendigits_test.txt forest3 50”
<press enter>


