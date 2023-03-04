module annotators

a := <error descr="If must have an else branch when used as an expression">if</error> true {
	10
}

b := <error descr="If must have an else branch when used as an expression">if</error> 100 > 200 {
	200
}

c := if 100 > 200 {
	200
} else <error descr="If must have an else branch when used as an expression">if</error> 200 > 200 {
	100
}

d := if 100 > 200 {
	200
} else if 200 > 200 {
	100
} else {
	300
}

println(<error descr="If must have an else branch when used as an expression">if</error> 100 > 200 {
	200
})

println(if 100 > 200 {
	200
} else {
	200
})

if 100 > 200 {
}

if 100 > 200 {
	println(100)
} else if 200 > 200 {
	println(200)
}
