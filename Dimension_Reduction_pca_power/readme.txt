This code has been developed in matlab.

Program will be invoked as follows:

pca_power(<training_file>,<test_file>,<M>,<iterations>)

Where,
<M> is new dimension.
<iterations> is for accuracy for selecting eigenvectors with highest eigen value.

1) open pca_power.m file in matlab
2) 
pca_power('pendigits_training.txt','pendigits_test.txt', 1, 10)

or

pca_power('satellite_training.txt', 'satellite_test.txt' ,2 ,20)

or

pca_power('yeast_training.txt', 'yeast_test.txt', 3, 30)

3) press enter
