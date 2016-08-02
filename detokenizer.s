;----------------------------------------------------
;READ:
;       header: byte
;               3-0 bit : mely pixeleket hagyjuk meg
;               5-4 bit : hany LDA/ORA van
;               7-6 bit : zero
;       data: (hi/lo)*
;
;WRITE:
;       data: word (hi/lo)
;               a 7. bit 1
;
;END:   0
;
; az als¢ byteok src2-t“l, a t”bbi src1-t“l!!!
;----------------------------------------------------


;       src1: a speedcode tokenlistara mutato pointer (hi)
;       src2: a speedcode tokenlistara mutato pointer (lo)
;       dest: ide irjuk a speedcodeot

.proc   makeBitshaderCode

OP_sta_w_x      = $9d
OP_lda_w        = $ad
OP_ora_w        = $0d
OP_and_b        = $29

loop:
        jsr read_src1
        cmp #0
        bne cont

        lda #$60
        jmp write_dest

cont:   pha
        cmp #127
        bmi write_load

write_save:
        lda #OP_sta_w_x
        jsr write_dest
        jsr read_src2
        jsr write_dest
        pla
        eor #(128 ^ >Bitmap)
        jsr write_dest
        jmp loop

write_load:

        and #15
        beq bit_add     ;mindent bitet torlunk
        tax
        lda bitmask,x
        beq bit_or      ;minden bitet megtartunk

        pha
        lda #OP_and_b
        jsr write_dest
        pla
        jsr write_dest

bit_or: lda #OP_ora_w
        bne co

bit_add:lda #OP_lda_w
co:     sta first_opcode
        pla
        and #15*16
        bne :+
        jmp loop
:       lsr
        lsr
        lsr
        lsr
        tax

first_opcode=*+1
:       lda #0
        jsr write_dest
        jsr read_src2
        jsr write_dest
        jsr read_src1
        jsr write_dest
        lda #OP_ora_w
        sta first_opcode
        dex
        bne :-
        jmp loop

bitmask:
.byte %00000000
.byte %00000011
.byte %00001100
.byte %00001111
.byte %00110000
.byte %00110011
.byte %00111100
.byte %00111111
.byte %11000000
.byte %11000011
.byte %11001100
.byte %11001111
.byte %11110000
.byte %11110011
.byte %11111100
.byte 0

.endproc

write_dest:
dest=*+1
         sta speedcode
         inc dest
         bne :+
         inc dest+1
:        rts

read_src1:
src1=*+1
         lda tok_hi
         inc src1
         bne :+
         inc src1+1
:        rts

read_src2:
src2=*+1
         lda tok_lo
         inc src2
         bne :+
         inc src2+1
:        rts
