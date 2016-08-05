.macpack cpu
.if !(.cpu .bitand CPU_ISET_6502X)
	.error "We need 6502X for illegal opcodes!"
.endif

.org $2000-2
.incbin "tables.prg"

        jmp player

tok_hi:	;0cf7
.include "speedcode_tok1.s"
.byte 0

tok_lo:	;11b9
.include "speedcode_tok2.s"
.byte 0

.align 256
.include "40x34irq.s"

Bitmap = $2000

ColorPage0 = $0800
ColorPage1 = $2000

; b800-bc00 : szabad!

.proc	player

next:	jsr readSP
        bne cikl

memconf:
        jsr readSP
        sta addr
        jsr readSP
        sta addr+1
        jsr readSP
addr=*+1
	sta $ffff
	jmp next

cikl:	sta counter
	jsr readSP
	sta routine
	jsr readSP
	sta routine+1
routine=*+1
c:	jsr $ffff
	dec counter
	bne c
	jmp next

counter:
	.byte 0
.endproc



.proc	beginDemo

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

        jsr DepackRotTable

        jsr prepareTexture

        lda #<speedcode
        sta $02
        lda #>speedcode
        sta $03

        jsr MakeCalculatorAddrTable

        lda $02
        sta renderer_address
        lda $03
        sta renderer_address+1

        jsr Make3AxRotRenderer

	jsr waitRetrace

        ;start music
	jsr initMusic
	jsr initMusicIrq
	cli


        jsr startOut

        lda $ff06
        and #$ef
        sta $ff06

	lda #0
	ldx #>ColorPage0
	jsr grClear
	lda #$a0
	ldx #4+>ColorPage0
	jsr grClear


        lda $ff06
        ora #$10
        sta $ff06

        rts

.endproc


.proc	initMusicIrq

	lda #<musicIrq
	sta $fffe
	lda #>musicIrq
	sta $ffff

	lda #2
	sta $ff0a
	sta $ff0b

	lda $ff09
	sta $ff09
	rts
.endproc


.proc	musicIrq

	sta tempa
	stx tempx
	sty tempy

	jsr playMusic

	lda $ff09
	sta $ff09

tempa = *+1
	lda #0
tempx = *+1
	ldx #0
tempy = *+1
	ldy #0
	rti

.endproc


.proc	initMusic
        lda #0
	jsr $1000
	lda $fb
	sta temp_fb
	lda $fc
	sta temp_fc
	rts

.endproc

temp_fb: .byte 0
temp_fc: .byte 0

.proc	playMusic

	lda $fb
	pha
	lda $fc
	pha

;	lda $ff07
;	eor #2
;	sta $ff07

	lda temp_fb
	sta $fb
	lda temp_fc
	sta $fc

	jsr $1003

	lda $fb
	sta temp_fb
	lda $fc
	sta temp_fc

	pla
	sta $fc
	pla
	sta $fb

	rts

.endproc


.proc	prepare3AxRotEffects

        lda $ff06
        and #$ef
        sta $ff06

	lda #0
	ldx #>ColorPage1
	jsr grClear
	lda #$a0
	ldx #4+>ColorPage1
	jsr grClear

        jsr gprintPage

        jsr InitIRQ

        lda #$20
        sta rot1
        sta rot2
        lda #$80+18
        sta rot3

        ldx #100
        jsr waitNRetrace

        rts
.endproc


.proc	restartEffext1

	lda #$a0
	ldx #4+>ColorPage0
	jsr grClear
	lda #$a0
	ldx #4+>ColorPage1
	jsr grClear

	lda #<angtab1
	sta angtab1_hi
	lda #>angtab1
	sta angtab1_hi+1
	lda #0
	sta dangtab1_lo
	lda #2
	sta dangtab1_hi
	ldy #0
        jmp MakeCalculators

.endproc


.proc	effect3AxRotComeOut

	lda #<angtab1
	sta angtab1_hi
	lda #>angtab1
	sta angtab1_hi+1

        sec
        lda dangtab1_lo
        sbc #32
        sta dangtab1_lo
        lda dangtab1_hi
        sbc #0
        sta dangtab1_hi

	ldy #0
        jsr MakeCalculators
        jmp effect3AxRot

