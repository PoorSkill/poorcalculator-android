# poorcalculator-android

A basic android calculator written in Java

This was a weekend project right before the start of the lectures to remember Java and programming in general. It's not perfect, bug-free nor good in any sense but it kinda works :)

## Table of Contents

- [poorcalculator-android](#poorcalculator-android)
	- [Table of Contents](#table-of-contents)
	- [Background](#background)
	- [Screenshots](#screenshots)
	- [Install](#install)
	- [Java](#java)
		- [Control-flow-statements](#control-flow-statements)
		- [Strings](#strings)
		- [Classes-and-Objects](#classes-and-objects)
		- [Inheritance-interfaces](#inheritance-interfaces)
		- [Exceptions-Exceptionhandling](#exceptions-exceptionhandling)
		- [Lists](#lists)
		- [Streams-lambdas](#streams-lambdas)
	- [Recursion](#recursion)
	- [Known-Bugs-And-TODO](#known-bugs-and-todo)
	- [License](#license)

## Background
This project was just a fun and interesting idea to relearn Java for me. Since Android applications can be nearly fully written in Java, I choose to make a simple tool - a calculator - and try to use all the different aspects of Java which I learn in the past year. Since I'm still fairly new to Java or programming in genreal (started to seriously learning programming in fall 2019) it's full of newbies bugs or unecessary spaghetti code. I have no idea why somebody would even look at this project. Maybe just me in few years to cringe about it.

## Screenshots

<p float="left">
<img src="/store/screenshots/screenshot_02_01.png?raw=true" width="200" />
<img src="/store/screenshots/screenshot_02_02.png?raw=true" width="200" />
</p>

**Blue Theme & Dark Mode**

## Install

APK Link and GooglePlayStore Link

[GooglePlayStore](https://play.google.com/store/apps/details?id=com.poorskill.poorcalculator "PoorCalculator on GooglePlayStore") or [download the APK from this repository](../master/app/release/poorcalc.apk)

## Java

My first programming experience with Java was 2015 in school where we used Java Abstract Window Toolkit to draw different stuff for learning purpose. We didn't learn much about Java. We learned more about the basics of programming and problem-solving.
Java exp talk and what wanted to revisit in this project.

### Control-flow-statements

You can't really make much without those. Except for *while* and *do-while*. I couldn't find any sensual spot to place them and switching a *for* in favor for a *while*/*do-while* do I see as cheating... But for the rest it's not really hard to find examples of them in my code since I used them pretty much everywhere. Also noticed I didn't use any *for-each* loop ...

### Strings

At first the whole calculator was based on Strings. But why use Strings when you can create your own datatype :)

Yet couldn't I fully remove Strings out of the project because the calculator had to read and output digits and operators for the user and Strings are often a great part at the beginning of learning Java. So I keep some of it.

### Classes-and-Objects

Java is an object-oriented programming language ¯\\_(ツ)_/¯

Utility classes, abstract classes, static, objects ...

Since the whole user input is converted into its own datatype ... Kinda unavoidable :)

### Inheritance-interfaces

The class CalculatorCharakter, which I used to save and process the calculations in inheritances to CalculatorNumber, CalculatorOperators ...
CalculatorNumber and CalculatorConstants also implement the interface CalculatorValues, so I could write this shorter:

```java
 if (ccs.get(i) instanceof CalculatorValues) 
 {
    //...
	xBD = ((CalculatorValues) ccs.get(i)).getValue();
	//....
}
```
Calculator.java btw

### Exceptions-Exceptionhandling

I wrote my own Exceptions ... for learning purpose. Some or even all of them are pretty unnecessary ...
The handling is also not really optimal, since I could avoid most of the *try-catch* with some more thinking and neat control-flow-statments :)

### Lists

The current CalculatorCharacters are saved as List. Don't need to speak about the advantages and disadvantages of List vs Arrays ... Mostly or dominantly only used ArrayLists.

### Streams-lambdas

While I worked on this project, Android started to support Java 8 Features on older devices (or they supported it for a long while, and I was too stupid to find it)
I like Streams and Lambdas and implemented some in this project

## Recursion

Nearly the whole Calculator.java utility class works recursive (but not perfectly optimized). It's magic. I love it.

## Known-Bugs-And-TODO

Bugs
* leading zeros are not shown when inputing decimal numbers (too lazy to rewrite or fix)

Todo
* Better README

## License

[MIT](LICENSE) © Anton Kesy