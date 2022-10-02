module main

__global name = 100

__global (
	sem   sync.Semaphore // needs initialization in `init()`
	mtx   sync.RwMutex // needs initialization in `init()`
	f1    = f64(34.0625) // explicily initialized
	shmap shared map[string]f64 // initialized as empty `shared` map
	f2    f64 // initialized to `0.0`
)

[markused]
__global g_main_argc = int(0)

[markused]
__global g_main_argv = unsafe { nil }

__global (
	intmap    map[string]int
	numberfns map[string]fn () int
	ch        chan f64
	mys       shared MyStruct
	sem       sync.Semaphore
	shmap     shared map[string]f64
	mtx       sync.RwMutex
	f1        = f64(545 / (sizeof(f64) + f32(8))) // directly initialized
	f2        f64
	test      = 0 // int
	testf     = 1.25 // f64
	testneg   = -2 // int
	testnegf  = -1.25e06 // f64
	testexpl  = f32(7)
	testfn    = get_u64()
	testarr   = []f64{len: 10, init: 2.75}
	testmap   = {
		'qwe': 2.5
		'asd': -7.25
		'yxc': 3.125
	}
	func1     = fn () {}
	func2     = fn (n int) int {
		return n
	}
	func3     = fn (n int) string {
		return '$n'
	}
)
