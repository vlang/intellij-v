module annotators

interface IFoo {}

type Name = <error descr="Sum type cannot hold an interface types">IFoo</error> | string
type Name2 = []IFoo | string
type Name3 = map[string]IFoo | string
type Name4 = IFoo
