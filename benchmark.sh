#!/bin/bash
#mvn clean
#mvn prepare-package
java -cp target/dep-classes:target/classes:target/test-classes org.openjdk.jmh.Main -wi 3 -i 3 -f 1