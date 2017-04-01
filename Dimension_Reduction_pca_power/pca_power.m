function [] = pca_power(trainingfileName, test_file, M, iterations)

    %
    % reading input file data as Data
    %
    delimiterIn = ' ';
    headerlinesIn = 0;
    Data = importdata(trainingfileName,delimiterIn,headerlinesIn);
    [trainingDataCount,Dimensioncount]=size(Data);
    Dimensioncount=Dimensioncount-1; % removing class label
    t=Data(1:end,end); % class lable of training data

    Sample=Data(1:end,1:end-1);
    
    for i=1:1:trainingDataCount
       x(i,:)=Sample(i,:);
    end

    for i=1:1:M
        
       CovarianceMatrix = cov(x);
       
       u(i,:)=getEigenVector(CovarianceMatrix,iterations); 
       
        for j=1:1:trainingDataCount
           x(j,:)=x(j,:) - (u(i,:)* (x(j,:)')).*u(i,:);
        end
      
    end
    
    [row column]=size(u);
    
    %display eigen vactors
    for i=1:1:M
        
          fprintf('Eigenvector %d\n',i);
          for j=1:1:column
               fprintf('%d : %.4f\n',j,u(i,j));
          end
          fprintf('\n');
      
      
       
    end
    
    %display(u);
    

    
    %
    %Test Data  
    %
    TestData=importdata(test_file,delimiterIn,headerlinesIn);
     
       TestData=TestData(1:end,1:end-1);
       [testDataCount,Dimensioncount]=size(TestData);
    for n=1:1:testDataCount
         fprintf('Test object %d\n',n-1);
        for i=1:1:M
        
         
               
               fprintf(' %d : %.4f\n',i, TestData(n,:)*u(i,:)');
          
        end
        fprintf('\n');
    end
    
    
    
end


function b = getEigenVector(CovarianceMatrix,iterations)    
       [Dimensioncount,Dimensioncount]=size(CovarianceMatrix);
     %  b=ones(Dimensioncount,1);
       b=rand(Dimensioncount,1);
       
       for i=1:1:iterations
           temp=(CovarianceMatrix * b);
           b=temp./(norm(temp));
       end
       
    
end
