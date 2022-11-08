module new

a := 100
b := 100

lock a, b; rlock a; lock {

}

lock {
	println("a");
}

lock a {
	println("b");
}