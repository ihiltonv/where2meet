# term-project

DATA:
The data contains three tables: users, events, and events\_users. 

Users contains id, name, latitude, longitude. Id is the id of the user, name is the name of the user, latitude is the lat of the user's location, and longitude is the long of the user's location. 

Events contains. id, name, latitude, longitude, date, and time. Id is the event id, name is the event name, latitude and longitude are the coordinates of the event's location, date is a string with the date of the event and time is a string with the time of the event. 

Events\_users contains event\_id, user\_id, price, rating, distance, and category. Event\_id is the id of the event, user\_id is the id of the user, price and rating are integers representing the values of the filters for that user in that event, distance is a double representing the distance filter for that event with that user, and category is the category filter for that user and that event. 