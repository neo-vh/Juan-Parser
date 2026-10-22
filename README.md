# Juan-Parser
## custom json parser bcs maven and gradle suck

*(read and write, currently only read and write test files you can change the files they read hardcoded in the code itself or remove the main methods to use them)*

- JsonParse contains a readFile helper

- JsonRead must be declared with a filepath it will then read the contents and allow you to call fromJson() it provides a hashmap containing <String, Object> the hashmap acts as the file string acts as every key inside the    top level and object is every value

- JsonWrite takes a hashmap <String, Object> and formats it to json formatting then writes it to the specified json file

## How to use methods

- JsonRead
  - ```java
    JsonRead reader = new JsonRead("Your file path as a string");
    reader.fromJson();
    ```
- JsonWrite
  - ```java
    JsonWrite writer = new JsonWrite("Your file path as a string");
    writer.write(Map to write);
    ```

## What to expect from each method

### [JsonRead](./JsonRead.java)
***JsonRead's .fromJson method returns the entire file as one Java hashmap containing all keys and values nested or not, it will***

- Read the file top to bottom
- check for start of keys (")
- if key encountered it reads it and moves to check for the colon indicating the value
- when colon is encountered it moves to the value and reads it calls dispatch parser which calls the needed parser for the identified data type
- once the entire key value pair is read and consumed it adds it to the Map and moves to the next key
- Uses a global position throughout the file to retain position context and keeps a final version of the string file contents for parsing

### [JsonWrite](./JsonWrite.java)
***JsonWrite's .write method takes a Hashmap and replaces the current file contents with that map, it will*** 

- takes a hashmap 
- loops through each entry of that map and identifies the key
- checks the type of the value associated with that key
- applies the type's formatting (hello -> "hello")
- writes a single string to the file containing the formatted key value pairs

### [JsonParse](./JsonParse.java)
JsonParse conains utility and although the methods inside are really only helpers they are used a bunch, it contains

- **readFile**
  - takes a file path and returns the contents as a string
  - ```java
    String fileContents = JsonParse.readFile(filePath as a string);
    ```



## Purpose of this Project

While practicing java Maven and Gradle required me to change my entire folder structure to use a json parser and I was too lazy

- Make a custom json parser to avoid gradle or maven for small projects
- Make it reliable while still learning Java (both of us)
- Fun

***

***This was written by 2 guys with a combined Java experience of 3 weeks over the span of 1 week it barely works***

**ps. juan IS hom**
