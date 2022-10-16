module parser

// notification_at returns the jsonrpc.Notification<T> in a given index.
pub fn (rw &TestStream) notification_at<T>(idx int) ?jsonrpc.NotificationMessage<T> {
	raw_json_content := rw.notif_buf[idx].bytestr().all_after('\r\n\r\n')
	return json.decode(jsonrpc.NotificationMessage<T>, raw_json_content)
}

