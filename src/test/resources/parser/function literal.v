module main

fn main() {
	a := fn (a i64) i64 {
		return 100
	}(100)

	if ctx.err() is none {
		go fn (mut ctx TimerContext, dur time.Duration) {
			ctx.cancel(true, deadline_exceeded)
		}(mut ctx, dur)
	}

	cancel_fn := fn [mut ctx] () {
		ctx.cancel(true, canceled)
	}
}

fn test_with_value() {
	f := fn (ctx context.Context, key context.Key) &Value {
		if value := ctx.value(key) {
			match value {
				Value {
					return value
				}
				else {}
			}
		}
		return not_found_value
	}

	key := 'language'
	value := &Value{
		val: 'VAL'
	}
	ctx := context.with_value(context.background(), key, value)

	assert value == f(ctx, key)
	assert not_found_value == f(ctx, 'color')
}
