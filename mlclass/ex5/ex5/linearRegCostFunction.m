function [J, grad] = linearRegCostFunction(X, y, theta, lambda)
%LINEARREGCOSTFUNCTION Compute cost and gradient for regularized linear 
%regression with multiple variables
%   [J, grad] = LINEARREGCOSTFUNCTION(X, y, theta, lambda) computes the 
%   cost of using theta as the parameter for linear regression to fit the 
%   data points in X and y. Returns the cost in J and the gradient in grad

% Initialize some useful values
m = length(y); % number of training examples

% You need to return the following variables correctly 
J = 0;
grad = zeros(size(theta));

% ====================== YOUR CODE HERE ======================
% Instructions: Compute the cost and gradient of regularized linear 
%               regression for a particular choice of theta.
%
%               You should set J to the cost and grad to the gradient.
%
tmpTheta=theta;
tmpTheta(1)=0 ;
%printf("size of y %f %f \n ", size(y));

J = sum(((X*theta)-y).^2 /(2*m));
J += sum(lambda/(2*m)* tmpTheta.^2);  
for i=1:size(X,2),
	tmp=  sum(((X*theta)-y) .* X(:,i))/m + ( (lambda/m)* tmpTheta(i));
%        printf("size of tmp %f %f \n ", size(tmp));
    grad(i)=tmp;
end;

% =========================================================================

grad = grad(:);

end
