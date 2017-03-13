function [ ] = linear_regression( file,degree,lambda )

degree=degree+1;
fileID = fopen(file);
c = textscan(fileID,'%f32 %f32');
fclose(fileID);

t=c{2};
x=c{1};
n= size(x,1);


for i=1:1:n
    for j=1:1:(degree)
        
    	X(i,j)=power((x(i,1)),(j-1));     
        
    end
end


I = eye(degree);
A=lambda.*I;
B = A+ (transpose(X) * (X));
C=inv(B);
Ans =(C * transpose(X)) * t;

fprintf('w0=%.4f\n', Ans(1,1));
fprintf('w1=%.4f\n', Ans(2,1));
if (size(Ans,1) == 2)
	fprintf('w2=%.4f\n', 0);
else
   fprintf('w2=%.4f\n', Ans(3,1)); 
end


end