
.org $4000-2
.word $4000

        sei
        sta $ff3f

        ;copy music

        ldx #$10
        ldy #0

c:      lda music,y
        sta $1000,y
        iny
        bne c
        inc c+2
        inc c+5
        dex
        bne c

        ;clear zeropage

        lda #0
        ldx 2
:       sta $0000,x
        inx
        bne :-

        jsr $1000

cc:
        lda #$c0
:       cmp $ff1d
	bne :-

	jsr $1003
	jmp cc


music = *+2
.incbin "music.prg"
