# Juan-Parser
## custom json parser bcs maven and gradle suck

*(read and write currently only read and write test files you can change the files they read hardcoded in the code itself or remove the main methods to use them)*

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

**ps. juan IS hom**
