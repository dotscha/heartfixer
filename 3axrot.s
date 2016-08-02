;calc_lo:
;calc_hi:

rot1 = $04
rot2 = $05
rot3 = $06

p = $02

shift1 = $80
shift2 = $200

texture_y = $0400

texture2_y = $0500
colors3 = $0600

texture = Rot-$2000


RotX = Rot
RotY = Rot + 128


.proc   prepareTexture

temp = $0700

        ldx #32
c2:     ldy #0
c10:
src=*+1
        lda patt31,y
        pha
        sta temp,y
        sta temp+64,y
        iny
        cpy #32
        bne c10

c11:    pla
dest2=*+1
        sta temp,y
dest3=*+1
        sta temp+64,y
        iny
        cpy #64
        bne c11

        ldy #0
c12:    lda temp,y
dest0=*+1
        sta texture+31*256,y
dest1=*+1
        sta texture+31*256+128,y
        iny
        bpl c12

        sec
        lda src
        sbc #32
        sta src
        lda src+1
        sbc #0
        sta src+1
        dec dest0+1
        dec dest1+1
        dex
        bne c2

        ldx #0
        ldy #>texture
c3:     tya
        sta texture_y+rs,x
        iny
        inx
        cpx #32
        bne c3
        dex
        ldy #>texture
c4:     tya
        sta texture_y+rs+32,x
        iny
        dex
        bpl c4

        rts

.endproc


.proc   MakeShiftTables

        lda rot1
        ldy #0
c1:     and #127
        sta shift1,y
        clc
        adc #1
        iny
        bpl c1

        lda rot2
        ldy #0
c2:     and #127
        sta shift2,y
        clc
        adc #1
        iny
        bpl c2

        rts

.endproc


.macro	colCycle color
	.byte $00+color,$10+color,$20+color,$30+color,$40+color,$50+color,$60+color,$70+color,$70+color,$60+color,$50+color,$40+color,$30+color,$20+color,$10+color,$00+color
.endmac


colors:
	colCycle 1
	colCycle 4

sample_colors:

	colCycle 1
	colCycle 4
	colCycle 5
	colCycle 12
	colCycle 6
	colCycle 11
	colCycle 2
	colCycle 10
	colCycle 3
	colCycle 7
	colCycle 14
	colCycle 8
	colCycle 13
	colCycle 9
	colCycle 15
restart:
	colCycle 1
	colCycle 4



.proc	MakeCalculatorAddrTable


        ldx #0

loop:
        lda p
        sta calc_lo,x
        lda p+1
        sta calc_hi,x

	clc
	lda p
	adc #36
	sta p
	lda p+1
	adc #0
	sta p+1

        inx
        cpx #52
        bne loop
        rts

.endproc



.proc   MakeCalculators
	; YR : calculator sorszama

        lda code_beg_lo,y
        sta sample_code_beg
        lda code_beg_hi,y
        sta sample_code_beg+1
        lda code_length,y
        sta sample_code_length
        lda code_update_lo,y
        sta code_update
        lda code_update_hi,y
        sta code_update+1

        ldx #0
loop:
        lda calc_lo,x
        sta p
        lda calc_hi,x
        sta p+1

code_update=*+1
	jsr code_update1

        ldy #0
sample_code_beg=*+1
c:      lda sample_code_beg1,y
        sta (p),y
        iny
sample_code_length=*+1
        cpy #(code_update1-sample_code_beg1)
        bne c

        inx
        cpx #52
        bne loop
        rts

.endproc

;### tablazatok

code_beg_lo:
	.byte <sample_code_beg1,<sample_code_beg2,<sample_code_beg3

code_beg_hi:
	.byte >sample_code_beg1,>sample_code_beg2,>sample_code_beg3

code_length:
	.byte <(code_update1-sample_code_beg1)
	.byte <(code_update2-sample_code_beg2)
	.byte <(code_update3-sample_code_beg3)

code_update_lo:
	.byte <code_update1,<code_update2,<code_update3

code_update_hi:
	.byte >code_update1,>code_update2,>code_update3


;### sample codeok

sample_code_beg1:

t = rot3

tr0 = *+1
        ldy RotY,x
        lda texture_y,y
        sta t+1
tr1 = *+1
        ldy RotX,x
        .byte $b3,t     ;   ldax (t),y
        lda colors,x
        rts

code_update1:
angtab1_hi = *+1
	lda angtab1
	sta tr0+1
        sta tr1+1
        clc
angtab1_lo = *+1
        lda #0
