timeseries-util
===============
Timeseries implementation, basic operations, and usage pattern.

###Structure
```
[element0]...[element359] # Series is an array of element

element contain time(begin), value and weight
```

###Type of data structure
Timeseries - Array of element
RollingTimeseries - Same as Timeseries except the older record get expired and removed.  This is good for long running process to avoid over use of memory.
Bucket - contain a timeseries with fine grain size and a element that is sum of those element.
```
at time 0
[bucket0]...[bucket359]  #Granular bucket.  Times series1 @ 10 sec (this can be graph)
[      60min          ]  #Custom bucket Times series2 @ 60 mins. (do not graph this as it mutate)

at time 10sec, bucket360 is added.  bucket0-359 is already computed, so no waste cpu.  bucket360 is incremental added.  To find out the avg value of the last 60 mins, we can just minus bucket0 and add bucket360

[bucket0][bucket1]...[bucket360]  #Times series1 10 sec
         [      60min          ]  #Times series2 60 min

and some time later...
[bucket2]...[bucket361][bucket362]...[bucket721]
            [      60min            ] #notice always only 1 bucket
```



###Time Selector - select size of each 
Rolling Time : a fix time from the current time. eg. last 30 mins.  
Fixed Time: the time range is fixed. Time of date, Day of week, custom start end.  

