# utils

Utils library for different puropses

TextNumberComparator implements a java Comparator interface and performs string comparison.
The comparison is different from the regular string comparison that all numerical parts
of a string are treated as numbers

For example

010abc > 2abc
0005text > 3text
and
some-string.100 > some-string.20 

while with pure string comparison it will be different
