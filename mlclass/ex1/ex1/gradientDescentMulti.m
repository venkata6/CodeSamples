function [theta, J_history] = gradientDescentMulti(X, y, theta, alpha, num_iters)
%GRADIENTDESCENTMULTI Performs gradient descent to learn theta
%   theta = GRADIENTDESCENTMULTI(x, y, theta, alpha, num_iters) updates theta by
%   taking num_iters gradient steps with learning rate alpha

% Initialize some useful values
m = length(y); % number of training examples
J_history = zeros(num_iters, 1);

%fprintf(' inside Gradient Descent - theta  %f \n', theta);

for iter = 1:num_iters

c1=alpha/length(y) ;

for i=1:[size(X,2)],
	temp(i) = (theta' * X'-y') * X(:,i) ;
end;
%%fprintf(' After another iteration of Gradient Descent - theta  %f \n', temp);

%now update the thetas
for i=1:[size(X,2)],
    theta(i) = theta(i) - ( c1 * temp(i)) ;  
end;

%fprintf(' After another iteration of Gradient Descent - theta  %f \n', theta);

    % Save the cost J in every iteration    
    J_history(iter) = computeCost(X, y, theta);

%printf(" thetas %f, %f \n", theta(1),theta(2)) ;
%printf(" J = %f \n",J_history(iter)) ;

end

end
