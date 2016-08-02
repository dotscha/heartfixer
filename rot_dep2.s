.proc	DepackRotTable

Rot = rs*256
RotX = Rot
RotY = Rot+128

prx = RotPacked
pry = RotPacked+32

c2:	ldy #0
i2 = * + 1;
	ldx #31
c1:
rx:	lda prx,y
wx1:	sta RotX,y
wx2:	sta RotX+32,x
	eor #63
wx3:	sta RotX+2*32,y
wx4:	sta RotX+3*32,x

ry:	lda pry,y
	pha
	clc
	adc #rs
wy1:	sta RotY,y
wy2:	sta RotY+3*32,x
	pla
	eor #63
	clc
	adc #rs
wy3:	sta RotY+32,x
wy4:	sta RotY+2*32,y

	iny
	dex
	bpl c1
	inc wx1+2
	inc wx2+2
	inc wx3+2
	inc wx4+2
	inc wy1+2
	inc wy2+2
	inc wy3+2
	inc wy4+2
	clc
	lda rx+1
	adc #64
	sta rx+1
	bcc n1
	inc rx+2
	clc
n1:	lda ry+1
	adc #64
	sta ry+1
	bcc n2
	inc ry+2
n2:	dec i1
i1 = * + 1
	lda #32
	bne c2

c4:	ldx #0
c3:
rx5:	lda RotX,x
	eor #127
wx5:	sta RotX+63*256,x
ry5:	lda RotY,x
wy5:	sta RotY+63*256,x
	inx
	bpl c3
	inc rx5+2
	inc ry5+2
	dec wx5+2
	dec wy5+2
	dec i2
	bpl c4
	rts

.endproc
