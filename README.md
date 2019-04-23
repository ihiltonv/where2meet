# term-project

DATA:
The data contains three tables: users, events, and events\_users. 

Users contains id:Long, name:string, latitude:double, longitude:double. Id is the id of the user, name is the name of the user, latitude is the lat of the user's location, and longitude is the long of the user's location. 

Events contains. id:Long, name:string, latitude:double, longitude:double, date:string, time:string, s1:string, s2:string, s3:string. Id is the event id, name is the event name, latitude and longitude are the coordinates of the event's location, date is a string with the date of the event and time is a string with the time of the event. S1,s2, and s3 are the top three suggestions for a user in that event, in string form. Use the Suggestion.toSugg(String) method to turn them back into suggestions.

Events\_users contains event\_id:long, user\_id:Long, price:int, rating:double, distance:double, category:string, s1:string, s2:string, and s3:string. Event\_id is the id of the event, user\_id is the id of the user, price and rating are integers representing the values of the filters for that user in that event, distance is a double representing the distance filter for that event with that user, and category is the category filter for that user and that event. S1,s2, and s3 are the top three suggestions for a user in that event, in string form. Use the Suggestion.toSugg(String) method to turn them back into suggestions.

Filters in use: 
	Price (integer 1-4)
	Location range(double)
	Popularity(double 0-5)
	Category(string)