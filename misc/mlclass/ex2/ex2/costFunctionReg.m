function [J, grad] = costFunctionReg(theta, X, y, lambda)
%COSTFUNCTIONREG Compute cost and gradient for logistic regression with regularization
%   J = COSTFUNCTIONREG(theta, X, y, lambda) computes the cost of using
%   theta as the parameter for regularized logistic regression and the
%   gradient of the cost w.r.t. to the parameters. 

% Initialize some useful values
m = length(y); % number of training examples

% You need to return the following variables correctly 
J = 0;
grad = zeros(size(theta));

% ====================== YOUR CODE HERE ======================
% Instructions: Compute the cost of a particular choice of theta.
%               You should set J to the cost.
%               Compute the partial derivatives and set grad to the partial
%               derivatives of the cost w.r.t. each parameter in theta


%vectorized version of J 

t=sigmoid(X*theta);
t1=log(t);
t2=log(1 - t);

tmpTheta=theta;  %i am making a  tmpTheta so that i can blank the theta(1)
tmpTheta(1)=0;

%regularization parameter is 'r'

%vectorised version
r=(lambda/(2*m)) * (sum(tmpTheta.^2));


%r=0;
%for j=2:length(theta),
%	r = r + ( theta(j)^2); 
%end;
%r= (r * lambda) / (2 * m) ;

%regularized cost function
J= (((-t1'*y) - (t2'*(1-y)))/m) + r;

%calculate the regularized gradient now 

grad = (X' * ( sigmoid ( X * theta ) - y ) / m) ;
grad = grad .+ ((lambda/m)*tmpTheta) ;

% =============================================================

end
