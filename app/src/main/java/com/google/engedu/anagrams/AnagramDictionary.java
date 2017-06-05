/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    //3.18.17
    ArrayList<String> wordList = new ArrayList<String>();
    HashMap<String, ArrayList<String> > lettersToWord = new HashMap<String, ArrayList<String> >();
    Set<String> wordSet = new HashSet<String>();
    HashMap<Integer, ArrayList<String> > sizeToWords = new HashMap<Integer, ArrayList<String>>();
    int wordLength = DEFAULT_WORD_LENGTH;
    //end
    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            //3.18.17
            wordList.add(word);
            wordSet.add(word);
            //another path (do not remove)
            /*if(sizeToWords.containsKey(word.length())){
                sizeToWords.get(word.length()).add(word);
            }else if (!sizeToWords.containsKey(word.length())){
                sizeToWords.put(word.length(), new ArrayList<String>());
                sizeToWords.get(word.length()).add(word);
            }*/
            String sorted = sortLetters(word);
            if(lettersToWord.containsKey(sorted)){
                lettersToWord.get(sorted).add(word);
            }else if(!lettersToWord.containsKey(sorted)){
                lettersToWord.put(sorted, new ArrayList<String>());
                lettersToWord.get(sorted).add(word);
            }
        }
    }
    //3.18.17 Sorts letter of word in alphabetical order
    private String sortLetters(String sortString){
        char[] toSort = sortString.toCharArray();
        Arrays.sort(toSort);
        String sorted = new String(toSort);
        return sorted;
    }
    //3.18.17 check if target word is valid  by checking hashset/dictionary & if the original word is not a substring of target word
    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && (!word.contains(base));
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        //3.18.17 Checks if targeted word in dictionary is an anagram
        for(int  i = 0; i < wordList.size(); i++){
            if( (targetWord.length() == wordList.get(i).length() ) && (sortLetters(wordList.get(i))).equals(sortLetters(targetWord)) ){
                result.add(targetWord);
            }
        }
        return result;
    }
    //3.18.17
    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String wordPlusLetter = "";
        for(char alphabet = 'a'; alphabet <= 'z'; alphabet++){
            wordPlusLetter = sortLetters(word+alphabet);
            if(lettersToWord.containsKey(wordPlusLetter)){
                //source: http://stackoverflow.com/questions/19541582/storing-and-retrieving-arraylist-values-from-hashmap
                //( addAll() method): http://stackoverflow.com/questions/11145931/put-arraylist-of-hashmap-into-another
                ArrayList<String> hashMapValues = lettersToWord.get(wordPlusLetter);
                for(String s : hashMapValues){
                    if(isGoodWord(s, word)){
                        result.add(s);
                    }
                }
            }
        }
        return result;
    }

    //choosing a word in arrayList with at least 5 anagrams
    public String pickGoodStarterWord() {
        String s = new String();
        ArrayList<String> aList = new ArrayList<String>();
        int r = random.nextInt(wordList.size());
        //source: http://stackoverflow.com/questions/18725428/going-back-to-the-first-index-after-reaching-the-last-one-in-an-array
        for(int i = r, j = 0; j < wordList.size()-1; i++, j++){ //start at random position
            i = i % (wordList.size()-1); //loop back to beginning of array if necessary
            String getWord = sortLetters(wordList.get(i));
            if(lettersToWord.containsKey(getWord)){
                if(lettersToWord.get(getWord).size() >= MIN_NUM_ANAGRAMS) {
                    aList.addAll(lettersToWord.get(getWord));
                    if (aList.size() >= MIN_NUM_ANAGRAMS) {
                        s = wordList.get(i);
                        break;
                    }
                }
            }
        }
        return s;
    }
}









