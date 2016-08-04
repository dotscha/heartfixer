.macro conv ch0,ch1,ch2,ch3,ch4,ch5,ch6,ch7,ch8,ch9
        .byte .paramcount
        conv_ ch0,ch1,ch2,ch3,ch4,ch5,ch6,ch7,ch8,ch9
.endmac

.macro conv_ ch0,ch1,ch2,ch3,ch4,ch5,ch6,ch7,ch8,ch9
        .ifblank ch0
                .exitmacro
        .endif
        cC ch0

        conv_ ch1,ch2,ch3,ch4,ch5,ch6,ch7,ch8,ch9
.endmac

.macro cC ch    ; convChar
                .if ch<$80 && ch>$60    ;lower case
                        .byte ch-$60 ;ch+$20
                .endif
                .if ch<$60 && ch>$40    ;upper case
                        .byte ch-$40
                .endif
                .if ch=' '              ;space
                        .byte 32
                .endif
                .if ch=':'              ;':'
                        .byte 58
                .endif

.endmac


.macro	call address, count

	.byte count
	.addr address

.endmac


.macro	memcomf address, value

	.byte 0
	.addr address
	.byte value

.endmac


screen_play:

call beginDemo,1
call showPage,4

page0:
        .byte 4
        .byte 24,21-13
        conv 'l'
        .byte 24+7,21-13
        conv 'u','c','a'
        .byte 32,21
        conv 'o','f'
        .byte 12,21+13
        conv 'f',' ','i',' ','r',' ','e'

page1:
        .byte 1
        .byte 20,21
        conv 'a',' ','n',' ','d'

page2:
        .byte 5
        .byte 20+1,21-13
        conv 'b','u','b'
        .byte 20+3*8,21-13
        conv 'i'
        .byte 20+4*8-1,21-13
        conv 's'

        .byte 32,21
        conv 'o','f'
        .byte 8,21+13
        conv 'r','e','s','o','u','r','c','e'

page3:
        .byte 1
        .byte 8,21
        conv 'p','r','e','s','e','n','t','s'

call prepare3AxRotEffects,1

page4:
        .byte 3
        .byte 0+1,30
        conv 'h','e','a','r','t','f'
        .byte 6*8,30
        conv 'i'
        .byte 7*8-1,30
        conv 'x','e','r'


call initRotD,1
	.byte 0,0,0

call effect3AxRotComeIn,32

call initRotD,1
	.byte 1,0,0

call effect3AxRot,8
call effect3AxRot+3,20
call effect3AxRot,16
call effect3AxRot+3,20

call initRotD,1
	.byte 0,0,1

call effect3AxRot,16
call effect3AxRot+3,16

call initRotD,1
	.byte 1,0,0

call effect3AxRot,16
call effect3AxRot+3,16


call initRotD,1
	.byte 2,1,1

call effect3AxRot,16

call initRotD,1
	.byte 3,254,254
call effect3AxRot,16
call effect3AxRot+3,16

call shiftColors,8

call prepareEffect3,1

call initRotD,1
	.byte 1,1,2
call effect3AxRot3,16
call effect3AxRot3+3,16
call initRotD,1
	.byte 1,2,1
call effect3AxRot3,16
call effect3AxRot3+3,16
call initRotD,1
	.byte 2,1,1
call effect3AxRot3,16
call effect3AxRot3+3,16
call initRotD,1
	.byte 2,1,2
call effect3AxRot3,16
call effect3AxRot3+3,16

call initRotD,1
	.byte 2,2,2
call effect3AxRot3,64

call initRotD,1
	.byte 255,2,2
call effect3AxRot3,64

call initRotD,1
	.byte 255,2,255
call effect3AxRot3,48


call prep4Sphere,1

call initRotD,1
	.byte 1,1,1

call effect3AxRot  ,8
call effect3AxRot+3,24

call initRotD,1
	.byte 1,2,1
call effect3AxRot  ,16
call effect3AxRot+3,16

call initRotD,1
	.byte 2,1,3
call effect3AxRot  ,16
call effect3AxRot+3,16

call initRotD,1
	.byte 1,255,3
call effect3AxRot  ,16
call effect3AxRot+3,16

call initRotD,1
	.byte 0,255,254
call effect3AxRot  ,16
call effect3AxRot+3,16

call initRotD,1
	.byte 255,1,254
call effect3AxRot  ,16
call effect3AxRot+3,16


call  restartEffext1,1

call effect3AxRot+3,32
call initRotD,1
	.byte 255,1,2
call effect3AxRot,16
call effect3AxRot+3,16
call initRotD,1
	.byte 1,2,2
call effect3AxRot,16
call effect3AxRot+3,16
call initRotD,1
	.byte 2,1,2
call effect3AxRot,16
call effect3AxRot+3,16
call initRotD,1
	.byte 1,254,2
call effect3AxRot,16
call effect3AxRot+3,16
call initRotD,1
	.byte 2,1,2
call effect3AxRot+3,16
call effect3AxRot,16;4

call effect3AxRotComeOut,16


call prepareBitshader,1

call initRotD,1
	.byte 1,1,1

call doFadeInOut1,11

	.byte $00,$f8
	.byte $30,$f8
	.byte $60,$f8
	.byte $10,$f8
	.byte $40,$f8
	.byte $70,$f8
	.byte $20,$f8
	.byte $50,$f8

	.byte $08,$f0
	.byte $38,$f0
	.byte $68,$f0

call doFadeIn1,1
	.byte $18,$f0


call doFadeInOut2,14

	.byte $48,$f0
	.byte $78,$f0
	.byte $28,$f0
	.byte $58,$f0

	.byte $00,$f8
	.byte $30,$f8
	.byte $60,$f8
	.byte $10,$f8
	.byte $40,$f8
	.byte $70,$f8
	.byte $20,$f8
	.byte $50,$f8

	.byte $08,$f0
	.byte $38,$f0
;	.byte $68,$f0
;	.byte $18,$f0
;	.byte $48,$f0
;	.byte $78,$f0
;	.byte $28,$f0
;	.byte $58,$f0


call initRotD,1
	.byte 2,1,1

call faceAnim1,64

call initRotD,1
	.byte 1,2,1
call faceAnim1,64

call initRotD,1
	.byte 2,254,1
call faceAnim1,64

call initRotD,1
	.byte 253,1,1
call faceAnim1,64

call initRotD,1
	.byte 254,253,1
call faceAnim1,64

call initRotD,1
	.byte 1,2,1
call faceAnim1,64

call initRotD,1
	.byte 3,1,1
call faceAnim1,64

call initRotD,1
	.byte 252,2,1
call faceAnim1,64

call prepareFaceAnim2,1

call initRotD,1
	.byte 6,0,1

call faceAnim2,1

call initRotD,1
	.byte 0,0,1

call faceAnim2,31

call initRotD,1
	.byte 4,1,1

call faceAnim2,1

call initRotD,1
	.byte 4,2,1

call faceAnim2,32
call initRotD,1
	.byte 252,3,1

call faceAnim2,255

call faceAnim2Out,255

call theEnd,1

page5:
        .byte 1
        .byte 12,21
        conv 't','h','e',' ','e','n','d'



