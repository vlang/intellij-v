module annotators

a := if true {
	10
} else {

}

b := if 100 > 200 {
	200
} else {

}

c := if 100 > 200 {
	200
} else if 200 > 200 {
	100
} else {

}

d := if 100 > 200 {
	200
} else if 200 > 200 {
	100
} else {
	300
}

println(if 100 > 200 {
	200
} else {

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
