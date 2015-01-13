timeseries-util
===============

[![Build Status](https://travis-ci.org/lunextelecom/timeseries-util.svg?branch=master)](https://travis-ci.org/lunextelecom/timeseries-util)

Timeseries implementation, basic operations, and usage pattern.

###Common Usage Pattern
- func(avg, sum)(x element) over series with x Seconds/Mins/Hours interval per element.
```
use function over series
```
- Avg,Sum of Last x Mins/Hours/Days 
```
Use TimeSeriesBucket of bucksize of x Mins/Hours/Days with elementSize of 5 sec(data definition)
alternatively, can use Function avg, sum over a regular series.
```
- Avg,Sum of Last x Friday
```
Create series Time Schedule to include only friday with element size day.
Create Function Avg or Sum for interval of x days.
```
- Series with only 9am-5pm, Mon-Fri
```
Create series with with custom Schedule, 
9am-5pm
Mon-Fri
```

###Aggregration
min/low: minimal value  
max/high:  
avg:  
count:  

### Element Behavior: a element that shift with the current time. eg. last 30 mins.
	EVERY(TIME_PERIOD): every 30 mins  
	LAST(TIME_PERIOD, DEFINITION): TIME_PERIOD is how far to look back, DEFINITION is how detail the data is.  eg. last 30mins 
### Time Schedule
	ALL: (Default) include all  
	DAY_OF_WEEK: where ELEMENT are only every friday  
	DAYS_OF_WEEK: WEEKDAY(MON-FRI), WEEKEND(SAT,SUN)  
	HOURS_OF_DAY: 9AM-5PM  

###Function
Function create a new series from a source series.
1. Simple Moving Avg
2. Sum
3. Max

###Series Structure
```
[element0]...[element359] # Series is an array of element

element contain time(begin), value and weight
```

###Type of data structure
Timeseries: Array of element. Older record get expired and removed.  This is good for long running process to avoid over use of memory.  
Bucket: contain a timeseries with granular size and a large element that is sum of those smaller ones.  
```
at time 0
[element0]...[element359]  #Granular time series.  Times series1 @ 10 sec (this can be graph)
[      60min          ]  #Custom element Times series2 @ 60 mins. (do not graph this as it mutate)

at time 10sec, element360 is added.  element0-359 is already computed, so no waste cpu.  element360 is incremental added.  To find out the avg value of the last 60 mins, we can just minus element0 and add element360

[element0][element1]...[element360]  #Times series1 10 sec
         [      60min          ]  #Times series2 60 min

and some time later...
[element2]...[element361][element362]...[element721]
            [      60min            ] #notice always only 1 element
```



