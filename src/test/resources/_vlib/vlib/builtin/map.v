module builtin

pub struct map {}

pub fn (mut m map) move() map

// Returns all keys in the map.
pub fn (m &map) keys() array

pub fn (m &map) values() array

[unsafe]
pub fn (m &map) clone() map
