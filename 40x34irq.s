;ColorPage0 = $0800
;ColorPage1 = $4000

.proc   InitIRQ

	sei
        lda #<irq0
        sta $fffe
        lda #>irq0
        sta $ffff

        lda #$18
        sta $ff0b
        lda #$03
        sta $ff0a

        lda $ff09
        sta $ff09
        cli
        rts

;$0118 -> $0136
;$0000 -> $0002
;$0058 -> $0010
;$00cd -> $00f5


irq0:
        pha
val = *+1
        lda #$36
        sta $ff1d
w:      lda $ff1d
        bne w
        lda #2
        sta $ff1d
        sta $ff0a
        lda #$c8
        sta $ff0b

	lda #$ff	;cursor to $3ff
	sta $ff0c
	sta $ff0d

        lda #>ColorPage0
        sta $ff14

	stx tempx
	sty tempy
        jsr playMusic
tempx = *+1
        ldx #0
tempy = *+1
        ldy #0

        lda #<irq1
out:    sta $fffe
        lda $ff09
        sta $ff09

        pla
        rti

irq1:
        pha
        lda #$80
        sta $ff1d
        lda #$cd
        sta $ff0b
        lda #>ColorPage1
        sta $ff14

	lda #0		;cursor to $000
	sta $ff0c
	sta $ff0d

        lda #<irq2

        jmp out

irq2:
        pha
        lda #$f5
        sta $ff1d
        lda #$03
        sta $ff0a
        lda #$18
        sta $ff0b
        lda #<irq0

        jmp out

.if ((>irq0 <> >irq1) || (>irq0 <> >irq2))
	.error "irq entries should be in one page"
	.error .string(<irq0)
	.error .string(<irq1)
	.error .string(<irq2)
.endif

.endproc
