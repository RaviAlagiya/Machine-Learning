function [] = main(trainingfileName,degree,testfileName)
    
%reading training & test data    
delimiterIn = ' ';
headerlinesIn = 0;
A = importdata(trainingfileName,delimiterIn,headerlinesIn);

[trainingDataCount,Dimensioncount]=size(A);
Dimensioncount=Dimensioncount-1; % removing class label

t=A(1:end,end); % class lable of training data



for i=1:1:trainingDataCount
   if(t(i) ~= 1)
       t(i)=0;
   end
end

%sample data  
samples=num2cell(A(:,1:end-1),2);

A=importdata(testfileName,delimiterIn,headerlinesIn);




  W = zeros(1,(Dimensioncount*(degree))+1);
  W=W';

%calculating Theta;
    for i=1:1:trainingDataCount
            k=1;
            Theta(i,k)=1;
        for d=1:1:Dimensioncount
            for j=1:1:(degree)
                k=k+1;
            	Theta(i,k)=power(samples{i}(d),(j));     
            end
        end 
    end
    

  tempDiff=1;
  W_old=W;
  Entropy=10000000;
  oldEntropy=Entropy;
  count=1;

  entropyDiff=1;
     while (tempDiff >= 0.001)  && (entropyDiff >= 0.001 )%|| count == 2)
         
       %fprintf('round %d\n',count);
       
       %calculate Y
          for i=1:1:trainingDataCount
              X=Theta(i,:);
                y(i)=yofx(W,X);
          end
           Y=y';
           
           R=diag(Y);
           
           for i=1:1:trainingDataCount
               
               R(i,i)=R(i,i)*(1-R(i,i)); 
               
           end
         
              gradE=mtimes(transpose(Theta),(Y-t));
  
            H=transpose(Theta)* R *Theta;
            W=W - mtimes(inv(H),gradE);
          
            Entropy=0;
             for i=1:1:trainingDataCount
             Entropy=Entropy - (t(i)*log(yofx(W,Theta(i,:))) + (1-t(i))*log(1.0-yofx(W,Theta(i,:))));
             end
         
            tempW=abs(W_old-W);
             
              tempDiff=sum(tempW(:));
              entropyDiff=oldEntropy-Entropy;
           
                W_old=W;
              oldEntropy=Entropy;
       
              count=count+1;
    
     end
     
     length=size(W,1);
     for i=1:1:length
         fprintf('w%d=%.4f\n',i-1, W(i));
     end   
    testData(W,A,degree);

end


function y = yofx(W,X)    
    temp=transpose(W) * transpose(X);
    y=1.0/(1.0+ exp(-temp));
end

function [] =testData(W,A,degree)
    
    classification_accuracy=0;
    T=A(1:end,end); % class lable of test data
    A=A(:,1:end-1);
    
    
    
    [testDataCount,Dimensioncount]=size(A);
    
        for i=1:1:testDataCount
           if(T(i) ~= 1)
               T(i)=0;
           end
        end
        
        for i=1:1:testDataCount
                k=1;
                ThetaTest(i,k)=1;
            for d=1:1:Dimensioncount
                for j=1:1:(degree)
                    k=k+1;
                	ThetaTest(i,k)=power(A(i,d),(j));     
                end
            end 
        end
        
    
    
          for i=1:1:testDataCount
           
              y=yofx(W,ThetaTest(i,:));
                if(y>0.5)
                    if(T(i) == 1)
                    accuracy=1;
                    else
                        accuracy=0;
                        
                    end
                    
                    fprintf('ID=%5d, predicted=%3d, probability = %.4f, true=%3d, accuracy=%4.2f\n',i-1,  1,y, T(i), accuracy);
                    
                elseif(y< 0.5)
                    if(T(i) == 0)
                        accuracy=1;
                    else
                        accuracy=0;
                    end
                   fprintf('ID=%5d, predicted=%3d, probability = %.4f, true=%3d, accuracy=%4.2f\n', i-1, 0,y, T(i), accuracy);
                    
                elseif(y==0.05)  
                    accuracy=0.5;
                     fprintf('ID=%5d, predicted=%3d, probability = %.4f, true=%3d, accuracy=%4.2f\n', i-1, randi([0 1],1,1),y, T(i), accuracy);
                end
               classification_accuracy=classification_accuracy+ accuracy;
           end
        
      
        
       classification_accuracy=classification_accuracy/testDataCount;
        
      fprintf('classification accuracy=%6.4f\n', classification_accuracy);
        
          
      
end







