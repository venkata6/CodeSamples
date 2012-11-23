function [C, sigma] = dataset3Params(X, y, Xval, yval)
%EX6PARAMS returns your choice of C and sigma for Part 3 of the exercise
%where you select the optimal (C, sigma) learning parameters to use for SVM
%with RBF kernel
%   [C, sigma] = EX6PARAMS(X, y, Xval, yval) returns your choice of C and 
%   sigma. You should complete this function to return the optimal C and 
%   sigma based on a cross-validation set.
%

% You need to return the following variables correctly.
C = 1;
sigma = 0.3;

% ====================== YOUR CODE HERE ======================
% Instructions: Fill in this function to return the optimal C and sigma
%               learning parameters found using the cross validation set.
%               You can use svmPredict to predict the labels on the cross
%               validation set. For example, 
%                   predictions = svmPredict(model, Xval);
%               will return the predictions on the cross validation set.
%
%  Note: You can compute the prediction error using 
%        mean(double(predictions ~= yval))
%

% test values for C and Sigma for checking in cross validation dataset
values=[0.01,0.03,0.1,0.3,1,3,10,30] ;

%predictions = zeros(size(Xval));
prederror = zeros(size(Xval));

cnti=1;
cntj=1;
optimum =1000 ;

for i=values,
  for j=values,
     model= svmTrain(X, y, i, @(x1, x2) gaussianKernel(x1, x2, j));
     predictions = svmPredict(model,Xval);     
     prederror = (double(predictions ~= yval)) ;
     %printf( "values of error for %f %f %f  \n ", sum(prederror),i,j ); 
     prederror = sum(prederror) ;
     if prederror < optimum,
       C=i;
       sigma=j;
       optimum = prederror;
     end;
     cntj += 1;
  end;
  cnti += 1;
end;

printf( "values of C and sigma for %f %f  \n ", C,sigma ); 


% =========================================================================

end
