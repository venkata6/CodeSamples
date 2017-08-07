function [theta, J_history] = gradientDescent(X, y, theta, alpha, num_iters)
%GRADIENTDESCENT Performs gradient descent to learn theta
%   theta = GRADIENTDESENT(X, y, theta, alpha, num_iters) updates theta by 
%   taking num_iters gradient steps with learning rate alpha

% Initialize some useful values
m = length(y); % number of training examples
J_history = zeros(num_iters, 1);

for iter = 1:num_iters

%temp variable to hold the theta values
temp1=0;
temp2=0;
c1=alpha/length(y) ;

% vectorization - delta
temp1 = (theta' * X'-y') * X(:,1) ;
temp2 = (theta' * X'-y') * X(:,2) ;

%now update the thetas
    theta(1) = theta(1) - ( c1 * temp1) ;  
    theta(2) = theta(2) - ( c1 * temp2) ;  
%printf(" thetas %f, %f \n", theta(1),theta(2)) ;
    % Save the cost J in every iteration    
    J_history(iter) = computeCost(X, y, theta);
%printf(" J = %f \n",J_history(iter)) ;
end

end
