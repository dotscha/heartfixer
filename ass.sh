PATH=$PATH:$HOME/asl-current/
asl $1.asm -A -a -L -u -x
p2bin $1.p $1.prg -l 0 -k -r 0x-0x
