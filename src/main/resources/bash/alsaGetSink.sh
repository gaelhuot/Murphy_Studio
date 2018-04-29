#!/bin/sh
pacmd list-sink-inputs | tr '\n' '\r' | perl -pe 's/ *index: ([0-9]+).+?application\.process\.id = "([^\r]+)"\r.+?(?=index:|$)/\2:\1\r/g' | tr '\r' '\n'