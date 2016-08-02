
crBase = ColorPage0 + $0400
charsetBase = $1e90 ;$d000

w  = $04
cx = $06
cy = $07


.proc   startOut

        lda #$7e
        sta $ff15
        ldy #7
c:      ldx #20
        jsr waitNRetrace
        sec
        lda $ff15
        sbc #$10
        sta $ff15
        sta $ff19
        dey
        bne c
        ldx #20
        jsr waitNRetrace
        lda #0
        sta $ff15
        sta $ff19
        rts

.endproc



.proc   waitRetrace

        lda #$ff
c:      cmp $ff1d
        bne c
        rts

.endproc


.proc   waitNRetrace

c:      jsr waitRetrace
        dex
        bne c
        rts

.endproc


.proc   fadeIn

        ldy #0
c:      ldx #20
        jsr waitNRetrace
        lda fades,y
        sta $ff15
        iny
        cpy #9
        bne c
        rts

.endproc


.proc   fadeOut

        ldy #8
c:      ldx #20
        jsr waitNRetrace
        lda fades,y
        sta $ff15
        dey
        bpl c
        rts

.endproc


fades:
.byte $00,$01,$11,$21,$31,$41,$51,$61,$71

.proc   showPage

        lda #$00
        sta $ff15
        jsr gprintPage

        jsr fadeIn

        ldx #255
        jsr waitNRetrace
        ldx #255
        jsr waitNRetrace

        jsr fadeOut

	lda #$a0
	ldx #4+>ColorPage0
        jmp grClear

.endproc


.proc   gprintPage
; sp : a page leiras helye a memoriaban
        jsr readSP
        sta strings

c:      jsr readSP
        sta cx
        jsr readSP
        sta cy

        jsr gprinti
        dec strings
        bne c

        rts

strings:
        .byte 0

.endproc


.proc   gprinti
; sp : a string helye a memoriaban
        jsr readSP
        sta counter

c:      jsr readSP
        jsr gprintChar
        clc
        lda cx
        adc #8
        sta cx
        dec counter
counter = *+1
        lda #0
        bne c
        rts

.endproc



.proc   gprintChar

;A = a karakter kodja

        asl
        rol p+1
        asl
        rol p+1
        asl
        rol p+1
        clc
        adc #<charsetBase
        sta p
        lda p+1
        and #7
        adc #>charsetBase
        sta p+1
p = *+1
c:      lda $ffff
        jsr gprintByte
        inc cy
        inc p
        lda p
        and #7
        bne c
        sec
        lda cy
        sbc #8
        sta cy
        rts

.endproc


.proc   gprintByte

        ldx #8
        stx mx
c:      asl
        pha
        bcc bit0
bit1:   jsr plot
bit0:   inc cx
        pla
        dec mx
mx = *+1
        ldx #0
        bne c
        sec
        lda cx
        sbc #8
        sta cx
        rts

.endproc

plot = plot4x4

.proc   plot4x4

        ldy cy
        lsr cy
        rol
        ldx cx
        lsr cx
        rol
        pha
        jsr calc8x8CPos
        stx cx
        sty cy
        pla
        and #3
        tax
        ldy #0
	lda (w),y
c:	cmp ttchars,y
	beq co
	iny
	bne c
co:
	tya
	ora bits,x
	tax
	lda ttchars,x
	ldy #0
        sta (w),y
        rts

bits: .byte 1,2,4,8

;# 2x2 chars
ttchars:
.byte $a0,$fe,$fc,$62
.byte $fb,$e1,$7f,$6c
.byte $ec,$ff,$61,$7b
.byte $e2,$7c,$7e,$20

.endproc


.proc   calc8x8CPos
        ;w = crBase+(40*cy+cx)
        lda #0
        sta w+1
        lda cy
        asl
        asl
        adc cy
        asl
        rol w+1
        asl
        rol w+1
        asl
        rol w+1
        adc cx
        sta w
        lda w+1
        adc #>crBase
        sta w+1
        rts

.endproc

