## amazon-estimate-api
## Search-volume estimate API for Amazon keywords.

### Stack:
* Java 8
* Spring Boot
* Maven

To compile and launch the service:
- Navigate to amazon-estimate-api folder
- Open command prompt/terminal
- Run command "mvnw compile" and then "mvnw spring-boot:run"
- Use any REST client to fetch results

Examples of URLs:
* localhost:8080/estimate?keyword=iphone
* localhost:8080/estimate?keyword=iphone+charger
* localhost:8080/estimate?keyword=books
* localhost:8080/estimate?keyword=dan+brown
* localhost:8080/estimate?keyword=water+bottle

### 1. a. What assumptions did you make?

The Amazon Autocomplete API provides a list of upto 10 results for a keyword. The assumption made in this case was to try to leverage the inner workings of this API. By typing one letter at a time, a list of possible items appear on the search bar. This indicates which possible items were most searched for in the last few days/hottest items and could possible be related to the current popularity of search terms.

### 1. b. How does your algorithm work?

I first split the string into substrings (0 -> i) from the keyword where i is the increasing length of the keywords. These substrings are then searched using the Amazon AutoComplete API to return the list of hot items. From this list, I try to match exactly which of the hit results match the keyword and then assign a weighted score to the result. These scores are then added and normalized at the end to return a score between 0 and 100 (inclusive).

### 1. c. Do you think the ( *hint ) that we gave you earlier is correct and if so - why?

In this specific case, I feel the hint given is correct as it reduces any noise that the hit results might have returned. The API could be just returning the top 10 items without bothering much about the current popularity of the search term. It could also be sorted based out of current hotness, but this is cannot be assumed in my calculations. But in my score, I take into account all the possible hit results that I come across, so it becomes insignificant for me to process this in a logical manner.

### 1. d. How precise do you think your outcome is and why?

It is relatively difficult to predict the accuracy of my outcome as there are multiple factors that weigh in. For example, the AutoComplete API may be returning items in a certain order in which case, the first items hold a greater weightage. However, common words have a relatively lower hit ratio since they are searched along with other words. Amazon, e.g., might be giving more weightage to "iphone charger" than to "iphone" as more people tend to buy an iPhone charger. Also the word "iphone" inside the "iphone charger" may not be included as a hot search term when only "iphone" is searched.

