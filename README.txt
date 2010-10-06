Downloads HTTP Live Streaming transport streams.

http://tools.ietf.org/html/draft-pantos-http-live-streaming-04

Supports encrypted and clear streams.

usage: download-hls [options...] <url>
 -h,--help                     print this message.
 -iv <iv>                      use this AES-128 IV for the stream.
 -k,--key <key>                use this AES-128 key for the stream.
 -o,--out-file <output file>   file to write joined transport streams to.
 -s                            silent mode.
