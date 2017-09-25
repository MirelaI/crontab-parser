# CronTab Parser

## Description
Command line application that parses a contab input string and prints the 
result to the STDOUT in the following format:

```
minute 0 15 30 45
hour 0
day of month 1 15
month 1 2 3 4 5 6 7 8 9 10 11 12
day of week 1 2 3 4 5
command /usr/bin/find
```

## Prerequisites
CronTab parser is using SBT as the build tool. In order to run the parser you
need to have installed [SBT](http://www.scala-sbt.org/) first. 

## Run instructions

1. Clone the current repo
```
git clone git@github.com:MirelaI/crontab-parser.git
```
2. Open sbt console from the crontab-parser repo
```
$ cd crontab-parser
$ sbt
```
3. Once in the sbt console start playing with the application. 
See below some useful commands:
```
# Compile the bundle
> compile
...
# Run tests
> test
...
# Run only one test
> testOnly com.crontab.ItemParserTest
...
```
4. Once familiar with sbt and parser application you can start parsing crontab
commands:
```
> run "*/15 0 1,15 * 1-5 /usr/bin/find"
...
> run "* * * * * /usr/bin/find"
```

## To fix

- improve toString method that displays the output to be more user friendly as 
is too scalaish
- validation on ranges for lower and upper bound on integer values and also on 
literals
- refactor ```parse()``` method from the ItemParser class
- benchmarking on performance
- improve test suite

