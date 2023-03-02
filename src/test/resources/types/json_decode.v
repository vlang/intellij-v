module types

import json

fn main() {
	expr_type(json.decode(string, ''), '!string')
	expr_type(json.decode([]string, ''), '![]string')
	expr_type(json.decode(map[string]string, ''), '!map[string]string')
	expr_type(json.decode([]map[string]string, ''), '![]map[string]string')
}
