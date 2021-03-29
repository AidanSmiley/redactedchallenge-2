package edu.utdallas;

import java.util.*;

public class ParseCheer {
    public static void main(String [] args) {
        int min = 1;
        String[] valid = {"cheer"};
        String message = "cheer1 cheer10001, cheer4";


        String[] cheers = solution(min, valid, message);

        for (int i = 0; i < cheers.length; i++) {
            System.out.println(cheers[i]);
        }
    }

    public static String[] solution(int min_cheermote_amount, String[] valid_cheermotes, String messages) {
        // Please write your code here.

        //using a hashmap with valid emotes for keys and each key maps to the total cheers used for that emote
        HashMap<String, Integer> cheers = new HashMap<>();

        //temp variable used for holding cheer in processing
        int amount = 0;

        //array to return when there were no valid cheers
        String[] noCheers = {"NO_CHEERS"};

        //create keys for the hashmap, initialize all their values to zero
        for (int i = 0; i < valid_cheermotes.length; i++) {
            cheers.put(valid_cheermotes[i], 0);
        }

        //spliting message at commas
        String[] messageArr = messages.split(",", -2);

        /*
        For each individual message tokenize each into another array split at whitespaces then operate
        on each token. Any cheer that is less than the minimum will disqualify the current message and cheers
        won't be counted.
        */
        for (int i = 0; i < messageArr.length; i++) {

            //split the current message at whitespaces into tokens
            String[] tokens = messageArr[i].split(" ", -2);

            //check current message for validity, if any token is less than the min or over 10k skip over it
            if (isValid(tokens, min_cheermote_amount)) {
                //goes through each token checking for digits
                for (int j = 0; j < tokens.length; j++) {

                    //get a cheer amount from the current token if it contains a digit
                    if (tokens[j].matches(".*\\d.*")) {
                        amount = numFromString(tokens[j]);

                        //gets just the emote from the current token
                        String currentEmote = getCheer(tokens[j]);

                        cheers.put(currentEmote, cheers.get(currentEmote) + amount);
                        //matches with a key if its in the hashmap
                        /*
                        if (cheers.containsKey(currentEmote)) {
                            cheers.replace(currentEmote, cheers.get(currentEmote) + amount);
                        }
                        */

                    }//end if
                }//end inner for
            }//end if
        }//end outer for

        //removes any cheers with a value of zero from the map
        Iterator<Map.Entry<String, Integer>> mapIterator = cheers.entrySet().iterator();

        while (mapIterator.hasNext()) {
            Map.Entry<String, Integer> entry = mapIterator.next();
            if (entry.getValue() == 0) {
                mapIterator.remove();
            }
        }

        if (cheers.isEmpty()) {
            return noCheers;
        }

        //final array with valid emotes and their cheers. Will be size of the cheers that are left.
        String[] totalCheers = new String[cheers.size()];

        //sort the hashmap by values
        Map<String, Integer> sortedCheers = valueSort(cheers);

        //contruct the final array for output by concatenating the key with its value
        int index = 0;
        for (Map.Entry<String, Integer> e: sortedCheers.entrySet()) {
            totalCheers[index] = e.getKey() + e.getValue();
            index++;
        }

        return totalCheers;
    }

    //takes in a string and parses it for the ints inside. Will return 0 if no digits in the string are found
    public static int numFromString(String toParse) {
        String intStr = "";

        //goes through string looking for digits in a cheermote. concatinates digits found into a single number
        for (int i = 0; i < toParse.length(); i++) {
            char currentChar = toParse.charAt(i);
            if (Character.isDigit(currentChar)) {
                intStr = intStr + currentChar;
            }
        }

        //empty string no digits so return 0
        if (intStr.equals("")) {
            return 0;
        }
        //non empty string meaning digits were found so return the string as an int
        else {
            return Integer.parseInt(intStr);
        }
    }

    public static String getCheer(String currMote) {
        String cheerMote = "";

        for (int j = 0; j < currMote.length(); j++) {
            //concatenates if there is a letter to make the cheermote
            if (Character.isLetter(currMote.charAt(j))) {
                cheerMote = cheerMote + currMote.charAt(j);
            }
        }//end for

        return cheerMote;
    }


    public static HashMap<String, Integer> valueSort(HashMap<String, Integer> map) {
        //create a list of the given map
        List<Map.Entry<String, Integer>> mapList = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());

        //sort mapList by the values
        Collections.sort(mapList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o1.getValue().compareTo(o2.getValue()));
            }
        });

        //default sort is increasing
        Collections.reverse(mapList);

        HashMap<String, Integer> sorted = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> e: mapList) {
            sorted.put(e.getKey(), e.getValue());
        }

        return sorted;
    }

    public static boolean isValid(String[] arr, int min) {
        boolean valid = true;
        int amount = 0;
        int msgTotal = 0;

        for (int i = 0; i < arr.length && valid; i++) {
            if (arr[i].matches(".*\\d.*")) {
                amount = numFromString(arr[i]);

                msgTotal += amount;

                if (amount < min || amount > 10000 || msgTotal > 100000) {
                    valid = false;

                }
            }
        }

        return valid;
    }
}
