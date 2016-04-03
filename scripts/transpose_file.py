#!/bin/bash
python -c "import sys; print('\n'.join('\t'.join(c) for c in zip(*(l.split() for l in sys.stdin.readlines() if l.strip()))))" < $1 > $2