.endproc


.proc	effect3AxRotComeIn

c = *+1
	lda #$1
	lsr
	lsr
	tax
	lda fades,x
	sta $ff15
	inc c

	lda #<angtab1
	sta angtab1_hi
	lda #>angtab1
	sta angtab1_hi+1
	ldy #0
        jsr MakeCalculators
        clc
        lda dangtab1_lo
        adc #16
        sta dangtab1_lo
        lda dangtab1_hi
        adc #0
        sta dangtab1_hi

.endproc


effect3AxRot:
        jsr shiftColors
        jsr MakeShiftTables

renderer_address = *+1
        jsr $ffff

        lda $ff06
        ora #$10
        sta $ff06

	clc
	lda rot3
	adc rot3d
        and #127
	sta rot3

moveRot12:

	clc
	lda rot1
	adc rot1d
	sta rot1

	clc
	lda rot2
	adc rot2d
	sta rot2

        rts


effect3AxRot3:

	jsr shiftColors
	ldy rot3
	jsr textureRot
	jmp effect3AxRot+3

rot1d:
	.byte 1
rot2d:
	.byte 255
rot3d:
	.byte 1


.proc	saveRot
	lda rot1
	sta sa,x
	lda rot2
	sta sa+1,x
	lda rot1d
	sta sa+2,x
	lda rot2d
	sta sa+3,x
	rts
.endproc


.proc	restoreRot
	lda sa,x
	sta rot1
	lda sa+1,x
	sta rot2
	lda sa+2,x
	sta rot1d
	lda sa+3,x
	sta rot2d
	rts
.endproc


sa:	.byte 0,0,0,0
sb:	.byte 0,0,0,0


.proc	prep4Sphere

	lda #$51
	ldx #4+>ColorPage0
	jsr grClear
	lda #$51
	ldx #4+>ColorPage1
	jsr grClear
	ldy #1
	jmp MakeCalculators

.endproc


.proc	prepareEffect3

	lda #0
	sta $ff15

	ldy #2
        jsr MakeCalculators

	ldx #0
:	txa
	and #8
	asl
	asl
	asl
	asl
	sta texture2_y+rs,x
	inx
	cpx #64
	bne :-

	lda #$cf ;a0
	ldx #4+>ColorPage0
	jsr grClear
	lda #$cf ;a0
	ldx #4+>ColorPage1
	jsr grClear

	rts

.endproc


.proc initRotD
	jsr readSP
	sta rot1d
	jsr readSP
	sta rot2d
	jsr readSP
	sta rot3d
	rts
.endproc


.proc	theEnd
	lda #$44
	sta $ff19
	jmp *
.endproc


.proc   shiftColors

        ldx colors
        ldy #0
c:      lda sample_colors,y
        sta colors,y
        iny
        cpy #32
        bne c

        inc c+1
        bne :+
        inc c+2

:       lda c+1
        cmp #<restart
        bne :+
        lda c+2
        cmp #>restart
        bne :+
        lda #<sample_colors
        sta c+1
        lda #>sample_colors
        sta c+2

:       rts

.endproc


.proc   grClear
; XR
; AR
        stx sta0+1
        ldy #0
        ldx #4
l:
sta0 = *+1
        sta ColorPage0,y
        iny
        bne l
        inc sta0+1
        dex
        bne l
        rts

.endproc


readSP:
sp = *+1
	lda screen_play
	inc sp
	bne :+
	inc sp+1
:       rts


calc_lo = $0780
calc_hi = calc_lo+52

dist_tab = $2000
ang_tab  = $2000+34*40

.include "gprint.s"

.include "3axrot.s"

.include "bitshader.s"

.include "screenplay.s"

RenderObject:

speedcode:

Rot = $bd00
rs = Rot/256


.include "rot_dep2.s"

RotPacked:
.include "rot2.dat"


.include "pattern.dat"

music = *+2

;zeropage used: $fb-$fc
.incbin "music.prg"


