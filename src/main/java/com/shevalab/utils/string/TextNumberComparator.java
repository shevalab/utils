package com.shevalab.utils.string;

import java.util.Comparator;

public class TextNumberComparator implements Comparator<String> {
    public TextNumberComparator() {
    }

    public int compare(String s1, String s2) {
        TextNumberTokenizer t1 = new TextNumberTokenizer(s1);
        TextNumberTokenizer t2 = new TextNumberTokenizer(s2);

        while(t1.hasNextToken() && t2.hasNextToken()) {
            int cmp = t1.getNextToken().compareTo(t2.getNextToken());
            if (cmp != 0) {
                return cmp;
            }
        }

        if (!t1.hasNextToken() && !t2.hasNextToken()) {
            return 0;
        } else {
            return t1.hasNextToken() ? 1 : -1;
        }
    }
}