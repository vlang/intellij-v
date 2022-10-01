module parser

// array returns `Any` as an array.
pub fn (a Any) array() []Any {
	if a is []Any {
		return a
	} else if a is map[string]Any {
		mut arr := []Any{}
		for _, v in a {
			arr << v
		}
		return arr
	}
	return [a]
}

fn test_parse_compact_text() {
	toml_doc := toml.parse_text(toml_text) or { panic(err) }

	title := toml_doc.value('title')
	assert title == toml.Any('TOML Example')
	assert title as string == 'TOML Example'

	database := toml_doc.value('database') as map[string]toml.Any
	db_serv := database['server'] or {
		panic('could not access "server" index in "database" variable')
	}
	assert db_serv as string == '192.168.1.1'

	assert toml_doc.value('owner.name') as string == 'Tom Preston-Werner'

	assert toml_doc.value('database.server') as string == '192.168.1.1'

	database_ports := toml_doc.value('database.ports') as []toml.Any
	assert database_ports[0] as i64 == 8000
	assert database_ports[1] as i64 == 8001
	assert database_ports[2] as i64 == 8002
	assert database_ports[0].int() == 8000
	assert database_ports[1].int() == 8001
	assert database_ports[2].int() == 8002

	assert toml_doc.value('database.connection_max') as i64 == 5000
	assert toml_doc.value('database.enabled') as bool == true

	assert toml_doc.value('servers.alpha.ip').string() == '10.0.0.1'
	assert toml_doc.value('servers.alpha.dc').string() == 'eqdc10'

	assert toml_doc.value('servers.beta.ip').string() == '10.0.0.2'
	assert toml_doc.value('servers.beta.dc').string() == 'eqdc10'

	clients_data := (toml_doc.value('clients.data') as []toml.Any)
	// dump(clients_data)
	// assert false
	gamma_delta_array := clients_data[0] as []toml.Any
	digits_array := clients_data[1] as []toml.Any
	assert gamma_delta_array[0].string() == 'gamma'
	assert gamma_delta_array[1].string() == 'delta'
	assert digits_array[0].int() == 1
	assert digits_array[1].int() == 2

	clients_hosts := (toml_doc.value('clients.hosts') as []toml.Any).as_strings()
	assert clients_hosts[0] == 'alpha'
	assert clients_hosts[1] == 'omega'
}
