SENTIMENT ANALYSIS - TURKISH TWEETS  - Detailed Report Can Be Found in "Report" file.
Project Report:
Problem Definition:
 	Given a tweet about banks written in Turkish, the team was asked to classify its polarity (how positive a review is it) as a score between -1 (very negative) to +1 (very positive); so this is a regression problem.
 E.g. “alt tarafi ATM karti istiyoruz. 5 aydir ne gelen ne giden. Para cekmek icin subeye gitmek
 zorundamiyim???” -> -0.8 (quite negative comment)
 “sorun çözüldü, tesekkürler!”-> +1 (fully positive comment)

Our Approach:  
	After proper investigation of the train and test sets, the team devised a way to tackle with this problem. Since we would be dealing with tweets our approach revolved around that. Initial step to be taken was “text cleaning”.  That was an essential step because the data was highly unstructured and noisy meaning the tweets were raw data and far away from the proper Turkish language. After this step, we needed to extract features which then combined with the given features. The extracted features are as follows:
o	Number of question marks
o	Number of exclamation marks
o	Number of negative smileys
o	Number of positive smileys
o	Number of very positive tokens
o	Number of positive tokens
o	Number of neutral tokens
o	Number of minor negative tokens
o	Number of negative tokens
o	Number of very negative tokens
o	Number of extremely negative tokens
o	General sentiment weight
Apart from those extracted features the team has also find features based on bag of words method and tf-idf and tried regression with those features as well. Our aim was combine all of the features and then find the best possible predictions and minimize errors.
After the team found the proper features, multitude of regression algorithms were executed for the task. Some of them are as follow: 
-	Decision Tree Regression
-	k-nearest neighbors
-	Support Vector Machine Regression
-	Stochastic Gradient Descent Regression
-	Random Forests Regression
-	Multi-layer Perceptron Regression
-	Ridge Regression
-	Bayesian Ridge Regression
-	Ensemble Regression with Ada Boost Regressor
Then based on the results of the regressions some of the parameters are changed iteratively in order to find maximum accuracy.
