module main

fn main() {
    <error descr="Variable name cannot contain uppercase letters, use snake_case instead">InvalidVarName</error> := 1

   <error descr="Variable name cannot start with '_'">_with_</error> := 100 // error
   snake_case := 100 // ok
   _ := 100 // ok
}
