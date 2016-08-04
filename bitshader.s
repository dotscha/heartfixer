color0 = 0
color1 = $19
color2 = $49
color3 = $79

shift22 = $0200
shift12 = shift22+128
colors2 = shift12+64

powers  = $300

lightPow0 = $500
lightPow1 = $520


buffer0 = usable
buffer1 = usable+512


.proc   prepareBitshader

	jsr waitRetrace

        lda $ff06
        and #$ef
        sta $ff06

	jsr initMusicIrq

        jsr makeBitshaderCode


	lda #0
	ldy #0
	ldx #$20
:	sta $2000,y
	iny
	bne :-
	inc :- + 2
	dex
	bne :-

	jsr initColors

	lda #8
	sta $ff12

        lda #>ColorPage0
        sta $ff14

	lda $ff06
	ora #$30
	sta $ff06

	lda $ff07
	ora #$10
	sta $ff07

        jsr PreparePatterns     ;ver2x2


        lda #64
        sta rot1
        lda #0
        sta rot2

	ldx #0
        jsr CopyColorTable

        rts

.endproc


.proc	faceAnim1

c1:
        jsr MakeShiftTables2
        jsr CalcPowers

	jsr renderFace

cont:   jmp moveRot12

.endproc


.proc	prepareFaceAnim2

        ldx #0
        lda #0
        ldy #1
        jsr calcLightPow

        ldx #$20
        lda #0
        ldy #1
        jsr calcLightPow

        jsr saveToBuffer0
        jsr saveToBuffer1

        ldx #0
        jsr saveRot
        ldx #4
        jsr saveRot
	rts

.endproc


.proc	faceAnim2Out

	sec
	lda powlo
	sbc #1
	sta powlo
	lda powhi
	sbc #0
	sta powhi

        ldx #0
        lda powlo
        ldy powhi
        jsr calcLightPow

        ldx #$20
        lda powlo
        ldy powhi
        jsr calcLightPow

.endproc


.proc	faceAnim2

mod = *+1
	lda #0
	eor #1
	sta mod
	beq mod1

mod0:
	ldx #4
	jsr saveRot
	ldx #0
	jsr restoreRot

        jsr MakeShiftTables2
        jsr CalcPowers
        jsr saveToBuffer0
	ldx #64
	jsr CopyColorTable
        jmp end

mod1:
	ldx #0
	jsr saveRot
	ldx #4
	jsr restoreRot

        jsr MakeShiftTables2
        jsr CalcPowers
        jsr saveToBuffer1
	ldx #0
	jsr CopyColorTable

end:	jsr maxBuffers
	jsr renderFace
	jmp moveRot12

.endproc


.proc	faceAnimTest
c1:
        jsr MakeShiftTables2
        jsr CalcPowers

	jsr renderFace

	jmp c1

.endproc





powlo:	.byte 0
powhi:	.byte 0

counter:
	.byte 0

.proc	doFadeIn1

	jsr readSP
	sta rot1
	jsr readSP
	sta rot2

        jsr MakeShiftTables2
        jsr CalcPowers
        jsr saveToBuffer0

        lda #0
        sta powlo
        sta powhi
        lda #17
        sta counter

:
        ldx #0
        lda powlo
        ldy powhi
        jsr calcLightPow
	jsr copyBackBuffer0
        jsr renderFace

        dec counter
        beq e

        clc
        lda powlo
        adc #16
        sta powlo
        lda powhi
        adc #0
        sta powhi


	jmp :-

e:      jsr renderFace
        jmp renderFace

.endproc


.proc	doFadeInOut1
	jsr doFadeIn1
	ldx #95
	jsr waitNRetrace
.endproc


.proc	doFadeOut1
        lda #9
        sta counter

:
        ldx #0
        lda powlo
        ldy powhi
        jsr calcLightPow
	jsr copyBackBuffer0
        jsr renderFace


        dec counter
        beq e

        sec
        lda powlo
        sbc #32
        sta powlo
        lda powhi
        sbc #0
        sta powhi

        jmp :-

e:      jsr renderFace
        jmp renderFace

.endproc


.proc	doFadeInOut2

	jsr saveToBuffer1

	jsr readSP
	sta rot1
	jsr readSP
	sta rot2

        jsr MakeShiftTables2
        jsr CalcPowers
        jsr saveToBuffer0

        lda #0
        sta powlo
        sta powhi

        lda #17
        sta counter

:
        ldx #$0
        lda powlo
        ldy powhi
        jsr calcLightPow

        sec
        lda #0
        sbc powlo
        sta pow2lo
        lda #1
        sbc powhi
        sta pow2hi

        ldx #$20
pow2lo = *+1
        lda #0
pow2hi = *+1
        ldy #0
        jsr calcLightPow

	jsr addBuffers

        jsr renderFace

        dec counter
        beq e

        clc
        lda powlo
        adc #16
        sta powlo
        lda powhi
        adc #0
        sta powhi

	jmp :-

