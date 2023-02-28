module annotators

mp := map[string]int{}

<caret><error descr="Specify both `key` and `value` variables to iterate over `map[string]int`, use `_` to ignore the key">for</error> value in mp {

}

for key, value in mp {

}