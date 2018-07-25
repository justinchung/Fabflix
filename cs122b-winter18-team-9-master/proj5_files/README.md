In the log.txt file, we are given entries such as this, “TS809966552 TJ808179081\n”. What our parser is displayed in the order of the following steps:
	1. Removing all alphabetical letters.
	2. Stripping off the newline.
	3. Splitting between spaces so now we have a list of lists 
		a. EX. [[“809966552”,”808179081”]]
	4. Moving all items out so it is just one list and type casting them into ints.
	5. Separating the list into two, TS and TJ values.
	6. Taking the average of both lists and returning them.

