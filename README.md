timeseries-util
===============
Timeseries implementation, basic operations, and usage pattern.

###Structure
```
[element0]...[element359] # Series is an array of element

element contain time(begin), value and weight
```

###Type of data structure
Timeseries: Array of element  
RollingTimeseries: Same as Timeseries except the older record get expired and removed.  This is good for long running process to avoid over use of memory.  
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





###Element time selector
######Rolling: a element that shift with the current time. eg. last 30 mins.   	
	LAST(TIME_PERIOD): eg. last 30mins  
######Fixed: the time range is fixed. Time of date, Day of week, custom start end.        
	ALL: (Default) include all  
	DAY_OF_WEEK: where ELEMENT are only every friday  
	DAYS_OF_WEEK: WEEKDAY(MON-FRI), WEEKEND(SAT,SUN)  
	HOURS_OF_DAY: 9AM-5PM  

