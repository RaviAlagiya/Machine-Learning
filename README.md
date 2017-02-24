# Machine-Learning : Naive Bayes classifiers based on histograms, Gaussians, and mixtures of Gaussians

How to Run :
1) type "javac SampleClass.java"
2) press enter.

3) type "javac naive_bayes.java "
4) press enter.

For histograms:

5) java naive_bayes.java <training_file> <test_file> histograms <number>
6) press enter.
example : 

java naive_bayes yeast_training.txt yeast_test.txt histograms 7
#naive_bayes <training_file> <test_file> histograms <number of histrogram>

For gaussians:
5) naive_bayes <training_file> <test_file> gaussians
6) press enter.
example : 
java naive_bayes yeast_training.txt yeast_test.txt gaussians
#naive_bayes <training_file> <test_file> gaussians

For mixtures:
5) naive_bayes <training_file> <test_file> mixtures <number>
6) press enter.
java naive_bayes yeast_training.txt yeast_test.txt mixtures 3
#naive_bayes <training_file> <test_file> mixtures <number of gaussians>