dangtab1_lo = *+1
        adc #16
        sta angtab1_lo
	lda angtab1_hi
dangtab1_hi = *+1
	adc #0
	sta angtab1_hi
	bcc :+
	inc angtab1_hi+1
:	rts

angtab1:
.include "stereog.s"


sample_code_beg2:

tr02 = *+1
       lda RotY,x
tr12 = *+1
       ldy RotX,x
sta0 = *+1
       sta ld0+1
sta1 = *+1
       sta ld1+1
       ldx shift2,y
ld0 = *+1
       ldy RotY,x
       lda texture_y,y
       sta t+1
ld1 = *+1
       ldy RotX,x
       .byte $b3,t     ;   ldax (t),y
       lda colors,x
       rts


code_update2:
	lda sphere,x
	beq setBlack
	lda sphere,x
	sta tr02+1
	sta tr12+1
	clc
	lda calc_lo,x
	adc #<(ld0+1-sample_code_beg2)
	sta sta0
	lda calc_hi,x
	adc #>(ld0+1-sample_code_beg2)
	sta sta0+1
	clc
	lda sta0
	adc #<(ld1-ld0)
	sta sta1
	lda sta0+1
	adc #>(ld1-ld0)
	sta sta1+1
        rts

setBlack:
	lda black
	sta sample_code_beg2
	lda black+1
	sta sample_code_beg2+1
	lda black+2
	sta sample_code_beg2+2
	rts

black:
	lda #0
	rts

sphere:
.include "sphere.s"



sample_code_beg3:

tr03 = *+1
       lda RotY,x
tr13 = *+1
       ldy RotX,x
sta03 = *+1
       sta ld0+1
sta13 = *+1
       sta ld1+1
       ldx shift2,y
ld03 = *+1
       ldy RotY,x

       lda texture2_y,y
ld13 = *+1
       adc RotX,x
       tax
       lda colors3,x
       rts


code_update3:
	txa
	asl
	tax
	lda angtab1,x
	sta tr03+1
	sta tr13+1
	txa
	lsr
	tax
	clc
	lda calc_lo,x
	adc #<(ld03+1-sample_code_beg3)
	sta sta03
	lda calc_hi,x
	adc #>(ld03+1-sample_code_beg3)
	sta sta03+1
	clc
	lda sta03
	adc #<(ld13-ld03)
	sta sta13
	lda sta03+1
	adc #>(ld13-ld03)
	sta sta13+1
        rts


.proc	textureRot

	ldx #0
:	tya
	pha
	and #31
	tay
	lda colors,y
	sta colors3,x
;	tya
;	eor #16
;	tay
;	lda colors,y
	eor #$0f
	sta colors3+128,x
	pla
	tay
	iny
	inx
	bpl :-
	rts

.endproc


;-----------------------------------
;       ang_tab:
;       dist_tab:
;-----------------------------------
.proc   Make3AxRotRenderer

p = $02

cy = *+1
        ldy #0
line_loop:
        lda line_hi,y
        sta write+1
        and #%11111100
        sta mask

        ldx #0

loop:
ang_addr = *+1
        lda ang_tab
        clc
        adc #<shift1
        sta ang
dist_addr = *+1
        ldy dist_tab
        lda calc_lo,y
        sta calc
        lda calc_hi,y
        sta calc+1

        inc ang_addr
        bne co1
        inc ang_addr+1
co1:    inc dist_addr
        bne co2
        inc dist_addr+1

co2:    ldy #0
c:      lda sample_code_beg,y
        sta (p),y
        iny
        cpy #<(sample_code_end-sample_code_beg)
        bne c
        clc
        tya
        adc p
        sta p
        bcc co3
        inc p+1
        clc
co3:    lda write
        adc #1
        sta write
        lda write+1
        adc #0
        and #3
mask = *+1
        ora #0
        sta write+1
        inx
        cpx #40
        beq co4
        jmp loop

co4:    inc cy
        ldy cy
        cpy #34
        beq co5
        jmp line_loop

co5:    lda #$60        ;rts
        ldy #0
        sta (p),y
        inc p
        bne e
        inc p+1
e:      rts

sample_code_beg:

ang = *+1
        ldx $00
calc = *+1
        jsr $0000
write = *+1
        sta ColorPage0

sample_code_end:



line_hi:
.repeat 25, i
        .byte >(ColorPage0+40*i)
.endrep
        .byte 3+>ColorPage1
.repeat 34-26,i
        .byte >(ColorPage1+40-24+40*i)
.endrep

.endproc

