package com.kane.test;

import java.util.ArrayList;
import java.util.List;

public class Test7 {

    public static void main(String[] args) {
        String str = "1742088298474569730,1742088605264445442,1742119683685548033,1742409480828289026,1742465551729491970,1742465635150004225,1743237909488271361,1743839758683668481,1744040092440264705,1744040756427948034,1744041416277557249,1744060183770497025,1744924839994826753,1744974399821815810,1745064793158950913,1745670508396486658,1745779371426332674,1745843490338672641,1745843793579180034,1745875240893513730,1745971930924617730,1746373305001086978,1746461442780659714,1746461627229372417,1746817773790265345,1746870588369764354,1746896545046560770,1746922101238628354,1747062565576413186,1747102027001925634,1747151479553884161,1747177207922130945,1747217050751766529,1747373822179315713,1747390736905572354,1747418287241728002,1747432616049545218,1747465151433445377,1747465303976804354,1747466187053240321,1747471439890518018,1747505817807716353,1746055892557332481,1747194688979374082";
        List<Long> list = new ArrayList<>();
        // SPLIT str by "," add list
        for (String s : str.split(",")) {
            list.add(Long.parseLong(s));
        }

        String str2 = "1742088298474569730,1742088605264445442,1742119683685548033,1742409480828289026,1742465551729491970,1742465635150004225,1743237909488271361,1743839758683668481,1744040092440264705,1744040756427948034,1744041416277557249,1744060183770497025,1744924839994826753,1744974399821815810,1745064793158950913,1745670508396486658,1745779371426332674,1745843490338672641,1745843793579180034,1745875240893513730,1745971930924617730,1746373305001086978,1746461442780659714,1746461627229372417,1746817773790265345,1746870588369764354,1746896545046560770,1746922101238628354,1747062565576413186,1747102027001925634,1747151479553884161,1747177207922130945,1747217050751766529,1747373822179315713,1747390736905572354,1747418287241728002,1747432616049545218,1747465151433445377,1747465303976804354,1747466187053240321,1747471439890518018,1747505817807716353,1746055892557332481,1747194688979374082";
        List<Long> list2 = new ArrayList<>();
        // SPLIT str by "," add list
        for (String s : str2.split(",")) {
            list2.add(Long.parseLong(s));
        }

        // list1 list2  intersection
        list.retainAll(list2);
        System.out.println(list);
    }
}