e:      jsr renderFace
        jmp renderFace

.endproc


.proc	renderFace

mod = * + 1

        lda #0
        eor #1
        sta mod
        bne mod1

mod0:   jsr CopyPatternsMod0
        ldx #0
        jmp RenderObject

mod1:   jsr CopyPatternsMod1
        ldx #1
        jmp RenderObject

.endproc


.proc   initColors

cc0 = (color2 & 250) + (color1 & 250)/16
cc1 = (color2 &  15) + (color1 &  15)*16

        lda #color0
        sta $ff15
        lda #color3
        sta $ff16

        ldx #4
        ldy #0
c:      lda #cc0
w0:     sta $0800,y
        lda #cc1
w1:     sta $0c00,y
        iny
        bne c
        inc w0+2
        inc w1+2
        dex
        bne c
        rts

.endproc


.proc   CopyColorTable

        ldy #0
c:      lda shade_sample,x
        sta colors2,y
        inx
        iny
        cpy #64
        bne c
        rts

.endproc


.proc   MakeShiftTables2

        lda rot1
        ldy #0
c1:     and #127
        sta shift12,y
        clc
        adc #4
        iny
        cpy #32
        bne c1

        lda rot2
        ldy #0
c2:     and #127
        sta shift22,y
        clc
        adc #1
        iny
        bpl c2

        rts

.endproc


.proc	saveToBuffer0

	ldy #0
:	lda powers,y
	sta buffer0,y
	lda powers+256,y
	sta buffer0+256,y
	iny
	bne :-
	rts

.endproc


.proc	saveToBuffer1

	ldy #0
:	lda powers,y
	sta buffer1,y
	lda powers+256,y
	sta buffer1+256,y
	iny
	bne :-
	rts

.endproc


.proc	copyBackBuffer0

	ldy #0

:	ldx buffer0,y
	lda lightPow0,x
	sta powers,y

	ldx buffer0+256,y
	lda lightPow0,x
	sta powers+256,y

	iny
	bne :-
	rts

.endproc


.proc	addBuffers

	ldy #0

:	ldx buffer0,y
	lda lightPow0,x
	ldx buffer1,y
	adc lightPow1,x
	sta powers,y

	ldx buffer0+256,y
	lda lightPow0,x
	ldx buffer1+256,y
	adc lightPow1,x
	sta powers+256,y

	iny
	bne :-
	rts

.endproc


.proc	maxBuffers

	ldy #0

c:	ldx buffer0,y
	lda lightPow0,x
	ldx buffer1,y

	cmp lightPow1,x
	bpl :+
	lda lightPow1,x

:	sta powers,y

	ldx buffer0+256,y
	lda lightPow0,x
	ldx buffer1+256,y

	cmp lightPow1,x
	bpl :+
	lda lightPow1,x

:	sta powers+256,y

	iny
	bne c
	rts

.endproc


.proc	calcLightPow

;XR = 0 v $20
;AR = lo
;YR = hi

	sta dlo
	sty dhi
	lda #0
	sta lo
	sta lightPow0,x
	ldy #12

:	clc
lo = *+1
	lda #0
dlo = *+1
	adc #0
	sta lo
	lda lightPow0,x
dhi = *+1
	adc #0
	sta lightPow0+1,x
	inx
	dey
	bne :-
	rts

.endproc


.proc   CalcPowers

.zeropage
xs = $ff
.code

RotX = Rot
RotY = Rot+128

p = $08

        lda #128
        sta p

        ldx #31
        stx xs

.repeat 16 , I

        ldy shift12,x
        lda RotY+(3*I+0)*256,y
        sta p+1
        ldx RotX+(3*I+0)*256,y
        ldy shift22,x

	lax (p),y		;.byte $b3,p
        lda colors2-rs,x
        ldx xs
        sta powers+I*32,x

.endrep

        ldx #29
beg:    stx xs

.repeat 16 , I

        ldy shift12,x
        lda RotY+(3*I+0)*256,y
        sta p+1
        ldx RotX+(3*I+0)*256,y
        ldy shift22,x

        lax (p),y		;.byte $b3,p
        lda colors2-rs,x
        ldx xs
        sta powers+I*32,x

        adc powers+I*32+2,x
        ror
        sta powers+I*32+1,x

.endrep
        dex
        dex
        bmi cont
        jmp beg

cont:
.repeat 16 , I
        sta powers+I*32+31
        adc powers+I*32+1
        ror
        sta powers+I*32
.endrep

        rts

.endproc



.include "shadepat.dat"


lo_bits  = Rot - $0c00
hi_bits  = Rot - $0a00
tup3bits = Rot - $0800
tup2bits = Rot - $0600
tup1bits = Rot - $0400
tup0bits = Rot - $0200

usable = lo_bits - 1024

shadebase = $0700

.include "ver2x2.s"

.include "detokenizer.s"

