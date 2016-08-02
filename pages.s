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
                        .byte ch+$20
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



screen_play:

page0:
        .byte 3
        .byte 24,21-13
        conv 'L','u','c','a'
        .byte 32,21
        conv 'o','f'
        .byte 12,21+13
        conv 'F',' ','I',' ','R',' ','E'

page1:
        .byte 1
        .byte 20,21
        conv 'a',' ','n',' ','d'

page2:
        .byte 5
        .byte 20+2,21-13
        conv 'b','u','b'
        .byte 20+3*8,21-13
        conv 'i'
        .byte 20+4*8-2,21-13
        conv 's'

        .byte 32,21
        conv 'o','f'
        .byte 8,21+13
        conv 'R','e','s','o','u','r','c','e'

page3:
        .byte 1
        .byte 8,21
        conv 'p','r','e','s','e','n','t','s'

page4:
        .byte 3
        .byte 0+1,30
        conv 'H','E','A','R','T','F'
        .byte 6*8,30
        conv 'I'
        .byte 7*8-1,30
        conv 'X','E','R'

page5:
        .byte 1
        .byte 12,21
        conv 'T','h','e',' ','E','n','d'



